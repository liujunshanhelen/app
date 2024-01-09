package com.example.sign_up;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import socket.Connect_Application;
import socket.connectToComputer;
import withAndroidCamera.permission;
import withAndroidCamera.receive_runnable;
import withAndroidCamera.withCamera;

public class sign_up3 extends AppCompatActivity {
    private socket.connectToComputer connectToComputer = null;
    private Handler mHandler = null;
    private ImageView iv;//显示图像
    private withCamera wCamera;
    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    byte[] data = (byte[]) msg.obj;
                    iv.setImageBitmap(BitmapFactory.decodeByteArray(data,0,data.length));
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_up3);
        //标题透明
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
        Connect_Application connect_application = (Connect_Application) getApplication();
        this.connectToComputer = connect_application.getConnectToComputer();
        this.wCamera = connect_application.getCamera();

        permission.verifyStoragePermissions(this);
        wCamera.submit(new receive_runnable(wCamera.getSocket(), this.handler));

        iv = (ImageView) findViewById(R.id.camera);
        //下一步按钮的监听
        ImageButton btn = (ImageButton) this.findViewById(R.id.cut);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "拍照 被用户点击了。");
//                connectToComputer.setmSendThread("sign");//传入"sign"，通知服务器开始拍照
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                }
                Intent mainIntent = new Intent(sign_up3.this, Main_activity.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",name_owner);
                bundle.putString("power",power_owner);
                mainIntent.putExtras(bundle);
                sign_up3.this.startActivity(mainIntent);
                sign_up3.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        iv.setImageBitmap(connectToComputer.getBitmap()); // 在ImageView中显示Bitmap
                        break;
                    case 2:
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        connectToComputer.setmHandler(mHandler);
        //connectToComputer.setmReceiveThread();//开始接受图像线程
        ImageButton btn_back = (ImageButton) this.findViewById(R.id.return_before);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "返回键 被用户点击了。");
                Intent mainIntent = new Intent(sign_up3.this, Main_activity.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",name_owner);
                bundle.putString("power",power_owner);
                mainIntent.putExtras(bundle);
                sign_up3.this.startActivity(mainIntent);
                sign_up3.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
    }
}
