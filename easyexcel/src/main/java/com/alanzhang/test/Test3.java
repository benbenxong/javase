package com.alanzhang.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.*;

public class Test3 {
    public static void main(String[] args) {
        List<Map<Integer,String>> list = new LinkedList<>();
        EasyExcel.read("D:\\worknote\\java\\easyexcel\\例子07.xlsx")
                .sheet()
                .registerReadListener(new AnalysisEventListener<Map<Integer,String>>() {
                    @Override
                    public void invoke(Map<Integer, String> integerStringMap, AnalysisContext analysisContext) {
                        list.add(integerStringMap);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                        System.out.println("数据读取完毕！");
                    }
                }).doRead();
        for (Map<Integer, String> integerStringMap : list) {
            Set<Integer> keySet = integerStringMap.keySet();
            Iterator<Integer> iterator = keySet.iterator();

            if(iterator.hasNext()){
                Integer key = iterator.next();
                System.out.print(key +": " + integerStringMap.get(key) );
            }
            while (iterator.hasNext()) {
                System.out.print(" ,");
                Integer key = iterator.next();
                System.out.print(key +": " + integerStringMap.get(key) );
            }
            System.out.println();

        }
    }
}
