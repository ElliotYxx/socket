import java.io.Serializable;

/**
 * @ProjectName: socket
 * @Package: PACKAGE_NAME
 * @ClassName: Info
 * @Author: jerry
 * @Description: ${description}
 * @Date: 20-2-29 下午5:30
 * @Version: 1.0
 */
public class Info implements Serializable {
    private static final long serialVersionUID=1L;
    private String cid;
    private String app_id;
    private String appkey;
    private String errMsg;
    private String dn;
    private String reqID;
    private String appeidcode;
    private String info;
    private String picture;
    private String ip;

    public Info(String cid, String app_id, String appkey, String errMsg, String dn, String reqID, String appeidcode, String info, String picture, String ip) {
        this.cid = cid;
        this.app_id = app_id;
        this.appkey = appkey;
        this.errMsg = errMsg;
        this.dn = dn;
        this.reqID = reqID;
        this.appeidcode = appeidcode;
        this.info = info;
        this.picture = picture;
        this.ip = ip;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getAppeidcode() {
        return appeidcode;
    }

    public void setAppeidcode(String appeidcode) {
        this.appeidcode = appeidcode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Info(String errMsg,String dn,String appeidcode,String info)
    {
        this.errMsg=errMsg;
        this.dn=dn;
        this.appeidcode=appeidcode;
        this.info=info;
    }
}
