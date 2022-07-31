package com.alanzhang.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;

public class Test {
    public static void main(String[] args) {
        //读文件
        //创建ExcelReaderBuilder实例
        ExcelReaderBuilder readerBuilder = EasyExcel.read();
        //获取文件对象
        readerBuilder.file("D:\\worknote\\java\\easyexcel\\例子07.xlsx");
        //指定sheet(不指定，获取全部sheet)
        readerBuilder.sheet();
        //自动关闭输入流
        readerBuilder.autoCloseStream(true);
        //设置Excel文件格式
        readerBuilder.excelType(ExcelTypeEnum.XLSX);
        //注册监听器
        readerBuilder.registerReadListener(new AnalysisEventListener<Object>() {
            @Override
            public void invoke(Object o, AnalysisContext analysisContext) {
                //一行读取完成后的回调。（o是一行）
                System.out.println(o);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                //通知读取文件完毕
                System.out.println("数据读取完毕！");
            }
        });
        //构建读取器
        ExcelReader build = readerBuilder.build();
        //读取数据
        build.readAll();
        //设置读取完毕
        build.finish();

    }
}
