package com.alanzhang;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DynamicEasyExcelExportUtils {

    private static final Logger log = LoggerFactory.getLogger(DynamicEasyExcelExportUtils.class);

    private static final String DEFAULT_SHEET_NAME = "sheet1";

    private static String tableName;

    private static String queryFields;


    /**
     * 动态生成导出模版(单表头)
     * @param headColumns 列名称
     * @return            excel文件流
     */
    public static byte[] exportTemplateExcelFile(List<String> headColumns){
        List<List<String>> excelHead = Lists.newArrayList();
        headColumns.forEach(columnName -> { excelHead.add(Lists.newArrayList(columnName)); });
        byte[] stream = createExcelFile(excelHead, new ArrayList<>());
        return stream;
    }

    /**
     * 动态生成模版(复杂表头)
     * @param excelHead   列名称
     * @return
     */
    public static byte[] exportTemplateExcelFileCustomHead(List<List<String>> excelHead){
        byte[] stream = createExcelFile(excelHead, new ArrayList<>());
        return stream;
    }

    /**
     * 动态导出文件
     * @param headColumnMap  有序列头部
     * @param dataList       数据体
     * @return
     */
    public static byte[] exportExcelFile(LinkedHashMap<String, String> headColumnMap, List<Map<String, Object>> dataList){
        //获取列名称
        List<List<String>> excelHead = new ArrayList<>();
        if(MapUtils.isNotEmpty(headColumnMap)){
            //key为匹配符，value为列名，如果多级列名用逗号隔开
            headColumnMap.entrySet().forEach(entry -> {
                excelHead.add(Lists.newArrayList(entry.getValue().split(",")));
            });
        }
        List<List<Object>> excelRows = new ArrayList<>();
        if(MapUtils.isNotEmpty(headColumnMap) && CollectionUtils.isNotEmpty(dataList)){
            for (Map<String, Object> dataMap : dataList) {
                List<Object> rows = new ArrayList<>();
                headColumnMap.entrySet().forEach(headColumnEntry -> {
                    if(dataMap.containsKey(headColumnEntry.getKey())){
                        Object data = dataMap.get(headColumnEntry.getKey());
                        rows.add(data);
                    }
                });
                excelRows.add(rows);
            }
        }
        byte[] stream = createExcelFile(excelHead, excelRows);
        return stream;
    }

    /**
     * 生成文件
     * @param excelHead
     * @param excelRows
     * @return
     */
    private static byte[] createExcelFile(List<List<String>> excelHead, List<List<Object>> excelRows){
        try {
            if(CollectionUtils.isNotEmpty(excelHead)){
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                EasyExcel.write(outputStream).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                        .head(excelHead)
                        .sheet(DEFAULT_SHEET_NAME)
                        .doWrite(excelRows);
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
           log.error("动态生成excel文件失败，headColumns：" + JSONArray.toJSONString(excelHead) + "，excelRows：" + JSONArray.toJSONString(excelRows), e);
         }
        return null;
    }

    /**
     * 导出文件测试
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        tableName = "tmp20220707_obj";
        queryFields = "object_name,object_id,object_type,created";

        //导出包含数据内容的文件
        LinkedHashMap<String, String> headColumnMap = Maps.newLinkedHashMap();
        String[] allArray = queryFields.split(",");
        for (int i = 0; i < allArray.length; i++) {
            headColumnMap.put(allArray[i],allArray[i]);
        }
/*
        headColumnMap.put("className","班级");
        headColumnMap.put("name","学生信息,姓名");
        headColumnMap.put("sex","学生信息,性别");
        headColumnMap.put("opDate","操作时间");
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> dataMap = Maps.newHashMap();
            dataMap.put("className", "一年级");
            dataMap.put("name", "张三" + i);
            dataMap.put("sex", "男");
            dataMap.put("opDate", new Date(System.currentTimeMillis() + i));
            dataList.add(dataMap);
        }

       byte[] stream = exportExcelFile(headColumnMap, getData());
        FileOutputStream outputStream = new FileOutputStream(new File("C:\\javapro\\dynEasy\\" + "easyexcel-export-user51.xlsx"));
        outputStream.write(stream);
        outputStream.close();*/
    }

    public static List<Map<String, Object>> getData() {
        //List<String[]> checkTables = new ArrayList<>(10000);
        List<Map<String, Object>> dataList = new ArrayList<>();
        String[] allArray = queryFields.split(",");
        String sql = "SELECT " + queryFields + " FROM " + tableName;
        Connection con = Conn.getConnection();
        Assert.notNull(con, "con 连接对象不能为空");
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs != null && rs.next()) {
                //String[] fieldValues = new String[allArray.length];
                Map<String, Object> dataMap = Maps.newHashMap();
                for (int i = 0; i < allArray.length; i++) {
                    Object value = rs.getObject(allArray[i]);
                    dataMap.put(allArray[i], value == null ? null : value.toString());
                    //fieldValues[i] = value == null ? null : value.toString();
                }
                //checkTables.add(fieldValues);
                dataList.add(dataMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static List<String[]> getData1() {
        List<String[]> checkTables = new ArrayList<>(10000);
        String[] allArray = queryFields.split(",");
        String sql = "SELECT " + queryFields + " FROM " + tableName;
        Connection con = Conn.getConnection();
        Assert.notNull(con, "con 连接对象不能为空");
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs != null && rs.next()) {
                String[] fieldValues = new String[allArray.length];
                for (int i = 0; i < allArray.length; i++) {
                    Object value = rs.getObject(allArray[i]);
                    fieldValues[i] = value == null ? null : value.toString();
                }
                checkTables.add(fieldValues);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return checkTables;
    }

    public static class Conn {
        private static Connection conn;

        //String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai";
        private static String url = "jdbc:oracle:thin:@10.10.3.15:1521:kfdb";

        //String Driver = "com.mysql.cj.jdbc.Driver";
        private static String Driver = "oracle.jdbc.driver.OracleDriver";
        private static String Userid = "zhanghongjing";
        private static String Password = "zhanghongjing";

        public static void setUrl(String url) {
            Conn.url = url;
        }

        public static void setDriver(String driver) {
            Driver = driver;
        }

        public static void setUserid(String userid) {
            Userid = userid;
        }

        public static void setPassword(String password) {
            Password = password;
        }

        public static Connection getConnection() {
            if (conn != null) {
                return conn;
            }

             try {
                Class.forName(Driver);
                System.out.println(url);
                conn = DriverManager.getConnection(url, Userid, Password
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return conn;
        }

    }

    @Test
    public void sheetImport() throws FileNotFoundException {
        // 输出流
        tableName = "tmp20220707_obj";
        queryFields = "object_name,object_id,object_type,created";
        OutputStream outputStream = null;
        outputStream = new FileOutputStream(new File("C:\\javapro\\dynEasy\\" + "easyexcel-export-user51.xlsx"));

        // 导出的数据
        //getData()

        // 标题
        List<List<String>> headList = new ArrayList<>();
        headList.add(Lists.newArrayList("班级"));
        headList.add(Lists.newArrayList("学生信息", "姓名"));
        headList.add(Lists.newArrayList("学生信息","年龄"));
        headList.add(Lists.newArrayList("学生信息","入学时间"));

        // 测试多sheel导出
        ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
        WriteSheet test1 = EasyExcel.writerSheet(0, "test1")
                //.head(headList)
                .build();
        WriteSheet test2 = EasyExcel.writerSheet(1, "test2")
                //.head(headList)
                .build();
        excelWriter.write(getData(),test1).write(getData(),test2);
        excelWriter.finish();
    }
}



