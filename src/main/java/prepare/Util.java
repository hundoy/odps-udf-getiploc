package prepare;

import com.aliyun.odps.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wgy on 2018/1/17.
 */
public class Util {
    public static Long ipToLong(String ipstr){
        if (StringUtils.isNullOrEmpty(ipstr) || !isMatch("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$", ipstr)) return 0L;

        String[] iparr = ipstr.split("\\.");
        return Long.parseLong(iparr[0])*(long)Math.pow(2, 24) + Long.parseLong(iparr[1])*(long)Math.pow(2, 16)
                + Long.parseLong(iparr[2])*(long)Math.pow(2, 8) + Long.parseLong(iparr[3]);
    }

    public static boolean isMatch(String regexp, String str){
        Pattern pat = Pattern.compile(regexp);
        Matcher mat = pat.matcher(str);

        return mat.find();
    }

    public static String str(String oriStr){
        if (oriStr==null) return "";
        return oriStr.trim();
    }

    /**
     * 二分查找，从list中找出小于tar的最小的一个值
     * @return
     */
    public static Long binarySearch(List<Long> list, Long tar){
        int len = list.size();
        int si = 0;
        int ti = list.size()-1;
        while(si<=ti){
            int mid = (ti+si)/2;
            Long val = list.get(mid);
            if (val==tar){
//                System.out.println("val=tar "+si+"-"+ti);
                return val;
            } else if (val>tar){
//                System.out.println("val>tar "+si+"-"+ti);
                ti = mid-1;
            } else{
//                System.out.println("val<=tar "+si+"-"+ti);
                si = mid+1;
            }
        }
//        System.out.println("si:"+si+" ti:"+ti);
        return list.get(Math.min(si, ti));
    }

    public static void main(String[] args){
        List<Long> list = Arrays.asList(new Long[]{20L, 40L, 60L, 80L, 100L, 120L, 140L});
        System.out.println(binarySearch(list, 73L));
        System.out.println(binarySearch(list, 28L));
        System.out.println(binarySearch(list, 92L));
        System.out.println(binarySearch(list, 121L));
        System.out.println(binarySearch(list, 113L));
    }
}
