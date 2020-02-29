import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

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
    Info info=null;

    public SocketServer(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface JLRC extends Library {
        int getInfo(byte[]cid ,byte[]app_id, byte[]appkey, byte[] reqID, byte[] biz_sequence_id,
                    byte[] info,byte[] picture, byte[] dn, byte[] appeidcode, byte[] errMsg,int decodeLevel ,byte[] ip)
                ;
    }

    @Override
    public void run() {

        super.run();
        try {
            System.out.println(getdate() + "  等待客户端连接...");
            socket = server.accept();
           /* new sendMessThread().start();// 连接并返回socket后，再启用发送消息线程*/
            System.out.println(getdate() + "  客户端 （" + socket.getInetAddress().getHostAddress() + "） 连接成功...");
            InputStream in = socket.getInputStream();
            ObjectInputStream ois=new ObjectInputStream(in);
            /*int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                System.out.println(getdate() + "  客户端: （"
                        + socket.getInetAddress().getHostAddress() + "）说："
                        + new String(buf, 0, len, "UTF-8"));
            }*/
            /*System.out.println("客户端对象"+((Info)ois.readObject()).getApp_id());*/
             info=(Info)ois.readObject();
            JLRC lib = (JLRC) Native.loadLibrary("query_V2.1.1", JLRC.class);
// 由动态库执行 CallbackAdd 回调函数
            int ret = lib. getInfo(info.getCid().getBytes(),info.getApp_id().getBytes(), info.getAppkey().getBytes(),info.getReqID().getBytes(),"jWEeQkfogZSJvrS2iDZ".getBytes(),info.getInfo().getBytes(),info.getPicture().getBytes(),info.getDn().getBytes(),info.getAppeidcode().getBytes(), info.getErrMsg().getBytes(),2,info.getIp().getBytes());
            System.out.println("返回的结果: "+ret);

           /* OutputStream os=socket.getOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(new Info(info.getErrMsg(),info.getDn(),info.getAppeidcode(),info.getInfo()));*/
            new sendMessThread().start();// 连接并返回socket后，再启用发送消息线程
        } catch (Exception e) {
            e.printStackTrace();
        }
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
           /* Scanner scanner = null;*/
            OutputStream out = null;
            ObjectOutputStream oos=null;
            try {
                if (socket != null) {
                   /* scanner = new Scanner(System.in);*/
                    out = socket.getOutputStream();
                    oos=new ObjectOutputStream(out);
                    oos.writeObject(new Info(info.getErrMsg(),info.getDn(),info.getAppeidcode(),info.getInfo()));
                   /* String in = "";*/
                   /* do {
                        in = scanner.next();
                        out.write(("" + in).getBytes("UTF-8"));
                        out.flush();// 清空缓存区的内容
                    } while (!in.equals("q"));
                    scanner.close();*/
                    /*try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    // 函数入口
    public static void main(String[] args) {
        SocketServer server = new SocketServer(1234);
        server.start();
    }
}
