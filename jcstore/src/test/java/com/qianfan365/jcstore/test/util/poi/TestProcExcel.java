package com.qianfan365.jcstore.test.util.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TestProcExcel {

  public static void main(String[] args) {
    String path = "D:\\workspace\\0924\\1.2版本\\示例下载.xlsx";
    File file = new File(path);
    try {
      Map<String, Object> map = importExcelFileToDataArray(new FileInputStream(file), 1);
      System.out.println(map);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static Logger logger = Logger.getLogger(TestProcExcel.class);

  /**
   * 批量导入Excel表
   * 
   * @param is
   * @param startRow
   * @return 返回Excel表的数据矩阵Array[行][列]
   */
  public static Map<String, Object> importExcelFileToDataArray(InputStream is, int startRow) {
    try {
      XSSFWorkbook workBook = new XSSFWorkbook(is);
      int index = workBook.getActiveSheetIndex();
      System.out.println(index);
      XSSFSheet sheet = workBook.getSheetAt(0);
      // 获取Sheet表中所包含的最后一行行号
      int sheetRows = sheet.getLastRowNum();
      // 获取Sheet表中所包含的最后一列的列号
      int sheetLines = sheet.getRow(startRow).getLastCellNum();
      logger.debug("\n读取Excel表格:\n" + "总行数： " + sheetRows + "\n" + "总列数： " + sheetLines + "\n"
          + "开始行号： " + startRow + "\n");
      // 实际数据字段行数
      int rowCnt = sheet.getLastRowNum() - startRow + 1;
      // 实际数据字段列数
      int lineCnt = sheet.getRow(startRow).getLastCellNum();
      HashMap<String, Object> ret = Maps.newHashMap();
      ret.put("rowNum", rowCnt);
      if (rowCnt > 1000) {
        return ret;
      }
      // 记录错误数据行号
      List<Integer> dataArray = Lists.newArrayList();
      // 记录错误正确数据
      List<ExcelProduct> productArray = Lists.newArrayList();

      // 过滤第一行标题
      for (int rowIndex = 0; rowIndex < sheetRows - startRow + 1; rowIndex++) {
        // logger.debug("读取第" + (rowIndex) + "行数据");
        XSSFRow row = sheet.getRow(rowIndex + startRow);
        List<String> values=Lists.newArrayList();
        for (int lineIndex = 0; lineIndex < sheetLines; lineIndex++) {
          // logger.debug("读取第" + (lineIndex) + "列数据");
          XSSFCell cell = row.getCell(lineIndex);
          cell.setCellType(XSSFCell.CELL_TYPE_STRING);// 默认都先设置为String
          // 设置值
          String stringCellValue = cell.getStringCellValue();
          System.out.print(stringCellValue + "\t");
          // 规则校验
          boolean fit = checkCellValue(lineIndex, stringCellValue);
          if (!fit) {
            dataArray.add(rowIndex);
            break;
          }
          values.add(stringCellValue);
        }
        if(values.size()==lineCnt){
          productArray.add(new ExcelProduct(values));
        }
      }
      System.out.println();
      ret.put("errorRowList", dataArray);
      ret.put("errorRowListSize", dataArray.size());
      ret.put("productList", productArray);
      return ret;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static boolean checkCellValue(int lineIndex, String stringCellValue) {
    // TODO Auto-generated method stub
    return true;
  }

}
