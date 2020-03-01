package service;

import com.sun.jna.Callback;
import com.sun.jna.Library;

/**
 * @ProjectName: socket
 * @Package: service
 * @ClassName: LgetLib
 * @Author: jerry
 * @Description: ${description}
 * @Date: 20-3-1 下午2:00
 * @Version: 1.0
 */
public interface LgetLib extends Library {
    /**
     * Linux导入格式，例如包名为 libeid.so导入时候不需要加后缀名及前面lib名称,系统会自动加入
     * @param cid
     * @param fdid
     * @param tdid
     * @param reqid
     * @param len
     * @param declevel
     * @param treadCard
     * @param loglvel
     * @return
     */
    int JLRCs(String cid, String fdid, String tdid, byte[] reqid, int len,
              int declevel, MyCallback treadCard, int loglvel);
}
