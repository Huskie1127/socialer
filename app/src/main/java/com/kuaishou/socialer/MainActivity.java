package com.kuaishou.socialer;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.util.LogTime;
import com.xuexiang.xui.widget.button.shadowbutton.ShadowButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.progress.materialprogressbar.MaterialProgressBar;
import com.xuexiang.xui.widget.toast.XToast;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.nio.charset.Charset;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {
    boolean NFCswitcher = false;
    //NFC是否加载的View
    MaterialProgressBar materialProgressBar;

    //弹框相关View
    MaterialEditText name;
    MaterialEditText phoneNumber;
    MaterialEditText qq;
    MaterialEditText weibo;
    MaterialEditText weixin;

    //NFC相关处理
    private NfcAdapter mNfcAdapter = null;
    private PendingIntent mPendingIntent = null;
    private static final String TAG = "MainActivity";
    private String resText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShadowButton shadowButtonStart = (ShadowButton)findViewById(R.id.controllerNFC);
        materialProgressBar = (MaterialProgressBar)findViewById(R.id.finding);

        //收藏按钮处理逻辑
        shadowButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NFCswitcher == false)
                {
                    //NFC的相关操作 并将 switcher置为true
                    mNfcAdapter = mNfcAdapter.getDefaultAdapter(MainActivity.this);
                    if(mNfcAdapter == null)
                    {
                        XToast.error(MainActivity.this,"你的设备不支持NFC",XToast.LENGTH_LONG).show();
                    }
                    if (mNfcAdapter.isEnabled())
                    {
                        NFCswitcher = true;
                        materialProgressBar.setVisibility(View.VISIBLE);
                        resText = loginActivity.sharedPreferences.getString("name","")+","+
                                loginActivity.sharedPreferences.getString("phoneNumber","")+","+
                                loginActivity.sharedPreferences.getString("qq","")+","+
                                loginActivity.sharedPreferences.getString("weixin","")+","+
                                loginActivity.sharedPreferences.getString("weibo","");
                        if (",,,,".equals(resText))
                        {
                            XToast.error(MainActivity.this,"请先设置好个人信息 再扫描 nfc tag",XToast.LENGTH_LONG).show();
                            MaterialDialog.Builder mdb = new MaterialDialog.Builder(MainActivity.this)
                                    .customView(R.layout.edit_dialog_layout,true).cancelable(false);

                            final MaterialDialog md=mdb.show();
                            name = (MaterialEditText)md.findViewById(R.id.EditName);
                            phoneNumber = (MaterialEditText)md.findViewById(R.id.EditPhoneNumber);
                            qq = (MaterialEditText)md.findViewById(R.id.editQQ);
                            weibo = (MaterialEditText)md.findViewById(R.id.editWeiBo);
                            weixin = (MaterialEditText)md.findViewById(R.id.editWeChat);
                            SharedPreferences getValue = loginActivity.sharedPreferences;
                            name.setText(getValue.getString("name",""));
                            phoneNumber.setText(getValue.getString("phoneNumber",""));
                            qq.setText(getValue.getString("qq",""));
                            weixin.setText(getValue.getString("weixin",""));
                            weibo.setText(getValue.getString("weibo",""));
                            md.findViewById(R.id.SubmitButton).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //UPDATE 数据具体操作
                                    SharedPreferences.Editor tempEditor = loginActivity
                                            .sharedPreferences.edit();
                                    tempEditor.putString("name",name.getEditValue().toString());
                                    tempEditor.putString("phoneNumber",phoneNumber.getEditValue().toString());
                                    tempEditor.putString("qq",qq.getEditValue().toString());
                                    tempEditor.putString("weixin",weixin.getEditValue().toString());
                                    tempEditor.putString("weibo",weibo.getEditValue().toString());
                                    tempEditor.apply();
                                    md.dismiss();

                                    resText = loginActivity.sharedPreferences.getString("name","")+","+
                                            loginActivity.sharedPreferences.getString("phoneNumber","")+","+
                                            loginActivity.sharedPreferences.getString("qq","")+","+
                                            loginActivity.sharedPreferences.getString("weixin","")+","+
                                            loginActivity.sharedPreferences.getString("weibo","");
                                    NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{createTextRecord(resText)});
                                    XToast.success(MainActivity.this,"更新个人资料成功!",XToast.LENGTH_LONG).show();
                                }
                            });
                        }
                        XToast.success(MainActivity.this,"你的设备支持NFC 已开始监听TAG标签",XToast.LENGTH_LONG).show();
                        mPendingIntent = PendingIntent.getActivity(MainActivity.this, 0, new Intent(MainActivity.this,
                                MainActivity.class), 0);

                        //NFC发送及前置操作
                        mNfcAdapter.setNdefPushMessageCallback(MainActivity.this, MainActivity.this);
                        mNfcAdapter.setOnNdefPushCompleteCallback(MainActivity.this, MainActivity.this);
                        mNfcAdapter.enableForegroundDispatch(MainActivity.this, mPendingIntent, null,
                                null);
                    }else {
                        XToast.warning(MainActivity.this,"手动开启NFC",XToast.LENGTH_LONG).show();
                    }
                    }

                else
                {
                    NFCswitcher = false;
                    materialProgressBar.setVisibility(View.INVISIBLE);
                    mNfcAdapter.disableForegroundDispatch(MainActivity.this);
                    mNfcAdapter = null;
                    XToast.success(MainActivity.this,"关闭寻找TAG标签",XToast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        Log.d("message", "complete");
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        //从NDEFRecord创建NDEFMessage 供NFC发送Tag标签使用
            NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{createTextRecord(resText)});
            return ndefMessage;
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        if (mNfcAdapter != null && NFCswitcher == true)
//            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null,
//                    null);
//        else if (mNfcAdapter != null && NFCswitcher == false)
//            mNfcAdapter.disableForegroundDispatch(this);
        processIntent(intent);
        //收到NFC TAG之后调用processIntent对标签做处理 实现业务相关操作
    }

    public NdefRecord createTextRecord(String text) {
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(
                Charset.forName("US-ASCII"));
        Charset utfEncoding = Charset.forName("UTF-8");
        byte[] textBytes = text.getBytes(utfEncoding);
        int utfBit = 0;
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
                textBytes.length);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);

        return record;
    }

    void processIntent(Intent intent) {

        Parcelable[] rawMsgs = intent
                .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        NdefMessage msg = (NdefMessage) rawMsgs[0];
        String text = TextRecord.parse(msg.getRecords()[0]).getText();
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        Log.d(TAG, "-------------->received ");
        final String[] temp = text.split(",");
        MaterialDialog.Builder mdb = new MaterialDialog.Builder(MainActivity.this)
                .customView(R.layout.view_dialog_layout,true).cancelable(false);
        final MaterialDialog md = mdb.show();
        md.findViewById(R.id.collection).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                mNfcAdapter.enableForegroundDispatch(MainActivity.this, mPendingIntent, null,
                        null);
                //查看优先级是否能一直为最高
                collectedUser collecteduser = new collectedUser();
                collecteduser.setName(temp[0]);
                collecteduser.setPhoneNumber(temp[1]);
                collecteduser.setQQ(temp[2]);
                collecteduser.setWeChat(temp[3]);
                collecteduser.setWeiBo(temp[4]);
                informationListActivity.collectedUsers.add(collecteduser);
                informationListActivity.informationAdapter.notifyItemInserted(informationListActivity.collectedUsers.size()-1);
                md.dismiss();
                //处理相关逻辑
            }
        });
        MaterialEditText viewName = (MaterialEditText)md.findViewById(R.id.ViewName);
        viewName.setText(temp[0]);
        MaterialEditText viewphoneNumber = (MaterialEditText)md.findViewById(R.id.ViewPhoneNumber);
        viewphoneNumber.setText(temp[1]);
        MaterialEditText viewqq = (MaterialEditText)md.findViewById(R.id.ViewQQ);
        viewqq.setText(temp[2]);
        MaterialEditText viewWeixin = (MaterialEditText)md.findViewById(R.id.ViewWeChat);
        viewWeixin.setText(temp[3]);
        MaterialEditText viewWeibo = (MaterialEditText)md.findViewById(R.id.ViewWeiBo);
        viewWeibo.setText(temp[4]);
        }
}