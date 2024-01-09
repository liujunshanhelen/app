package cloud;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class withCloud {
    private String host = "192.168.43.29";//云服务器地址
    private int port = 4444;
    private Socket withCloudSocket = null;
    private Executor executor = Executors.newCachedThreadPool();

    public withCloud(int port) {
        try {
            this.port = port;
            System.out.println("发起连接请求");
            this.withCloudSocket = new Socket(host, this.port);
        }
        catch(IOException e){e.printStackTrace();}

    }
    public void submit(Runnable x){
        this.executor.execute(x);
    }
    public Socket getSocket(){
        return this.withCloudSocket;
    }
}
