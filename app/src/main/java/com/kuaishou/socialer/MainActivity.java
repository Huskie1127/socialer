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
    MaterialProgressBar materialProgressBar;
    private NfcAdapter mNfcAdapter = null;
    private PendingIntent mPendingIntent = null;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShadowButton shadowButtonStart = (ShadowButton)findViewById(R.id.controllerNFC);
        materialProgressBar = (MaterialProgressBar)findViewById(R.id.finding);
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
                        XToast.success(MainActivity.this,"你的设备支持NFC 已开始监听TAG标签",XToast.LENGTH_LONG).show();
                        mPendingIntent = PendingIntent.getActivity(MainActivity.this, 0, new Intent(MainActivity.this,
                                MainActivity.class), 0);
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
        String text = loginActivity.sharedPreferences.getString("name","")+","+
                loginActivity.sharedPreferences.getString("phoneNumber","")+","+
                loginActivity.sharedPreferences.getString("qq","")+","+
                loginActivity.sharedPreferences.getString("weixin","")+","+
                loginActivity.sharedPreferences.getString("weibo","");
        if ("".equals(text))
            XToast.error(MainActivity.this,"请先设置好个人信息 已先发送默认配置信息",XToast.LENGTH_LONG).show();
            text = "error,error,error,error,error";
		/*NdefMessage ndefMessage = new NdefMessage(
				new NdefRecord[] { NdefRecord
						.createApplicationRecord("com.android.calculator2") });*/

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{createTextRecord(text)});

        return ndefMessage;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null,
                    null);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
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
        MaterialDialog.Builder mdb = new MaterialDialog.Builder(MainActivity.this)
                .customView(R.layout.view_dialog_layout,true);
        final MaterialDialog md = mdb.show();
        md.findViewById(R.id.collection).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                mNfcAdapter.enableForegroundDispatch(MainActivity.this, mPendingIntent, null,
                        null);
                //查看优先级是否能一直为最高
                md.dismiss();
                //处理相关逻辑
            }
        });
        String[] temp = text.split(",");
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