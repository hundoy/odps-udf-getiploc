# ODPS(Maxcompute) UDF of Get Ip Location

## 简述
阿里云Maxcompute的UDF，可以用来快速离线查询一个IP地址的地市信息。

## 原理
利用代码自动生成hardcode代码，将地市信息写在代码中。
查找方式使用btree和二分查找

## 其他解决方案
- Maxcompute的UDF不能使用httpClient联网，所以无法在UDF中在线从第三方接口获取地市信息。
- Maxcompute的UDF不支持读取静态资源。跟联网问题一样，原因是由于其沙箱机制。（已跟阿里云的技术人员确认求证）
- 如果不使用UDF，可以将IP地市信息存入数据库，利用map join查询。但需要对IP地市库进行大幅的删减，不然会因为过大无法加载到内存。并且查询速度极慢。

## 使用
1. 用新的IP地市信息文件替换resources下的ip.merge.txt
2. 把ipdata包下的文件除了BaseIpData.java和IpDataIndex.java以外的类全部删除。
3. 执行 prepare.ReadAndCreateUdf 类，批量生成IpDataXX类。此类中的BLOCK_SIZE和outputPath两个值可以根据实际情况调整。
4. 将生成的IpDataXXX和IpDataIndex类复制到ipdata包下，替换原来的文件。
5. 将整个getiploc-udf打包成jar，上传到maxcompute，即可使用。

## 参考内容
此项目灵感来自于 https://github.com/lionsoul2014/ip2region
在此致谢！

