package com.example.sign_up;

import Database.database;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import socket.Connect_Application;

import java.util.ArrayList;
import java.util.List;

public class shanData extends AppCompatActivity {
    PieChart pc;
    List<PieChartBean> list = new ArrayList<>();
    private database db;
    private TextView num;
    private TextView title;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connect_Application connect_application = (Connect_Application) getApplication();
        DBapplication dBapplication1=(DBapplication)getApplication();
        this.db = connect_application.getDB();
        Connect_Application dBapplication=(Connect_Application)getApplication();
        this.db=dBapplication.getDB();

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shan);
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

        pc = findViewById(R.id.data);
        num = findViewById(R.id.num);
        title = findViewById(R.id.title);
        time = findViewById(R.id.time);
        JSONArray results = db.executeShan();
        int a = 0, b = 0, c = 0, d = 0,max, now;
        String most = null;
        for (int i = 0; i < results.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) results.get(i);
                now = Integer.parseInt(String.valueOf(jsonObject.get("time")).split(" ")[1].split(":")[0]);
                System.out.println(now);
                if (now >= 0 && now < 6) {
                    a++;

                } else if (now < 12) {
                    b++;

                } else if (now < 18) {
                    c++;

                } else {
                    d++;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(a>b){
            max=a;
            most="00:00-05:59";
        }else{
            max=b;
            most="06:00-11:59";
        }if(max<c){
            max=c;
            most="12:00-17:59";
        }if(max<d){
            max=d;
            most="18:00-23:59";
        }
        int sum = a + b + c + d;
        num.setText(a + "\n" + b + "\n" + c + "\n" + d+"\n"+sum+"");
        title.setText("00:00-05:59\n06:00-11:59\n12:00-17:59\n18:00-23:59\n总计");
        time.setText(most);
        num.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        list.add(new PieChartBean("00:00-05:59", (float) a / sum, Color.WHITE));
        list.add(new PieChartBean("06:00-11:59", (float) b / sum, Color.rgb(217, 217, 217)));
        list.add(new PieChartBean("12:00-17:59", (float) c / sum, Color.GRAY));
        list.add(new PieChartBean("18:00-23:59", (float) d / sum, Color.BLACK));
        pc.setDate(list);
        ImageButton btn_back = (ImageButton) this.findViewById(R.id.return_before);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent;
                if(power_owner.equals("1")){
                    mainIntent = new Intent(shanData.this, Main_activity.class);
                }else {
                    mainIntent = new Intent(shanData.this, Main_common.class);
                }
                Bundle bundle=new Bundle();
                bundle.putString("name",name_owner);
                bundle.putString("power",power_owner);
                mainIntent.putExtras(bundle);
                shanData.this.startActivity(mainIntent);
                shanData.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
    }
}

