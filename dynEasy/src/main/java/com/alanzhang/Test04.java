package com.alanzhang;

import java.io.*;

public class Test04 {
    public static void main(String[] args) throws IOException {
        File f1 = new File("C:\\javapro\\dynEasy\\out.sql");
        File f2 = new File("C:\\javapro\\dynEasy\\out1.sql");

        FileReader fr = new FileReader(f1);
        FileWriter fw = new FileWriter(f2);

        /* 方式1：
        int n = fr.read();
        while(n != -1){
            fw.write(n);
            n = fr.read();
        }*/

        /*方式2：
        char[] ch = new char[1000];
        int len = fr.read(ch);
        while(len != -1){
            fw.write(ch, 0, len);
            len = fr.read(ch);
        }*/

        char[] ch = new char[1000];
        int len = fr.read(ch);
        while(len != -1){
            String s = new String(ch,0,len);
            fw.write(s);
            len = fr.read(ch);
        }
        fw.close();
        fr.close();


    }
}
