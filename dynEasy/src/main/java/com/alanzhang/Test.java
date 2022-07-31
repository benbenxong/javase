package com.alanzhang;

public class Test {

    public static void main(String[] args) {
            String ss = ",aa,\nbb,cc,\ndd,,,";
            String[] array = ss.split(",");
        System.out.println(ss);
            System.out.println(array.length);//结果是5，而不是预想中的8
            for (int i = 0; i < array.length; i++) {
                System.out.println(array[i]);
            }
        }
}
