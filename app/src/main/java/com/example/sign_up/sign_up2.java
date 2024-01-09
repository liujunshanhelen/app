package com.example.sign_up;

import Database.DBapplication;
import Database.database;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import socket.Connect_Application;
import socket.connectToComputer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class sign_up2 extends AppCompatActivity {
    public String provence, city, street, home;
    private socket.connectToComputer connectToComputer = null;
    private database db;
    private DataBase.DBBean db;
    String id0_next, name_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_up2);
        TextView textView =findViewById(R.id.name_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        //sign_up1输入获取
        Bundle bundle = getIntent().getExtras();
        final String basic = bundle.getString("basic");
        final String name_owner = bundle.getString("name");
        final String power_owner = bundle.getString("power");
        final String[] basic_data = basic.split(" ");
        Connect_Application connect_application = (Connect_Application) getApplication();
        this.connectToComputer = connect_application.getConnectToComputer();
        DBapplication dBapplication1=(DBapplication)getApplication();
        this.db=connect_application.getDB();
        //获取共享的数据库类
        DBapplication dBapplication=(DBapplication)getApplication();


        this.db = connect_application.getDBBean();

        //输入省份编辑框
        final EditText editText1 = (EditText) findViewById(R.id.provence_input);

        //输入城市编辑框
        final EditText editText2 = (EditText) findViewById(R.id.city_input);

        //输入区县编辑框
        final EditText editText3 = (EditText) findViewById(R.id.street_input);

        //输入区县编辑框
        final EditText editText4 = (EditText) findViewById(R.id.home_input);

        //下一步按钮的监听
        Button btn = (Button) this.findViewById(R.id.save_location);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "下一步 被用户点击了。");
                provence=editText1.getText().toString();
                city=editText1.getText().toString();
                street=editText1.getText().toString();
                home=editText1.getText().toString();
                db.executeInsert(basic+",'"
                        +provence + city + street + home+"'");

                                db.executeQuery("person(name,tel,home_location,id_number)", '\'' + basic_data[0] + '\'' + ',' + '\'' + basic_data[2] + '\'' + ',' + '\'' + provence + city + street + home + '\'' + ',' + '\'' + basic_data[1] + '\'');
                ResultSet re = db.executeFindMAXID("person", "id");
                try {
                    while (re.next()) {
                        id0_next = re.getString("id");
                        name_next = re.getString("name");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                db.executeCreateNewTable(name_next);
                Intent mainIntent = new Intent(sign_up2.this, sign_up3.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",name_owner);
                bundle.putString("power",power_owner);
                mainIntent.putExtras(bundle);
                sign_up2.this.startActivity(mainIntent);
                sign_up2.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        ImageButton btn_back = (ImageButton) this.findViewById(R.id.return_before);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "返回键 被用户点击了。");
                Intent mainIntent = new Intent(sign_up2.this, Main_activity.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",name_owner);
                bundle.putString("power",power_owner);
                mainIntent.putExtras(bundle);
                sign_up2.this.startActivity(mainIntent);
                sign_up2.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
    }
}