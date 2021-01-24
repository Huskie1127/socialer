package com.kuaishou.socialer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.toast.XToast;

public class actionBar extends LinearLayout {
    MaterialEditText name;
    MaterialEditText phoneNumber;
    MaterialEditText qq;
    MaterialEditText weibo;
    MaterialEditText weixin;



    public actionBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.actionbar,this);
        ImageButton userPhotoButton = (ImageButton) view.findViewById(R.id.userPhotoImageButton);
        userPhotoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog.Builder mdb = new MaterialDialog.Builder(getContext())
                        .customView(R.layout.edit_dialog_layout,true);
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
                md.findViewById(R.id.SubmitButton).setOnClickListener(new OnClickListener() {
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
                        XToast.success(getContext(),"更新个人资料成功!",XToast.LENGTH_LONG).show();
                    }
                });
            }
        });

        ImageButton friendListButton = (ImageButton) view.findViewById(R.id.friendPhotoImageButton);
        friendListButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),informationListActivity.class);
                getContext().startActivity(intent);
            }
        });
    }
}
