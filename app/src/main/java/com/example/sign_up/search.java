package com.example.sign_up;

import Database.DBapplication;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import hlq.tablbeview.OnTableClick;
import hlq.tablbeview.TableView;
import Database.DBapplication;
import Database.database;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import socket.Connect_Application;

public class search extends AppCompatActivity {
    private String[] mlistHead={"时间","姓名"};//声明表格表头
    private String[] mlistContent;

    private TableView tableView;
    private database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Connect_Application connect_application = (Connect_Application) getApplication();
//        DBapplication dBapplication1=(DBapplication)getApplication();
        this.db=connect_application.getDB();

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search);
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
        //搜索框
        final EditText editText = (EditText) findViewById(R.id.search_text);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    String name = editText.getText().toString();
                    JSONArray results = db.executeFind("'"+name+"'");
                    String[] name1={"time","tem"};
                    mlistHead= new String[]{"时间", "温度"};
                    mlistContent=new String[results.length()*2];
                    if(results!=null){
                        for (int i = 0; i < results.length(); i++) {
                            try {
                                JSONObject jsonObject = (JSONObject) results.get(i);
                                for (int j = 0; j < 2; j++) {
                                    mlistContent[i*2+j]=String.valueOf(jsonObject.get(name1[j]));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        displayToast("查无此人");
                    }
                    tableView.removeAllViews();
                    tableView.setTableHead(mlistHead);
                    tableView.setTableContent(mlistContent);
                }
                return true;
            }
        });
        tableView = findViewById(R.id.tabview);
        tableView.setTable(new OnTableClick() {
            @Override
            public void onTableClickListener(int row, int col) {

            }
        });

        JSONArray results = db.executeFindAll("record_all");
        String[] name={"time","name"};
        mlistContent=new String[results.length()*2];
        if(results!=null){
            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject jsonObject = (JSONObject) results.get(i);
                    for (int j = 0; j < 2; j++) {
                        mlistContent[i*2+j]=String.valueOf(jsonObject.get(name[j]));
                        System.out.println(mlistContent[i*2+j]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        if(results!=null){
            int length = results.length();
            int length_minus = length -1;
            for (int i = length-1; i >=0; i--) {
                try {
                    JSONObject jsonObject = (JSONObject) results.get(i);
                    for (int j = 0; j < 2; j++) {
                        mlistContent[(length_minus-i)*2+j]=String.valueOf(jsonObject.get(name[j]));
                        System.out.println(mlistContent[i*2+j]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        tableView.setTableHead(mlistHead);
        tableView.setTableContent(mlistContent);
        ImageButton btn_back = (ImageButton) this.findViewById(R.id.return_before);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "返回键被用户点击了。");
                Intent mainIntent = new Intent(search.this, Main_activity.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",name_owner);
                bundle.putString("power",power_owner);
                mainIntent.putExtras(bundle);
                search.this.startActivity(mainIntent);
                search.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        ImageButton shan_menu = (ImageButton) this.findViewById(R.id.shan_menu);
        shan_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "扇形图被用户点击了。");
                Intent mainIntent = new Intent(search.this, shanData.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",name_owner);
                bundle.putString("power",power_owner);
                mainIntent.putExtras(bundle);
                search.this.startActivity(mainIntent);
                search.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
    }
    // 弹窗显示  显示Toast函数
    private void displayToast(String s) {
        Toast.makeText(this, s, 10).show();
    }
}