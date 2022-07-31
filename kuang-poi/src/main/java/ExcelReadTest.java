import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFHeaderFooterProperties;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.junit.Test;
//import sun.plugin.dom.exception.BrowserNotSupportedException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

public class ExcelReadTest {
    String PATH ="C:\\javapro\\kuang-poi\\";

    @Test
    public void testRead03() throws Exception {
        //获取文件流
        FileInputStream fileInputStream = new FileInputStream(PATH + "统计表03.xls");
        //新建工作簿。用文件流。
        Workbook workbook = new HSSFWorkbook(fileInputStream);
        //得到表
        Sheet sheet = workbook.getSheetAt(0);
        //得到行
        Row row = sheet.getRow(0);
        //等到列
        Cell cell = row.getCell(0);
        //得到值
        System.out.println(cell.getStringCellValue());
        fileInputStream.close();


    }

    @Test
    public void testRead07() throws Exception {
        //获取文件流
        FileInputStream fileInputStream = new FileInputStream(PATH + "统计表07.xlsx");
        //新建工作簿。用文件流。
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        //得到表
        Sheet sheet = workbook.getSheetAt(0);
        //得到行
        Row row = sheet.getRow(0);
        //等到列
        Cell cell = row.getCell(1);
        //得到值
        System.out.println(cell.getNumericCellValue());
        fileInputStream.close();


    }

    @Test
    public void testCellType() throws Exception {
        //获取文件流
        FileInputStream fileInputStream = new FileInputStream(PATH + "20170613药品调查7026.xlsx");
        //新建工作簿。用文件流。
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        //得到表
        Sheet sheet = workbook.getSheetAt(0);
        //得到行 等到标题行
        Row rowTitle = sheet.getRow(0);
        //等到列
        if(rowTitle != null){
            int cellCount = rowTitle.getPhysicalNumberOfCells();
            for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                Cell cell = rowTitle.getCell(cellNum);
                if(cell != null){
                    CellType cellType = cell.getCellType();
                    String cellValue = cell.getStringCellValue();
                    System.out.print(cellValue + " | ");
                }
            }
            System.out.println();
        }

        //获取所有数据行
        //int rowCount = sheet.getPhysicalNumberOfRows();
        int rowCount = 3;
        for (int rowNum = 1; rowNum < rowCount; rowNum++) {
            Row rowData = sheet.getRow(rowNum);
            if(rowData != null){
                int cellCount = rowData.getPhysicalNumberOfCells();
                for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                    System.out.print("[" + (rowNum+1)+"-"+(cellNum+1));
                    Cell cell = rowData.getCell(cellNum);
                    if(cell!=null) {
                        CellType cellType = cell.getCellType();
                        String cellValue = "";

                        switch (cellType){
                            case STRING:
                                System.out.print("--String--");
                                cellValue = cell.getStringCellValue();
                                break;
                            case BOOLEAN:
                                System.out.print("--Boolean--");
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case BLANK:
                                System.out.print("--Blank--");
                                break;
                            case _NONE:
                                break;
                            case NUMERIC://日期和数字
                                if(DateUtil.isCellDateFormatted(cell)){
                                    System.out.print("--Date--");
                                    Date date = cell.getDateCellValue();
                                    cellValue = new DateTime(date).toString("yyyy-MM-dd HH:mm:ss");

                                }else {

                                    System.out.print("--Number--");
                                    cell.setCellType(CellType.STRING);
                                    cellValue = cell.toString();
                                }
                                break;
                            case FORMULA:
                                break;
                            case ERROR:
                                System.out.print("--error--");
                                break;
                        }
                        System.out.println(cellValue);

                    }



                }

            }

        }
        fileInputStream.close();
    }

    @Test
    public  void testFormula() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(PATH + "公式.xls");
        Workbook workbook = new HSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(4);
        Cell cell = row.getCell(0);

        //拿到计算公式
        FormulaEvaluator formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
        //输出单元格内容
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case FORMULA:
                String cellFormula = cell.getCellFormula();
                System.out.println(cellFormula);
                CellValue evaluate = formulaEvaluator.evaluate(cell);
                System.out.println(evaluate.formatAsString());
                break;

        }
        fileInputStream.close();

    }

    @Test
    public void testWrite03Bigdata() throws Exception {
        long begin = System.currentTimeMillis();
        //建一个簿
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        for (int rowNum = 0; rowNum < 65536; rowNum++) {
            Row row = sheet.createRow(rowNum);
            for (int cellNum = 0; cellNum < 10; cellNum++) {
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(cellNum);
            }

        }
        System.out.println("over");
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "testWrite03Bigdata.xls");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        //建一个表

        //插入数据
        long end = System.currentTimeMillis();
        System.out.println((double) (end-begin)/1000);

    }

    @Test
    public void testWrite07Bigdata() throws Exception {
        long begin = System.currentTimeMillis();
        //建一个簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        for (int rowNum = 0; rowNum < 65537; rowNum++) {
            Row row = sheet.createRow(rowNum);
            for (int cellNum = 0; cellNum < 10; cellNum++) {
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(cellNum);
            }

        }
        System.out.println("over");
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "testWrite07Bigdata.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        //建一个表

        //插入数据
        long end = System.currentTimeMillis();
        System.out.println((double) (end-begin)/1000);

    }

    @Test
    public void testWrite07BigdataS() throws Exception {
        long begin = System.currentTimeMillis();
        //建一个簿
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        for (int rowNum = 0; rowNum < 100000; rowNum++) {
            Row row = sheet.createRow(rowNum);
            for (int cellNum = 0; cellNum < 10; cellNum++) {
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(cellNum);
            }

        }
        System.out.println("over");
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "testWrite07BigdataS.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        //清除临时文件
        ((SXSSFWorkbook)workbook).dispose();
        //建一个表

        //插入数据
        long end = System.currentTimeMillis();
        System.out.println((double) (end-begin)/1000);

    }
}