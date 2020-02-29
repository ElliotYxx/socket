

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * @ProjectName: socket
 * @Package: PACKAGE_NAME
 * @ClassName: SocketClient
 * @Author: jerry
 * @Description: ${description}
 * @Date: 20-2-29 下午5:07
 * @Version: 1.0
 */
public class SocketClient extends Thread {
    //定义一个Socket对象
    Socket socket = null;

    byte[] reqid = new byte[35];// 返回的 reqid，下面的 3 个数据需要修改成自己的
    String cid = "1190707";
    String app_id = "B1R7gJTeiC87k2MU5TJk";
    String appkey = "25BEE6EDB&63AF512345852A8342D5A1";
    byte[] errMsg = new byte[50];
    byte[] dn = new byte[35];
    String reqID = "1190807C1917A093238363531363FABF";
    byte[] appeidcode = new byte[50];
    byte[] info = new byte[500];
    byte[] picture=new byte[100000];
    String ip = "118.145.9.208:80";

    public SocketClient(String host, int port) {
        try {
            //需要服务器的IP地址和端口号，才能获得正确的Socket对象
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        //客户端一连接就可以写数据给服务器了
        new sendMessThread().start();
        super.run();
        try {
            // 读Sock里面的数据
            InputStream s = socket.getInputStream();
            ObjectInputStream oos=new ObjectInputStream(s);
            /*byte[] buf = new byte[1024];
            int len = 0;
            while ((len = s.read(buf)) != -1) {
                System.out.println(getdate() + "  服务器说：  "+new String(buf, 0, len,"UTF-8"));
            }*/
            Info info=(Info) oos.readObject();
            System.out.println(info.getErrMsg());
            System.out.println(info.getDn());
            System.out.println(info.getAppeidcode());
            System.out.println(info.getInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //往Socket里面写数据，需要新开一个线程
    class sendMessThread extends Thread{
        @Override
        public void run() {
            super.run();
            //写操作
            /*Scanner scanner=null;*/
            OutputStream os= null;
            ObjectOutputStream oos=null;
            try {
               /* scanner=new Scanner(System.in);*/
                os= socket.getOutputStream();
                oos=new ObjectOutputStream(os);
                /*String in="";*/
                /*do {*/
                   /* in=scanner.next();*/
                    oos.writeObject(new Info(cid,app_id,appkey,reqID,dn.toString(),reqID,appeidcode.toString(),info.toString(),picture.toString(),ip));
                    oos.flush();
               /* } while (!in.equals("bye"));*/
            } catch (IOException e) {
                e.printStackTrace();
            }
           /* scanner.close();*/
           /* try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    public static String getdate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String result = format.format(date);
        return result;
    }

    //函数入口
    public static void main(String[] args) {
        //需要服务器的正确的IP地址和端口号
        SocketClient clientTest=new SocketClient("127.0.0.1", 1234);
        clientTest.start();
    }
}
