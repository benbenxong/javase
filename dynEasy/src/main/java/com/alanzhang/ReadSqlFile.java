package com.alanzhang;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ReadSqlFile {

    public static String getFileCharsetByICU4J(File file) {
        String encoding = null;

        try {
            Path path = Paths.get(file.getPath());
            byte[] data = Files.readAllBytes(path);
            CharsetDetector detector = new CharsetDetector();
            detector.setText(data);
            //这个方法推测首选的文件编码格式
            CharsetMatch match = detector.detect();
            //这个方法可以推测出所有可能的编码方式
            CharsetMatch[] charsetMatches = detector.detectAll();
            if (match == null) {
                return encoding;
            }
            encoding = match.getName();
        } catch (IOException var6) {
            System.out.println(var6.getStackTrace());
        }
        return encoding;
    }

    public static String[] getSQLs(String sqlFile) throws IOException {
        String[] sqls = null;
        //String content = null;

        StringBuilder builder = new StringBuilder();

        File file = new File(sqlFile);
        String code = getFileCharsetByICU4J(file);

        System.out.println(code);

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, code);

        char[] ch = new char[20];
        int len = isr.read(ch);
        while (len != -1){
            String content = new String(ch,0,len);
            System.out.print(content);
            builder.append(content);
            len = isr.read(ch);
        }

        isr.close();
//        while ((content = bufferedReader.readLine()) != null)
//            builder.append(content + "\n");

        sqls = builder.toString().trim().split(";");
        for (int i = 0; i < sqls.length; i++) {
            System.out.println(i + ": " + sqls[i]);
        }
        return sqls;
    }

    public static void main(String[] args) throws Exception {
        String[] sqls;
        String content = "";
        StringBuilder builder = new StringBuilder();

        File file = new File("C:\\javapro\\dynEasy\\out.sql");
        String code = getFileCharsetByICU4J(file);

        System.out.println(code);

        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file), code);
        BufferedReader bufferedReader = new BufferedReader(streamReader);

        while ((content = bufferedReader.readLine()) != null)
            builder.append(content + "\n");

        //sqls = builder.toString().trim().split(";");
        sqls = getSQLs("C:\\javapro\\dynEasy\\out.sql");
        for (int i = 0; i < sqls.length; i++) {
           System.out.println(i + ": " + sqls[i]);
        }

        //System.out.println(builder.toString());
    }


}
