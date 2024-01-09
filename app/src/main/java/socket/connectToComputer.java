package socket;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class connectToComputer {

    private String host = null;//主机地址s
    private String myOrder_String;//电脑sever端返回的字符串
    private Socket clientSocket = null;//服务端套接字
    private Socket sendStringClientSocket = null;
    private ReceiveThread mReceiveThread = null;//接收图像线程
    private Bitmap bitmap;//图像的格式
    private Send_Runnable send_runnable;//发送数据的线程
    private Executor executor = Executors.newCachedThreadPool();//线程池
    private Handler mHandler = null;

    //    @DZH
    private Send_Runnable mSendThread = null;//发送数据线程

    public void setClientSocket(int port) {
        try {
            this.clientSocket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    /**
     * 获取电脑端返回的数据
     *
     * @return
     */
    public String getMyOrder_String() {
        return myOrder_String;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
        public void setHost(String host) {
        this.host = host;
    }

    /**
     * 启动接收数据的线程
     */
    public void setmReceiveThread() {
        try {
            this.mReceiveThread = new ReceiveThread(clientSocket);
            mReceiveThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    @DZH

    /**
     * 启动android发送数据的线程
     *
     * @param send_message
     */
    public void setmSendThread(String send_message) {
        try {
            this.mSendThread = new Send_Runnable(new Socket(host, clientSocket.getPort()));
        } catch (Exception e) {
        }
        mSendThread.setString(send_message);
        mSendThread.start();
    }

    /**
     * 安卓端接收数据线程
     */
    private class ReceiveThread extends Thread {

        private Socket s;

        DataInputStream dataInput;

        public ReceiveThread(Socket s) throws IOException {
            this.s = s;
            //客户端接收服务端发送的数据的缓冲区
            dataInput = new DataInputStream(
                    s.getInputStream());
        }


        public void run() {
            try {
                while (true) {
                    Log.e("5555555555555", "11111111111");
                    int size = dataInput.readInt();//获取服务端发送的数据的大小
                    //更新名字
                    if (size < 100) {//如果大小小于10 证明发送的是个字符串  显示在对应的文字显示区域
                        Log.e("size in client", Integer.toString(size));
                        byte[] data = new byte[size];
                        int len = 0;
                        while (len < size) {
                            len += dataInput.read(data, len, size - len);
                        }
                        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
                        myOrder_String = new String(data);
                        if (myOrder_String != "222") {
                            Message message = new Message();
                            message.what = 2;
                            mHandler.sendMessage(message);
                            Thread.sleep(100);
                        }
                    } else {//否则就是服务端发送的相机的图像的数据，将其解析之后显示在image中
                        byte[] data = new byte[size];
                        int len = 0;
                        //将二进制数据写入data数组
                        while (len < size) {
                            len += dataInput.read(data, len, size - len);
                        }
                        //存储数据的缓冲区
                        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
                        //将二进制数据进行解码  解析成bitmap格式  为显示做准备
                        bitmap = BitmapFactory.decodeByteArray(data, 0,
                                data.length);
                        //显示成PNG格式　　
                        bitmap.compress(CompressFormat.PNG, 100, outPut);
                        //发消息给主线程，让其更新图像显示
                        Message message = new Message();
                        message.what = 1;
                        mHandler.sendMessage(message);
//					Thread.sleep(100);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 安卓端发送数据
     */
    public class Send_Runnable extends Thread {
        private java.net.Socket send_StringSocket;
        private String receiveString = null;
        private String myString = null;

        public Send_Runnable(java.net.Socket socket) {
            this.send_StringSocket = socket;
        }

        /**
         * 设置要发送的命令字符串
         *
         * @param myOrder 要发送的字符串
         */
        public void setString(String myOrder) {
            this.myString = myOrder;
        }


        public void run() {
            DataOutputStream dos = null;
            try {
                dos = new DataOutputStream(send_StringSocket.getOutputStream());
                while (myString != null && !send_StringSocket.isClosed()) {
                    System.out.println(myString);
                    byte[] data = myString.getBytes();//将字符串转换成二进制数，为了底层发送
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    baos.write(data, 0, data.length);
                    baos.flush();
                    byte[] orderStringInByte = baos.toByteArray();
                    baos.close();
                    dos.writeInt(orderStringInByte.length);
                    dos.write(orderStringInByte, 0, orderStringInByte.length);
                    dos.flush();
                    dos.close();
                    Thread.sleep(200);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dos != null) dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //客户端接收数据线程
    public class ReceiveRunnable implements Runnable {

        private java.net.Socket cliebtSocket;
        private String receiveString;
        DataInputStream dataInput;

        public ReceiveRunnable(java.net.Socket s) throws IOException {
            this.cliebtSocket = s;
            //客户端接收服务端发送的数据的缓冲区
            dataInput = new DataInputStream(
                    s.getInputStream());
        }


        public void run() {
            try {
                while (!cliebtSocket.isClosed()) {

                    int size = dataInput.readInt();//获取服务端发送的数据的大小

                    System.out.println("size in client:  " + size);
                    byte[] data = new byte[size];
                    int len = 0;
                    while (len < size) {
                        len += dataInput.read(data, len, size - len);
                    }
                    ByteArrayOutputStream outPut = new ByteArrayOutputStream();
                    receiveString = new String(data);
                    System.out.println(receiveString);
                    if (receiveString.length() > 2) {
                        cliebtSocket.close();
                    }
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
