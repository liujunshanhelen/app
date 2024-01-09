package com.example.sign_up;

import Database.DBapplication;
import Database.database;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileWithBitmapCallback;
import socket.Connect_Application;
import socket.connectToComputer;

import java.io.File;
import java.io.IOException;


public class sign_up1 extends AppCompatActivity {
    public String name, id, phone, secret;
    private database db;
    private CheckBox limit;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private ImageView imageView;
    private Button choose;
    private View layout;
    private TextView cancelTV;
    private int face = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_up1);
        imageView = findViewById(R.id.origin_photo);
        choose = findViewById(R.id.choose);
        //去掉标题栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        Bundle bundle = getIntent().getExtras();
        final String name_owner = bundle.getString("name");
        final String power_owner = bundle.getString("power");
        //输入姓名编辑框
        final EditText editText1 = (EditText) findViewById(R.id.name_input);

        //输入身份证号编辑框
        final EditText editText2 = (EditText) findViewById(R.id.id_input);

        //输入联系方式编辑框
        final EditText editText3 = (EditText) findViewById(R.id.phone_input);

        //输入密码编辑框
        final EditText editText4 = (EditText) findViewById(R.id.secret_input);
        editText4.setTransformationMethod(PasswordTransformationMethod.getInstance());

        limit = findViewById(R.id.limit);
        Connect_Application connect_application = (Connect_Application) getApplication();
        DBapplication dBapplication1=(DBapplication)getApplication();
        this.db=connect_application.getDB();
        //下一步按钮的监听
        Button btn = (Button) this.findViewById(R.id.save_basic);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "下一步 被用户点击了。");
                Intent mainIntent = new Intent(sign_up1.this, sign_up2.class);
                Bundle bundle = new Bundle();
                name = editText1.getText().toString();
                id = editText2.getText().toString();
                phone = editText3.getText().toString();
                secret = editText4.getText().toString();
                if (limit.isChecked()) {
                    bundle.putString("basic", "'" + name + "','" + phone + "','" + id + "','" + secret + "',1," + face);
                } else {
                    bundle.putString("basic", "'" + name + "','" + phone + "','" + id + "','" + secret + "',0," + face);
                }
                mainIntent.putExtras(bundle);
                sign_up1.this.startActivity(mainIntent);
                sign_up1.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        ImageButton btn_back = (ImageButton) this.findViewById(R.id.return_before);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "返回键 被用户点击了。");
                Intent mainIntent = new Intent(sign_up1.this, Main_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name_owner);
                bundle.putString("power", power_owner);
                mainIntent.putExtras(bundle);
                sign_up1.this.startActivity(mainIntent);
                sign_up1.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "选择头像 被用户点击了。");
                viewInit();
            }
        });
    }

    /*
初始化控件方法
 */
    public void viewInit() {

        builder = new AlertDialog.Builder(this);//创建对话框
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.dialog_select_photo, null);//获取自定义布局
        builder.setView(layout);//设置对话框的布局
        dialog = builder.create();//生成最终的对话框
        dialog.show();//显示对话框
        ImageButton face1 = layout.findViewById(R.id.face1);
        ImageButton face2 = layout.findViewById(R.id.face2);
        ImageButton face3 = layout.findViewById(R.id.face3);
        ImageButton face4 = layout.findViewById(R.id.face4);
        ImageButton face5 = layout.findViewById(R.id.face5);
        ImageButton face6 = layout.findViewById(R.id.face6);
        cancelTV = layout.findViewById(R.id.cancel);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "取消 被用户点击了。");
                dialog.dismiss();
            }
        });
        face1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.face1);
                Log.i("widgetDemo", "f1 被用户点击了。");
                face = 1;
                dialog.dismiss();
            }
        });
        face2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.face2);
                Log.i("widgetDemo", "f2 被用户点击了。");
                face = 2;
                dialog.dismiss();
            }
        });
        face3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.face3);
                Log.i("widgetDemo", "f3 被用户点击了。");
                face = 3;
                dialog.dismiss();
            }
        });
        face4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.face4);
                Log.i("widgetDemo", "f4 被用户点击了。");
                face = 4;
                dialog.dismiss();
            }
        });
        face5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.face5);
                Log.i("widgetDemo", "f5 被用户点击了。");
                face = 5;
                dialog.dismiss();
            }
        });
        face6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.face6);
                Log.i("widgetDemo", "f6 被用户点击了。");
                face = 6;
                dialog.dismiss();
            }
        });
    }
}