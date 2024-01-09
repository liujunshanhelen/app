package com.example.sign_up;

import Database.database;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import socket.Connect_Application;
import socket.connectToComputer;
import window.CommomDialog;

public class Main_common extends AppCompatActivity {
    MenuButton menubutton;
    static CommomDialog commomDialog;
    private SearchView searchView;
    private ListView listView;

    private String url_string;
    private connectToComputer connectToComputer = null;
    private Handler mHandler = null;//内部子线程给主线程发消息的控制
    private int port = 8888;
    private database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.common_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle bundle = getIntent().getExtras();
        final String name_owner = bundle.getString("name");
        final String power_owner = bundle.getString("power");

        Connect_Application connect_application = (Connect_Application) getApplication();
        this.connectToComputer = connect_application.getConnectToComputer();
        //设置按钮的监听
        ImageButton btn = (ImageButton) this.findViewById(R.id.setting);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "个人信息 被用户点击了。");
                Intent mainIntent = new Intent(Main_common.this, login.class);
                Main_common.this.startActivity(mainIntent);
                Main_common.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                btdialog(v);
            }
        });
        menubutton = new MenuButton((ImageButton) findViewById(R.id.button),
                (ImageButton) findViewById(R.id.match),
                (ImageButton) findViewById(R.id.add),
                (ImageButton) findViewById(R.id.search));
        menubutton.ClickButton();
        //设置按钮的监听
        ImageButton btn = (ImageButton) this.findViewById(R.id.setting);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "菜单 被用户点击了。");
                btdialog(v);
            }
        });
        //设置按钮的监听
        ImageButton btn_match = (ImageButton) this.findViewById(R.id.match);
        btn_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "匹配 被用户点击了。");
                Intent mainIntent = new Intent(Main_common.this, toComputer.class);
                Main_common.this.startActivity(mainIntent);
                Main_common.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        //设置按钮的监听
        Button btn_add = (Button) this.findViewById(R.id.shan);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "扇形图 被用户点击了。");
                Intent mainIntent = new Intent(Main_common.this, shanData.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",name_owner);
                bundle.putString("power",power_owner);
                mainIntent.putExtras(bundle);
                Main_common.this.startActivity(mainIntent);
                Main_common.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        //设置按钮的监听
        Button btn_search = (Button) this.findViewById(R.id.search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "搜索 被用户点击了。");
                Intent mainIntent = new Intent(Main_common.this, search2.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",name_owner);
                bundle.putString("power",power_owner);
                mainIntent.putExtras(bundle);
                Main_common.this.startActivity(mainIntent);
                Main_common.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void btdialog(View view) {
        commomDialog = new CommomDialog(Main_common.this, R.style.dialog, "请输入地址", new CommomDialog.OnCloseListener() {

            @Override
            public void onClickCancel(Dialog dialog, boolean confirm) {
                dialog.dismiss();
            }

            @Override
            public void onClickSubmit(Dialog dialog, boolean confirm) {
                url_string = commomDialog.contentTxt.getText().toString();
                connectToComputer.setHost(url_string);//设置安卓端的主机地址为电脑的地址
                //建立socket
                connectToComputer.setClientSocket(port);//设置一个新的套接字
                //弹窗提示建立成功
                displayToast("连接成功！");
                dialog.dismiss();
            }
        });
        commomDialog.setTitle("输入地址").show();
    }
    // 弹窗显示  显示Toast函数
    private void displayToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}