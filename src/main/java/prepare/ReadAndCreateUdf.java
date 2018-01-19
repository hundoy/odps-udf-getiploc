package prepare;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wgy on 2018/1/17.
 */
public class ReadAndCreateUdf {
    private final static int BLOCK_SIZE = 1000;
    private final static String ipDataPutMapTemp = "        ipInfoMap.put(${startIp}L, new IpInfo(${startIp}L,${endIp}L,\"${province}\",\"${city}\"));";
    private final static String ipDataAddListTemp = "        startIpList.add(${startIp}L);";
    private final static String ipDataIndexPutMapTemp = "        ipDataMap.put(${indexIp}L, IpData${num}.class);";
    private final static String ipDataIndexAddListTemp = "        startIpList.add(${indexIp}L);";

    private final static String outputPath = "getiploc/src/main/java/ipdata/";

    public static void main(String[] args){
        // 读取IP地市文件
        List<IpInfo> ipInfos = readIpFile("../ip.merge.txt");

        // 文件模板读取
        String ipDataTemplate = readFileStr("../IpData0.template");
        String ipDataIndexTemplate = readFileStr("../IpDataIndex.template");

        // 分区并处理
        process(ipInfos, ipDataTemplate, ipDataIndexTemplate);

        System.out.println("prepare end!");
    }

    private static void process(List<IpInfo> ipInfos, String ipDataTemplate, String ipDataIndexTemplate) {
        StringBuffer ipDataPutMap = new StringBuffer();
        StringBuffer ipDataAddList = new StringBuffer();
        StringBuffer ipDataIndexPutMap = new StringBuffer();
        StringBuffer ipDataIndexAddList = new StringBuffer();
        String indexIp = "";
        for (int i=0; i<ipInfos.size(); i++){
            IpInfo info = ipInfos.get(i);
            // 分区第一个元素，记录其startIp作为本分区的indexIp
            if ((i+1)%BLOCK_SIZE==1){
                indexIp = String.valueOf(info.getStartIp());
            }

            // 生成一行IpData中的内容
            String ipDataPutMapLine = ipDataPutMapTemp.replace("${startIp}", String.valueOf(info.getStartIp()))
                    .replace("${endIp}", String.valueOf(info.getEndIp()))
                    .replace("${province}", info.getProvince())
                    .replace("${city}", info.getCity());
            String ipDataAddListLine = ipDataAddListTemp.replace("${startIp}", String.valueOf(info.getStartIp()));
            ipDataPutMap.append(ipDataPutMapLine).append("\n");
            ipDataAddList.append(ipDataAddListLine).append("\n");

            // 分区最后一个元素，完成一个分区，保存内容
            if ((i+1)%BLOCK_SIZE==0){
                String num = String.valueOf(i/BLOCK_SIZE);
                // 生成一个IpData类文件内容并保存
                String outputIpData = ipDataTemplate.replace("${put_map}", ipDataPutMap)
                        .replace("${add_list}", ipDataAddList)
                        .replace("${num}", num);
                writeFileStr(outputPath+"IpData"+num+".java", outputIpData);
                System.out.println(outputPath+"IpData"+num+".java");

                // 生成一行IpDataIndex的内容
                String ipDataIndexPutMapLine = ipDataIndexPutMapTemp.replace("${indexIp}", indexIp)
                        .replace("${num}", num);
                String ipDataIndexAddListLine = ipDataIndexAddListTemp.replace("${indexIp}", indexIp);
                ipDataIndexPutMap.append(ipDataIndexPutMapLine).append("\n");
                ipDataIndexAddList.append(ipDataIndexAddListLine).append("\n");

                ipDataPutMap = new StringBuffer();
                ipDataAddList = new StringBuffer();
            }

        }
        // 最后的处理，1、未保存成文件的分区内容，保存成一个文件
        if (ipDataPutMap.length()>0){
            // 生成一个IpData类文件内容并保存
            String num = String.valueOf(ipInfos.size()/BLOCK_SIZE);
            String outputIpData = ipDataTemplate.replace("${put_map}", ipDataPutMap)
                    .replace("${add_list}", ipDataAddList)
                    .replace("${num}", num);
            writeFileStr(outputPath+"IpData"+num+".java", outputIpData);
            System.out.println(outputPath+"IpData"+num+".java");

            // 生成一行IpDataIndex的内容
            String ipDataIndexPutMapLine = ipDataIndexPutMapTemp.replace("${indexIp}", indexIp)
                    .replace("${num}", num);
            String ipDataIndexAddListLine = ipDataIndexAddListTemp.replace("${indexIp}", indexIp);
            ipDataIndexPutMap.append(ipDataIndexPutMapLine).append("\n");
            ipDataIndexAddList.append(ipDataIndexAddListLine).append("\n");
        }

        // 最后的处理，1、保存IpDataIndex文件
        String outputIpDataIndex = ipDataIndexTemplate.replace("${put_map}", ipDataIndexPutMap)
                .replace("${add_list}", ipDataIndexAddList);
        writeFileStr(outputPath+"IpDataIndex.java", outputIpDataIndex);
        System.out.println(outputPath+"IpDataIndex.java");
    }

    private static void writeFileStr(String fname, String str){
        try{
            File file = new File(fname);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            writer.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static String readFileStr(String fname){
        StringBuffer sb = new StringBuffer();
        try{
            InputStream is = ReadAndCreateUdfResearch.class.getResourceAsStream(fname);
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            String line = br.readLine();
            while (line!=null){
                sb.append(line+"\n");
                line = br.readLine();
            }
            br.close();
            ir.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static List<IpInfo> readIpFile(String fname){
        List<IpInfo> ipInfos = new ArrayList<IpInfo>();
        try {
            InputStream is = ReadAndCreateUdfResearch.class.getResourceAsStream(fname);
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            String line = br.readLine();
            while (line!=null){
                IpInfo ipInfo = new IpInfo();
                // 分割7份
                String[] infoArr = line.split("\\|");
                ipInfo.setStartIp(Util.ipToLong(infoArr[0]));
                ipInfo.setEndIp(Util.ipToLong(infoArr[1]));
                ipInfo.setProvince(infoArr[4]);
                ipInfo.setCity(infoArr[5]);
                ipInfos.add(ipInfo);

                line = br.readLine();
            }
            br.close();
            ir.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipInfos;
    }
}
