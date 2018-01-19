package ipdata;

import prepare.IpInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wgy on 2018/1/17.
 * IpData基类
 */
public class BaseIpData {
    public long startIp;
    public List<Long> startIpList = new ArrayList<Long>();
    public Map<Long, IpInfo> ipInfoMap = new HashMap<Long, IpInfo>();

    public IpInfo getIpInfoByStartIp(Long startIp){
        return ipInfoMap.get(startIp);
    }
}
