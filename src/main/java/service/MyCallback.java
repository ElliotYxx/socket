package service;

import javax.security.auth.callback.Callback;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/3/1 下午9:46
 */
public interface MyCallback extends Callback {
    /**
     * 读卡函数
     * @param fid
     * @param tidid
     * @param resp
     * @return
     */
    String readCard(String fid,String tidid, String resp);
}
