package com.alanzhang.utils;
//https://icode.best/i/50786441729196
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import groovy.lang.GroovyClassLoader;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenEntityUtil {

    private static String tableName;

    private static String queryFields;
    /**
     * 解析处理(生成实体类主体代码)
     */

    private static String parse(String[] colNames) {
        StringBuilder sb = new StringBuilder();

        sb.append("package com.alanzhang.pojo;").append("\n");
        sb.append("import com.alibaba.excel.annotation.ExcelProperty;").append("\n\n");

        sb.append("public class ").append(camelStr(tableName, true)).append(" {\r\n");

        processAllAttrs(sb, colNames);

        processAllMethod(sb, colNames);

        sb.append("}\r\n");

        System.out.println(sb.toString());

        return sb.toString();

    }

    /**
     * 生成所有的方法
     *
     * @param sb
     */

    private static void processAllMethod(StringBuilder sb, String[] colNames) {
        for (String colname : colNames) {
            sb.append("\tpublic " + "String" + " get").append(camelStr(colname, true)).append("(){\r\n");

            sb.append("\t\treturn ").append(camelStr(colname)).append(";\r\n");

            sb.append("\t}\r\n");

            sb.append("\tpublic void set").append(camelStr(colname, true)).append("(").append("String").append(" ").append(camelStr(colname)).append("){\r\n");

            sb.append("\t\tthis.").append(camelStr(colname)).append(" = ").append(camelStr(colname)).append(";\r\n");

            sb.append("\t}\r\n");

        }
    }


    /**
     * 解析输出属性
     *
     * @return
     */

    private static void processAllAttrs(StringBuilder sb, String[] colNames) {
        for (String colname : colNames) {
            sb.append("\t@ExcelProperty(\"").append(colname).append("\")\r\n");
            sb.append("\tprivate " + "String" + " ").append(camelStr(colname)).append(";\r\n");
        }

    }

    /**
     * 把输入字符串的首字母改成大写, 下划线的colName转换为驼峰
     *
     * @param str colName
     * @return camel name
     */

    private static String camelStr(String str, boolean... method) {
        char[] ch = str.toCharArray();
        for (int i = 1; i < ch.length; i++) {
            if (ch[i - 1] == '_') {
                ch[i] = (char) (ch[i] - 32);
            }
        }
        if (method.length > 0) {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch).replace("_", "");
    }

    /**
     * 获取项目所在路径
     *
     * @return
     */
    public static String getRealPath() throws IOException {
        String realPath = GenEntityUtil.class.getClassLoader().getResource("").getPath();
        realPath = realPath + File.separator + "com" +
                File.separator + "alanzhang" +
                File.separator + "pojo" +
                File.separator;
        try {
            //路径decode转码
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realPath;
    }

    public static void doGen() throws Exception {
        String[] columnNames = queryFields.split(",");
        String content = parse(columnNames);
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        Class<?> clazz = groovyClassLoader.parseClass(content);
        export(clazz, data(clazz, columnNames, getData()));
    }

    public static List<Object> data(Class<?> loadClass, String[] columnNames, List<String[]> rawData) throws Exception {
        List<Object> data = new ArrayList<>(rawData.size());
        for (String[] rawDatum : rawData) {
            Object instance = loadClass.newInstance();
            for (int i = 0; i < rawDatum.length; i++) {
                String field = camelStr(columnNames[i], true);
                Method method = loadClass.getDeclaredMethod("set" + field, String.class);
                method.invoke(instance, rawDatum[i]);

            }
            data.add(instance);
        }
        return data;
    }

    public static void export(Class<?> clazz, List<Object> tableList) {
        String fileName = "C:\\Users\\alan\\Desktop\\" + System.currentTimeMillis() + ".xlsx";
        EasyExcelFactory.write(fileName, clazz).sheet("sheet1").doWrite(tableList);


    }

    public static void main(String[] args) throws Exception {
        tableName = "tmp20220707_obj";
        queryFields = "object_name,object_id,object_type,created";
        doGen();
    }

    public static List<String[]> getData() {
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

        public static Connection getConnection() {
            if (conn != null) {
                return conn;
            }
            //String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai";
            String url = "jdbc:oracle:thin:@10.10.3.15:1521:kfdb";

            //String Driver = "com.mysql.cj.jdbc.Driver";
            String Driver = "oracle.jdbc.driver.OracleDriver";
            String Userid = "zhanghongjing";
            String Password = "zhanghongjing";
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

}