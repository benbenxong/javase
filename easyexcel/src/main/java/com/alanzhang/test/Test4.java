package com.alanzhang.test;
//https://www.justdojava.com/2021/04/18/easyexcel-01/
//https://blog.csdn.net/baidu_25117757/article/details/124256177?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1-124256177-blog-118461874.pc_relevant_multi_platform_whitelistv1&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1-124256177-blog-118461874.pc_relevant_multi_platform_whitelistv1&utm_relevant_index=1
//https://blog.csdn.net/qq_41307443/article/details/80762255
//https://mvnrepository.com/artifact/org.springframework/spring-core/5.3.21
//https://www.bilibili.com/video/BV1fK4y1D7Mu?spm_id_from=333.337.search-card.all.click&vd_source=12e6b3ebbf619495c398ebaa653772d2
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
//import org.apache.commons.compress.utils.Lists;

import java.util.*;

public class Test4 {
    public static void main(String[] args) {
        //获取列名称
        List<List<String>> headList = new ArrayList<>();
        headList.add(Lists.newArrayList("班级"));
        headList.add(Lists.newArrayList("学生信息", "姓名"));
        headList.add(Lists.newArrayList("学生信息","年龄"));
        headList.add(Lists.newArrayList("学生信息","入学时间"));

        List<Map<Integer,String>> list = parseDate();

        EasyExcel.write("D:\\worknote\\java\\easyexcel\\例子07_副本.xlsx")
                .head(headList)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet("s11111")
                .doWrite(list);
    }

    public static List<Map<Integer,String>> parseDate(){
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
        return list;

    }
}
