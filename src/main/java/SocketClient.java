

import com.sun.jna.Native;
import service.LgetLib;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
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

    byte[] reqID = new byte[35];// 返回的 reqid，下面的 3 个数据需要修改成自己的
    String cid = "1190707";
    String app_id = "B1R7gJTeiC87k2MU5TJk";
    String appkey = "25BEE6EDB&63AF512345852A8342D5A1";
    byte[] errMsg = new byte[50];
    byte[] dn = new byte[35];
    /*String reqID = "1190807C1917A093238363531363FABF";*/
    byte[] appeidcode = new byte[50];
    byte[] info = new byte[500];
    byte[] picture = new byte[100000];
    String ip = "118.145.9.208:80";

    public static char[] getChars(byte[] bytes)
    {
        Charset cs = Charset.forName("utf8");
        ByteBuffer bb=ByteBuffer.allocate(bytes.length);
        bb.put(bytes).flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    public SocketClient(String host, int port) {
        try {
            //需要服务器的IP地址和端口号，才能获得正确的Socket对象
            socket = new Socket(host, port);
            socket.setSoTimeout(5*1000);
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
        InputStream is = null;
        ObjectInputStream ois = null;
        try {
            // 读Sock里面的数据
             is = socket.getInputStream();
             ois = new ObjectInputStream(is);

            Info info = (Info) ois.readObject();
            System.out.println(info.getErrMsg());
            System.out.println(info.getDn());
            System.out.println(info.getAppeidcode());
            System.out.println(info.getInfo());
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

    //往Socket里面写数据，需要新开一个线程
    class sendMessThread extends Thread{
        @Override
        public void run() {
            super.run();

            LgetLib INSTANCE = (LgetLib) Native.loadLibrary("eid_V4.0.0", LgetLib.class);
            LgetLib.MyCallback mycall = new LgetLib.MyCallback() {

                public String readCard(String fid,String tidid, String resp) {
                    /*System.out.println("resp:"+resp);*/
                    if("80B0000020".equals(resp)){
                        return "00014845010807100000000000067777EED1E76E59EB123456420F9A520B8C269000";
                    }
                    return "9000";
                }
            };
            int num = INSTANCE.JLRCs("1235678",
                    "abacadae", "98541BDA41CA",
                    reqID, 0x3D, 2, mycall, 3);
            //写操作
           System.out.println(num);
           System.out.println(reqID);
            for(int i=0;i<reqID.length;i++){
                System.out.print((char)reqID[i]);
            }
            char[] test = getChars(reqID);
            OutputStream os = null;
            ObjectOutputStream oos = null;
            try {
                os = socket.getOutputStream();
                oos = new ObjectOutputStream(os);
                    oos.writeObject(new Info(cid,app_id,appkey,errMsg.toString(),dn.toString(),test.toString(),appeidcode.toString(),info.toString(),picture.toString(),ip));
                    oos.writeObject(null);
                    oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
           /* try {
                os.close();
                oos.close();
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
