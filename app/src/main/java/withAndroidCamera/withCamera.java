package withAndroidCamera;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.*;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class withCamera {

    private String host = "172.20.66.214";//android驱动相机的IP地址
    private int port = 6666;
    private DatagramSocket withCameraSocket = null;
    private Executor executor = Executors.newCachedThreadPool();

    public withCamera(int port) {
        try {
            this.port = port;
            this.withCameraSocket = new DatagramSocket(this.port); //这个地方向android Camera请求建立连接
            System.out.println("建立连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void submit(Runnable x){
        this.executor.execute(x);
    }
    public  DatagramSocket getSocket(){
        return this.withCameraSocket;
    }

    public withCamera(int port) {
        try {
            this.port = port;
            withCameraSocket = new ServerSocket(this.port);
            System.out.println("准备连接");
            while (!withCameraSocket.isClosed()) {
                acceptSocket = withCameraSocket.accept();
                inputStream = new DataInputStream(acceptSocket.getInputStream());
                System.out.println("连接成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void submit(String path){
        executor.execute(new save_image_runnable(this.acceptSocket,path));
    }

    public class save_image_runnable implements Runnable{
        private Socket socket;
        private DataInputStream inputStream;
        private String path;
        public save_image_runnable(Socket s,String path) {
            try {
                this.socket = s;
                this.path = path;
                this.inputStream = new DataInputStream(this.socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            try {
                while (true) {
                    int temp_size = inputStream.readInt();
                    if (temp_size <= 0) {
                        continue;
                    }
                    byte[] temp_data = new byte[temp_size];
                    int temp_len = 0;
                    while (temp_len < temp_size) {
                        temp_len += inputStream.read(temp_data, temp_len, temp_size - temp_len);
                    }
                    if (temp_size < 30) {continue;}//保持连接发送的短串
                    try (FileOutputStream fileOutputStream =
                                 new FileOutputStream(new File(path))) {
                        fileOutputStream.write(temp_data);
                        System.out.println("success");
                    }
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void save_image(String path) {
        try {
            while (true) {
                int temp_size = inputStream.readInt();
                if (temp_size <= 0) {
                    continue;
                }
                System.out.println(temp_size);
                byte[] temp_data = new byte[temp_size];
                int temp_len = 0;
                while (temp_len < temp_size) {
                    temp_len += inputStream.read(temp_data, temp_len, temp_size - temp_len);
                }
                if (temp_size < 30) {continue;}//保持连接发送的短串
                try (FileOutputStream fileOutputStream =
                             new FileOutputStream(new File(path))) {
                    fileOutputStream.write(temp_data);
                    System.out.println("成功接收图像");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket(){
        return this.acceptSocket;
    }

}
