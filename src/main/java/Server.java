import com.sun.jna.Native;
import constant.Constants;
import entity.TerReq;
import service.LgetLib;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/1 下午6:51
 */
public class Server extends Thread{
    ServerSocket server = null;
    Socket socket = null;
    TerReq terReq = null;
    byte[] reqID = new byte[35];


    public Server(int port){
        try{
            server = new ServerSocket(port);
            server.setSoTimeout(10000);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        InputStream is = null;
        ObjectInputStream ios = null;

        try{
            System.out.println("等待客户端连接...");
            socket = server.accept();
            System.out.println("客户端（" + socket.getInetAddress().getHostAddress() + "） 连接成功...");
            is = socket.getInputStream();
            ios = new ObjectInputStream(is);

            terReq = (TerReq) ios.readObject();

            if (Constants.DECODE_CMD.equals(terReq.getTrans_code())){
                System.out.println("收到解码请求...");
                System.out.println("注册回调函数...");
                LgetLib INSTANCE = (LgetLib) Native.loadLibrary("eid", LgetLib.class);
                System.out.println("so库注册成功...");
                LgetLib.MyCallback readCallback = new LgetLib.MyCallback() {
                    public String readCard(String fid, String tidid, String resp) {
                        //发送读卡命令
                        //Thread readCardThread = new SendReadThread(resp);
                        //readCardThread.start();
                        System.out.println(resp);
                        return "2132135fGCV";
                    }
                };
                int result = INSTANCE.JLRCs("1235678",
                        "abacadae", "98541BDA41CA",
                        reqID, 0x3D, 2, readCallback, 3);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //发送结果信息给门锁系统
        //Thread sendResultThread = new SendResultThread(result);
    }

    class SendResultThread extends Thread{
        private int result;
        SendResultThread(int result){
            this.result = result;
        }
        @Override
        public void run() {
            super.run();
            OutputStream os = null;
            DataOutputStream dos = null;
            try{
                os = socket.getOutputStream();
                dos = new DataOutputStream(os);
                dos.writeInt(result);
                dos.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    class SendReadThread extends Thread{
        private String resp;
        SendReadThread(String resp){
            this.resp = resp;
        }

        @Override
        public void run() {
            super.run();
            OutputStream os = null;
            try{
                os = socket.getOutputStream();
                do{
                    os.write(resp.getBytes());
                    os.flush();
                }while (resp != null);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();
    }

}
