package socket;

import Database.DBapplication;
import Database.database;
import withAndroidCamera.withCamera;

public class Connect_Application extends android.app.Application {
    private static final connectToComputer connectToComputer=new connectToComputer();
    private static final database db=new database("192.168.43.150:80");//手机热点IP地址
   private static final withCamera wCamera = new withCamera(6666);
   public withCamera getCamera(){
        return wCamera;
    }
    public database getDB(){
        return db;
    }
    //    private static final DBBean db=new DBBean();
    public connectToComputer getConnectToComputer(){
        return connectToComputer;
    }
    public DBBean getDBBean(){
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}