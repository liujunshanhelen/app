package Database;

import android.app.Application;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class database extends Application {
    private JSONArray response;
    private String host;

    public database(String host){
        this.host="http://"+host+"/";
    }
    /*登录*/
    public JSONArray executeLogin(final String index,final String value) {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                response= executeHttpFind(host+"executeFind.php",null,index,value,"login");
            }
        });
        thread.start();
        try {
            thread.join();//等待当前线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    /*查询权限*/
    public JSONArray executeLimit(final String index) {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                response= executeHttpFind(host+"executeFind.php",null,index,null,"power");
            }
        });
        thread.start();
        try {
            thread.join();//等待当前线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    /*扇形图*/
    public JSONArray executeShan(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                response= executeHttpFind(host+"shan.php","record_all",null,null,"search_shan");
            }
        });
        thread.start();
        try {
            thread.join();//等待当前线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    /*查询数据库中数据*/
    public JSONArray executeFind(final String value) {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                response= executeHttpFind(host+"executeFindPerson.php","record_all",null,value,"search_name");
            }
        });
        thread.start();
        try {
            thread.join();//等待当前线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    //查找数据库中某一个表中具有最大id的一行
    public JSONArray executeFindMAXID() {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                response= executeHttpFind(host+"findMAX_id.php",null,null,null,"get_max");
            }
        });
        thread.start();
        try {
            thread.join();//等待当前线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**查询数据库某表中全部数据
     */
    public JSONArray executeFindAll(final String tableName) {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                response= executeHttpFind(host+"executeFindAll.php",tableName,null,null,"search_all");
            }
        });
        thread.start();
        try {
            thread.join();//等待当前线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 向数据库中插入一条数据
     */
    public JSONArray executeInsert(final String value){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                response= executeHttpFind(host+"executeInsert.php",null,null,value,"sign_up");
            }
        });
        thread.start();
        try {
            thread.join();//等待当前线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private JSONArray executeHttpFind(String path,String tableName,String index,String value,String type) {
        HttpURLConnection con;
        InputStream in;
        OutputStream out;
        try {
            con= (HttpURLConnection) new URL(path).openConnection();
            con.setConnectTimeout(8000);
            con.setReadTimeout(8000);
            con.setDoOutput(false);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection","close");
            out = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            String data=Construct_SQL_find_parameters(tableName,index,value,type);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            out.close();
            con.connect();
            int responseCode=con.getResponseCode();
            if(responseCode==200){
                in=con.getInputStream();
                String resultString=parseInfo(in);
                if(resultString.equals("false")){
                    return null;
                }
                JSONObject result=new JSONObject(resultString);
                JSONArray resultArray=new JSONArray(result.getString(type));
                con.disconnect();
                return resultArray;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将返回的语言转换为字符串
     * @param in
     * @return
     * @throws IOException
     */
    private String parseInfo(InputStream in) throws IOException {
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        StringBuilder sb=new StringBuilder();
        String line;
        while ((line=br.readLine())!=null){
            sb.append(line+"\n");
        }
        return sb.toString();
    }

    /**
     *
     * @param value      想要查询的值
     * @param tableName     想要查询的数据库的表名
     * @param index      想要查询数据库的表的哪一列的名字
     * @return
     */
    private String Construct_SQL_find_parameters(String tableName,String index,String value,String type){
        String data = null;
        try {
            if (tableName!=null){
                data = URLEncoder.encode("tableName", "UTF-8") + "=" + URLEncoder.encode(tableName, "UTF-8");
            }
            if (index!=null){
                String tmp[]=index.split("&");
                if (tmp.length>1){
                    data=data+"&" + URLEncoder.encode("index", "UTF-8") + "=" + URLEncoder.encode(tmp[0],
                            "UTF-8")+"&"+tmp[1]+"&"+tmp[2]+"&"+tmp[3];
                }else {
                    data=data+"&" + URLEncoder.encode("index", "UTF-8") + "=" + URLEncoder.encode(index, "UTF-8");
                }
            }
            if (value!=null){
                data=data+"&" + URLEncoder.encode("value", "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            }
            if (type!=null){
                data=data+"&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }
}
