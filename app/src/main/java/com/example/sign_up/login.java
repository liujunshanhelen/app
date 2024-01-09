package com.example.sign_up;

import Database.database;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import socket.Connect_Application;

public class login extends AppCompatActivity {
    String login_id,login_secret;
    private database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
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
        final EditText id = (EditText) findViewById(R.id.id_input);
        final EditText secret = (EditText) findViewById(R.id.secret_input);
        secret.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Connect_Application connect_application = (Connect_Application) getApplication();
//        DBapplication dBapplication1=(DBapplication)getApplication();
        this.db=connect_application.getDB();

        //下一步按钮的监听
        Button btn = (Button) this.findViewById(R.id.login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "登录 被用户点击了。");
                login_id=id.getText().toString();
                login_secret=secret.getText().toString();

                JSONArray results = db.executeLogin(login_id,login_secret);
                if(results!=null) {
                        try {
                            JSONObject jsonObject = (JSONObject) results.get(0);
                            String power=String.valueOf(jsonObject.get("power"));
                            String name_owner=String.valueOf(jsonObject.get("name"));
                            Bundle bundle=new Bundle();
                        if(power.equals("1")){
                            Intent mainIntent = new Intent(login.this, Main_activity.class);
                            bundle.putString("name",name_owner);
                            bundle.putString("power","1");
                            mainIntent.putExtras(bundle);
                            login.this.startActivity(mainIntent);
                            login.this.finish();
                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                        }else{
                            Intent mainIntent = new Intent(login.this, Main_common.class);
                            bundle.putString("name",name_owner);
                            bundle.putString("power","0");
                            mainIntent.putExtras(bundle);
                            login.this.startActivity(mainIntent);
                            login.this.finish();
                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }else{
                    displayToast("用户名或密码错误");
                }
            }
        });
    }
    // 弹窗显示  显示Toast函数
    private void displayToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
