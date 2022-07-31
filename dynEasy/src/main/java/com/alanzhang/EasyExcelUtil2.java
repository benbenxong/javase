package com.alanzhang;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * EasyExcel 操作工具类(写,追加)
 * @author lzt
 * editUser:lizetao
 * editdate:2021/12/28
 *
 */
public class EasyExcelUtil2 {
    private ExcelWriter excelWriter = null;
    private WriteSheet writeSheet = null;

    private static String tableName;

    private static String queryFields;

    /** ===========================================================
     *
     *    Purpose      :   EasyExcel工具类 初始化(最先调用)
     *    @params:  absFilePath  绝对路径
     *    @params:  sheetName 标签页名字
     *    @params:  titleList 标题头(第一行)
     *    @return
     *    Author       :   lzt
     *    Created Date :   2021-12-28
     *    Update History
     *    Version       Date               Name            Description
     *    --------  ---------------   --------------  --------------------
     *     V1.0       2021-12-28           lzt            Creation
     * ===========================================================*/
    public void init(String absFilePath, String sheetName, List<List<String>> titleList){
        if(excelWriter == null) {
            List<List<String>> heads = new ArrayList<>();
            //heads.add(titleList);
            // 这里 需要指定写用哪个标题头去写 可以用class 也可以不用
            excelWriter = EasyExcelFactory.write(absFilePath).head(titleList)
                    .useDefaultStyle(false)
                    .build();
        }
            // 这里注意 如果同一个sheet只要创建一次
        writeSheet = EasyExcelFactory.writerSheet(sheetName).build();

    }
    /** ===========================================================
     *
     *    Purpose      :   EasyExcel工具类 写入excel写内容
     *    @params:  dataList  要插入的数据(多行插入)
     *    @return
     *    Author       :   lzt
     *    Created Date :   2021-12-28
     *    Update History
     *    Version       Date               Name            Description
     *    --------  ---------------   --------------  --------------------
     *     V1.0       2021-12-28           lzt            Creation
     * ===========================================================*/
    public void doExportExcel(List<List<String>> dataList){
        try{
            excelWriter.write(dataList,writeSheet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /** ===========================================================
     *
     *    Purpose      :   EasyExcel工具类 关闭(最后调用 关闭流)
     *    @return
     *    Author       :   lzt
     *    Created Date :   2021-12-28
     *    Update History
     *    Version       Date               Name            Description
     *    --------  ---------------   --------------  --------------------
     *     V1.0       2021-12-28           lzt            Creation
     * ===========================================================*/
    public void finish(){
        if(excelWriter!=null){
            excelWriter.finish();
        }
    }

    public static List<List<List<String>>> getData(String sql) {
        //List<String[]> checkTables = new ArrayList<>(10000);
        List<List<List<String>>> resultData = new ArrayList<>(2);
        List<List<String>> headList = new ArrayList<>();
        List<List<String>> dataList = new ArrayList<>();
        //String[] allArray = queryFields.split(",");
        //String sql = "SELECT " + queryFields + " FROM " + tableName;
        Connection con = DynamicEasyExcelExportUtils.Conn.getConnection();
        Assert.notNull(con, "con 连接对象不能为空");
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            String columnNames = rsmd.getColumnName(1);
            for (int i = 0; i < count; i++) {
                headList.add(Lists.newArrayList(rsmd.getColumnName(i + 1)));
            }
            resultData.add(headList);

            while (rs != null && rs.next()) {
                //String[] fieldValues = new String[allArray.length];
                //Map<String, Object> dataMap = Maps.newHashMap();
                List<String> row = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    Object value = rs.getObject(i + 1);
                    row.add(value == null ? null : value.toString());
                    //fieldValues[i] = value == null ? null : value.toString();
                }
                //checkTables.add(fieldValues);
                dataList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resultData.add(dataList);
        return resultData;
    }

    public static void main(String[] args) throws IOException {
//        String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai";
//        String Driver = "com.mysql.cj.jdbc.Driver";
//        String Userid = "root";
//        String Password = "admin";
        String url = null;
        String Driver = null;
        String Userid = null;
        String Password = null;

        if(url != null){
            DynamicEasyExcelExportUtils.Conn.setUrl(url);
            DynamicEasyExcelExportUtils.Conn.setDriver(Driver);
            DynamicEasyExcelExportUtils.Conn.setUserid(Userid);
            DynamicEasyExcelExportUtils.Conn.setPassword(Password);
        }

        String[] sqls = ReadSqlFile.getSQLs("C:\\javapro\\dynEasy\\mysql.sql");
        String[] sheets = "标题1".split(",");

        if(sqls.length != sheets.length){
            System.out.println("SQL数" + sqls.length + "与SHEET数" + sheets.length + " 不符！");
            System.exit(1);
        }

        EasyExcelUtil2 easyExcelUtil = new EasyExcelUtil2();
        //使用原文件名在java的临时文件夹中创建临时文件
        File file = new File("C:\\javapro\\dynEasy\\" + "easyexcel-export-user53.xlsx");
        //若临时文件夹中已经存在该文件，则先删除
        if (file.exists()){
            file.delete();
        }
        String absFilePath = file.getAbsolutePath();
        for (int i = 0; i < sqls.length; i++) {
            List<List<List<String>>> resultData = getData(sqls[i]);
            System.out.println(sheets[i]);
            easyExcelUtil.init(absFilePath,sheets[i],resultData.get(0));
            easyExcelUtil.doExportExcel(resultData.get(1));
        }
        //关闭流
        easyExcelUtil.finish();
    }
}
