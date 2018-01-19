package udf;

import com.aliyun.odps.udf.UDF;
import ipdata.BaseIpData;
import ipdata.IpDataIndex;
import prepare.IpInfo;
import prepare.Util;

import java.util.List;

/**
 * Created by wgy on 2018/1/18.
 */
public final class GetIpLoc extends UDF {
    public String evaluate(String ipstr) {
        try {
            Long ipLong = Util.ipToLong(ipstr);
            if (ipLong==null) return ",,";

            // 二分查找索引
            IpDataIndex ipDataIndex = new IpDataIndex();
            List<Long> list = ipDataIndex.startIpList;
            Long indexIp = Util.binarySearch(list, ipLong);
            Class clz = ipDataIndex.ipDataMap.get(indexIp);
            BaseIpData ipData = (BaseIpData) clz.newInstance();
            List<Long> infoList = ipData.startIpList;
            Long startIp = Util.binarySearch(infoList, ipLong);
            IpInfo info = ipData.ipInfoMap.get(startIp);
            if (info!=null){
                String province = Util.str(info.getProvince());
                String city = Util.str(info.getCity());
                if (province.equals("0")) province = "";
                if (city.equals("0")) city = "";
                return province+","+city+",";
            }
        } catch (Exception e) {
            System.err.println("error val is: "+ipstr);
            e.printStackTrace();
        }

        return ",,";
    }

    public static void main(String[] args){
        GetIpLoc obj = new GetIpLoc();
        System.out.println(obj.evaluate("1.1.8.20"));
    }
}
