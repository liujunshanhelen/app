package withAndroidCamera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.sign_up.R;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.List;
import java.util.Objects;

//从android那个地方接收发过来的图片，存在某个路径下
public class receive_runnable extends AppCompatActivity implements Runnable {
    private DatagramSocket socket = null;
    private DatagramPacket packet = null;
    private ImageView iv = null;
    private Handler mHandler = null;

    public receive_runnable(DatagramSocket socket, Handler mHandler){
        this.socket = socket;
        this.packet = new DatagramPacket(new byte[100000], 100000);
        this.mHandler = mHandler;
    }
    @Override
    public void run() {
        System.out.println("开始接收数据...");

        while (true) {
            try {
                this.socket.receive(this.packet);		//这个地方是从udp那拿到数据包
                System.out.println("收到了一个数据包");
                byte[] data = packet.getData();		//这个地方是获得数据包里的数据
                System.out.println(packet.getAddress().getHostAddress() + data.length);
                //TODO
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = data;
                mHandler.sendMessage(msg);
                File file = new File("\\src\\main\\res\\drawable\\hh.png");
                if(!file.exists()){file.mkdirs();}
                        try
                        {
                            FileOutputStream fileOutputStream = openFileOutput("image.png",MODE_PRIVATE);
                            fileOutputStream.write(data);
                            System.out.println("success"+data.length);
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                this.parse_image(data,file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void parse_image(byte[] data, File file){
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
            System.out.println("success"+data.length);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
