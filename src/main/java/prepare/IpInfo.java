package prepare;

/**
 * Created by wgy on 2018/1/17.
 */
public class IpInfo {
    private long startIp;
    private long endIp;
    private String province;
    private String city;

    public IpInfo(){

    }

    public IpInfo(long startIp, long endIp, String province, String city) {
        this.startIp = startIp;
        this.endIp = endIp;
        this.province = province;
        this.city = city;
    }

    public long getStartIp() {
        return startIp;
    }

    public void setStartIp(long startIp) {
        this.startIp = startIp;
    }

    public long getEndIp() {
        return endIp;
    }

    public void setEndIp(long endIp) {
        this.endIp = endIp;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
