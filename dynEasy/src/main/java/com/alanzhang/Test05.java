package com.alanzhang;

import java.io.*;

public class Test05 {
    public static void main(String[] args) throws IOException {
        File f = new File("C:\\javapro\\dynEasy\\out.sql");
        FileInputStream fis = new FileInputStream(f);
        InputStreamReader isr = new InputStreamReader(fis,"gbk");
        char[] ch = new char[20];
        int len = isr.read(ch);
        while (len != -1){
            System.out.print(new String(ch,0,len));
            len = isr.read(ch);
        }
        isr.close();
    }
}
