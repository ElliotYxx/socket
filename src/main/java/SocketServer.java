import com.sun.jna.Library;
import com.sun.jna.Native;
import service.JLRC;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ProjectName: socket
 * @Package: PACKAGE_NAME
 * @ClassName: SocketServer
 * @Author: jerry
 * @Description: ${description}
 * @Date: 20-2-29 下午5:07
 * @Version: 1.0
 */
public class SocketServer extends Thread{
    ServerSocket server = null;
    Socket socket = null;
    Info info = null;

    public SocketServer(int port) {
        try {
            server = new ServerSocket(port);
            server.setSoTimeout(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {

        super.run();
        InputStream is = null;
        ObjectInputStream ois = null;
        try {
            System.out.println(getdate() + "  等待客户端连接...");
            socket = server.accept();
            System.out.println(getdate() + "  客户端 （" + socket.getInetAddress().getHostAddress() + "） 连接成功...");
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);

             info = (Info)ois.readObject();
             System.out.println(info.getReqID());
            JLRC lib = (JLRC) Native.loadLibrary("query_V2.1.1", JLRC.class);
// 由动态库执行 CallbackAdd 回调函数
            int ret = lib. getInfo(info.getCid().getBytes(),info.getApp_id().getBytes(), info.getAppkey().getBytes(),"1190807C1917A093238363531363FABF".getBytes(),"jWEeQkfogZSJvrS2iDZ".getBytes(),info.getInfo().getBytes(),info.getPicture().getBytes(),info.getDn().getBytes(),info.getAppeidcode().getBytes(), info.getErrMsg().getBytes(),2,info.getIp().getBytes());
            System.out.println("返回的结果: "+ret);

            new sendMessThread().start();// 连接并返回socket后，再启用发送消息线程
        } catch (Exception e) {
            e.printStackTrace();
        }
      /*  try {
            is.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public static String getdate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String result = format.format(date);
        return result;
    }

    class sendMessThread extends Thread {
        @Override
        public void run() {
            super.run();
            OutputStream os = null;
            ObjectOutputStream oos = null;
            try {
                if (socket != null) {
                    os = socket.getOutputStream();
                    oos = new ObjectOutputStream(os);
                    oos.writeObject(new Info(info.getErrMsg(),info.getDn(),info.getAppeidcode(),info.getInfo()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
          /*  try {
                os.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }

    }

    // 函数入口
    public static void main(String[] args) {
        SocketServer server = new SocketServer(1234);
        server.start();
    }
}
