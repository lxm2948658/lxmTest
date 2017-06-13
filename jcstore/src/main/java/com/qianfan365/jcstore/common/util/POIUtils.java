package com.qianfan365.jcstore.common.util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class POIUtils {

  // 读取单元格内容
  public static String getCellValue(Cell cell) {  
    if(cell == null){
      return "";
    }
    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
    return cell.getStringCellValue();  
  }
  
  public static boolean isEmpty(Row row){
    short lastCellNum = row.getLastCellNum();
    for (int i = 0; i < lastCellNum; i++) {
      String value = POIUtils.getCellValue(row.getCell(i));
      if(StringUtils.isNotEmpty(value)){
        return true;
      }
    }
    return false;
  }
  
}
