package org.example.step_definitions;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;


public class readBranches {

    public static void main(String[] args) {


        String fileName = new String("categorizes.xlsx");

        try {
            //Create the input stream from the xlsx/xls file
            FileInputStream fis = new FileInputStream(fileName);

            //Create Workbook instance for xlsx/xls file input stream
            Workbook workbook = null;
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            }


            Sheet sheet = workbook.getSheet("Branchen");

            System.out.println(sheet.getSheetName());

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                System.out.println(row.getCell(0));

/*                Iterator<Cell> cellIterator = row.cellIterator();


                while (cellIterator.hasNext()) {
                    //Get the Cell object
                    Cell cell = cellIterator.next();


                    System.out.println(cell.getStringCellValue());

                }*/

                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
