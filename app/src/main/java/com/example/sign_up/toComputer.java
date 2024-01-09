package com.example.sign_up;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import socket.Connect_Application;
import socket.connectToComputer;
import window.four_phone;
import window.tem_message;
import withAndroidCamera.permission;
import withAndroidCamera.receive_runnable;
import withAndroidCamera.withCamera;
import cloud.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class toComputer<mHandler> extends AppCompatActivity {
    private ImageView iv;//显示图像
    private ImageButton sendStringButton = null;//用来发送字符串的按钮s
    private TextView myOrderTextview = null;//显示返回数据
    private TextView match_tem = null;
    private TextView match_time = null;
    private connectToComputer connectToComputer = null;
    private withCamera wCamera;
    private Handler mHandler = null;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    tem_message commomDialog;
    four_phone fourPhone;

    private withCloud wCloud = new withCloud(4444); //与云服务器建立连接


    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    byte[] data = (byte[]) msg.obj;
                    iv.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
                    break;
            }
        }
    };
    // 消息处理


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.match);
        //标题栏透明
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
        iv = (ImageView) findViewById(R.id.camera);
        sendStringButton = this.findViewById(R.id.cut);//开始拍照按钮
        myOrderTextview = findViewById(R.id.match_name);//匹配姓名
        match_tem = findViewById(R.id.match_tem);//匹配姓名
        match_time = findViewById(R.id.match_time);//匹配姓名

        Connect_Application connect_application = (Connect_Application) getApplication();
        this.connectToComputer = connect_application.getConnectToComputer();
        this.wCamera = connect_application.getCamera();

        permission.verifyStoragePermissions(this);
        wCamera.submit(new receive_runnable(wCamera.getSocket(), this.handler));

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


         new Thread(new Runnable() {
        @Override public void run() {
        //                iv.setImageResource(R.drawable.f1);
        while (true){
        try {
        //\src\main\res\drawable\dzh.png
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
        + File.separator+"VeinAPP/dzh.png";
        File file = new File(path);
        if(!file.exists()){file.mkdirs();}
                                if (!Objects.requireNonNull(file.getParentFile()).exists()) {
                                    file.getParentFile().mkdirs();
                                }
        iv.setImageResource(R.drawable.f2);
        iv.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream("\\src\\main\\res\\drawable\\f2.png"))); // 在ImageView中显示Bitmap
        iv.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(path)));
        System.out.println(new File(".").getAbsolutePath());
                            } catch (FileNotFoundException e) {
        } catch (Exception e) {
        e.printStackTrace();
        }
        }
        }
        }).start();

        sendStringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "拍照 被用户点击了。");
                wCloud = new withCloud(4444);
                final match_runnable x = new match_runnable(wCloud.getSocket());
                wCloud.submit(x);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String name = x.getResult();
                if (name.equals("1")) {
                    myOrderTextview.setText("姓名：");
                } else {
                    myOrderTextview.setText("姓名: " + name);
                    match_time.setText(df.format(new Date()));
                    Double tem = 36 + Math.random();
                    String temString = String.format("%.2f", tem);
                    match_tem.setText("体温：" + temString);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //                btdialog_phone();
                    if (tem > 36) {
                        btdialog("您当前体温为" + temString + "℃，如有不适请及时就诊");
                    }
                }
                connectToComputer.setmSendThread("match");//传入"match"，通知服务器开始接收图像并匹配
            }
        });


                mHandler = new Handler() {
                public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        wCamera.submit(new receive_runnable(wCamera.getSocket()));
                        iv.setImageBitmap(connectToComputer.getBitmap()); // 在ImageView中显示Bitmap

                        break;
                    case 2://姓名 温度 时间信息
                        String[] total = connectToComputer.getMyOrder_String().split(" ");
                        if(total.length==3){
                            myOrderTextview.setText(total[0]);
                            match_tem.setText(total[1]);
                            match_time.setText(total[2]);
                            btdialog("");
                        }

                    default:
                        break;
                }
                super.handleMessage(msg);
            }

        };

        connectToComputer.setmHandler(mHandler);
        connectToComputer.setmReceiveThread();//开始接受图像线程
        ImageButton btn_back = (ImageButton) this.findViewById(R.id.return_before);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("widgetDemo", "返回键 被用户点击了。");
                Intent mainIntent = new Intent(toComputer.this, Main_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name_owner);
                bundle.putString("power", power_owner);
                mainIntent.putExtras(bundle);
                toComputer.this.startActivity(mainIntent);
                toComputer.this.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
    }

    public void btdialog(String s) {
        commomDialog = new tem_message(toComputer.this, R.style.dialog, "温度预警", new tem_message.OnCloseListener() {

            @Override
            public void onClickSubmit(Dialog dialog, boolean confirm) {
                //弹窗提示建立成功
                System.out.println("*******************************");
                dialog.dismiss();
            }
        });
        commomDialog.setMessage(s).show();
    }

    public void btdialog_phone() {
        fourPhone = new four_phone(toComputer.this, R.style.dialog, "温度预警", new four_phone.OnCloseListener() {

            @Override
            public void onClickSubmit(Dialog dialog, boolean confirm) {
                //弹窗提示建立成功
                System.out.println("*******************************");
                dialog.dismiss();
            }
        });
        fourPhone.show();
    }

    private static final int WRITE_REQUEST_CODE = 43;

    private void createFile(String mimeType, String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }
}
