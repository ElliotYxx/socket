import constant.Constants;
import entity.TerReq;

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
        TerReq decodeReq = new TerReq();
        decodeReq.setTrans_code("01");
        decodeReq.setDevsn("Q500000001");
        Thread sendDecodeReq = new SendTerReqThread(decodeReq);
        sendDecodeReq.start();
        super.run();
        try{
            //接受开锁服务端的读卡命令
            InputStream in = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = in.read(buf);
            String resp = new String(buf, 0, len);
            System.out.println("接受到的resp： " + resp);
            TerReq cardResult = new TerReq();
            //设置返回结果
            cardResult.setTrans_code("03");
            if ("80B0000020".equals(resp)){
                cardResult.setComm_data("00014845010807100000000000067777EED1E76E59EB123456420F9A520B8C269000");
                cardResult.setErrcode("0");
            }else{
                cardResult.setComm_data("9000");
                cardResult.setErrcode("-43002");
            }
            Thread sendResultThread = new SendTerReqThread(cardResult);
            sendResultThread.start();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    class SendTerReqThread extends Thread{
        private TerReq terReq;
        SendTerReqThread(TerReq terReq){
            this.terReq = terReq;
        }
        @Override
        public void run() {
            super.run();
            OutputStream os = null;
            ObjectOutputStream oos = null;
            try{
                os = socket.getOutputStream();
                oos = new ObjectOutputStream(os);
                oos.writeObject(terReq);
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
