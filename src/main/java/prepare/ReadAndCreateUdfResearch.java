package prepare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * Created by wgy on 2018/1/17.
 * 读取文件并构建udf类，调研用。不能缓存和使用静态资源的情况下，反射的效率远远高于读文件，所以使用反射的方案。
 * 10000次循环的情况下
 * io cost(ms): 3112.857443
 * reflect cost(ms): 123.307518
 */
public class ReadAndCreateUdfResearch {
    public static void main(String[] args){
        // test io and reflect
        double st = System.nanoTime();

        for (int i=0; i<10000; i++){
            iotest();
        }

        double et = System.nanoTime();
        System.out.println("io cost(ms): "+((et-st)/1000000.0));

        st = System.nanoTime();

        for (int i=0; i<10000; i++){
            try {
                Class clz = Class.forName("prepare.ReflectTest");
                ReflectTest rt = (ReflectTest) clz.newInstance();
                Method mth = clz.getDeclaredMethod("oh",null);
                mth.invoke(rt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        et = System.nanoTime();
        System.out.println("reflect cost(ms): "+((et-st)/1000000.0));

    }

    private static void iotest(){
        try {
            InputStream is = ReadAndCreateUdfResearch.class.getResourceAsStream("../test.txt");
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            String line = br.readLine();
            while (line!=null){
                line = br.readLine();
            }
            br.close();
            ir.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



