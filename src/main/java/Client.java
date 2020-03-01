import constant.Constants;
import entity.TerReq;
import service.LgetLib;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/1 下午6:51
 */
public class Client extends Thread{

    Socket socket = null;
    byte[] reqID = new byte[35];

    public Client(String host, int port){
        try{
            socket = new Socket(host, port);
        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        new SendDecodeReqThread().start();
        super.run();
        try{
            //接受开锁服务端的读卡命令
            InputStream in = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = in.read(buf);
            String resp = new String(buf, 0, len);
            System.out.println("接受到的resp： " + resp);

//            int num = Server.INSTANCE.JLRCs("1235678", "abacadae", "98541BDA41CA",
//                    reqID, 0x3D, 2, readCard, 3);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    class SendDecodeReqThread extends Thread{
        @Override
        public void run() {
            super.run();
            OutputStream os = null;
            ObjectOutputStream oos = null;
            try{
                os = socket.getOutputStream();
                oos = new ObjectOutputStream(os);
                oos.writeObject(new TerReq(Constants.DECODE_CMD, "Q500000001", null, "0", null));
                oos.writeObject(null);
                oos.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 1234);
        client.start();
    }
}
