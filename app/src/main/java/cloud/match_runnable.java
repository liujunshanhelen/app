package cloud;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

public class match_runnable implements Runnable{
    private Socket socket = null;
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;
    private String command;
    private String match_result = null;
    public match_runnable(Socket socket) {
        try {
            this.socket = socket;
            this.command = "APP_match";
            this.dataOutput = new DataOutputStream(this.socket.getOutputStream());
            this.dataInput = new DataInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        //FIXME 发的时候就只发送命令
        System.out.println("开始发送数据...");
        send_message(this.command,this.dataOutput);
        try {
            while (true) {
                int size = 0;
                size = this.dataInput.readInt();
                if (size <= 0) continue;
                byte[] data = new byte[size];
                int len = 0;
                //将二进制数据写入dac sta数组
                while (len < size) {
                    len += dataInput.read(data, len, size - len);
                }
                String temp = parse_command(data);
                if (temp.equals("continue")) continue;
                this.match_result = temp;
                System.out.println("成功接收数据");
                break;
            }
        }catch (Exception e){e.printStackTrace();}
    }
    public String parse_command(byte[] data){
        return new String(data);
    }
    public void send_message(String message, DataOutputStream dataOutput){
        try {
            byte[] tmp = message.getBytes();
            dataOutput.writeInt(tmp.length);
            dataOutput.write(tmp, 0, tmp.length);
            dataOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_message(String message){
        try {
            byte[] tmp = message.getBytes();
            dataOutput.writeInt(tmp.length);
            dataOutput.write(tmp, 0, tmp.length);
            dataOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getResult(){
        return this.match_result;
    }
}
