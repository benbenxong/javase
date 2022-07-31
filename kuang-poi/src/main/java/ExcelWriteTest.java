import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.junit.Test;
//import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ExcelWriteTest {
    String PATH ="C:\\javapro\\kuang-poi\\";

    @Test
    public void testWrite03() throws Exception {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("统计表");
        Row row1 = sheet.createRow(0);
        Cell cell11 = row1.createCell(0);
        cell11.setCellValue("今日新增");
        Cell cell12 = row1.createCell(1);
        cell12.setCellValue(666);

        Row row2 = sheet.createRow(1);
        Cell cell21 = row2.createCell(0);
        cell21.setCellValue("日期");
        Cell cell22 = row2.createCell(1);
        String time = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        cell22.setCellValue(time);

        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "统计表03.xls");
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        System.out.println("文件输出完毕！");

    }

    @Test
    public void testWrite07() throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("统计表");
        Row row1 = sheet.createRow(0);
        Cell cell11 = row1.createCell(0);
        cell11.setCellValue("今日新增");
        Cell cell12 = row1.createCell(1);
        cell12.setCellValue(666);

        Row row2 = sheet.createRow(1);
        Cell cell21 = row2.createCell(0);
        cell21.setCellValue("日期");
        Cell cell22 = row2.createCell(1);
        String time = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        cell22.setCellValue(time);

        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "统计表07.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        System.out.println("文件输出完毕！");

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