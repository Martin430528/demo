package com.project.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel工具类
 * 
 * @author limin
 * 
 */
public class ExcelTools {

	public static final String XLS = "xls";
	public static final String XLSX = "xlsx";

	/**
	 * Excel 文件读取
	 * 
	 * @param book
	 * @return
	 */
	public static List<Map<String, Object>> getWorkbookData(InputStream is,
			String type, String[] keys, int rowNum, int sheetNum)
			throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int cellNum = 0;
		if (keys != null) {
			cellNum = keys.length;
		}
		if (XLS.equals(type)) {
			HSSFWorkbook book = new HSSFWorkbook(is);
			HSSFSheet sheet = book.getSheetAt(sheetNum);
			for (int i = 1; i < rowNum; i++) {
				HSSFRow row = sheet.getRow(i);

				Map<String, Object> map = new HashMap<String, Object>();
				for (int j = 0; j < cellNum; j++) {
					if (row != null) {
						HSSFCell cell = row.getCell(j);
						String key = keys[j];
						Object value = getValue(cell);
						map.put(key, value);
					}
				}
				list.add(map);
			}
			book.close();
		} else if (XLSX.equals(type)) {
			XSSFWorkbook book = new XSSFWorkbook(is);
			XSSFSheet sheet = book.getSheetAt(sheetNum);
			for (int i = 1; i < rowNum; i++) {
				XSSFRow row = sheet.getRow(i);

				Map<String, Object> map = new HashMap<String, Object>();
				for (int j = 0; j < cellNum; j++) {
					if (row != null) {
						XSSFCell cell = row.getCell(j);
						String key = keys[j];
						Object value = getValue(cell);
						map.put(key, value);
					}
				}
				list.add(map);
			}
			book.close();
		}

		return list;
	}

	/**
	 * 获得Excel某行某列的值
	 * 
	 * @param obj
	 * @return
	 */
	public static Object getValue(Object obj) {
		Object value = null;
		if (obj instanceof HSSFCell) {
			HSSFCell cell = (HSSFCell) obj;
			int type = cell.getCellType();
			switch (type) {
			case HSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				value = cell.getNumericCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				value = cell.getBooleanCellValue();
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				value = cell.getNumericCellValue();
				break;
			default:
				value = null;
			}
		} else if (obj instanceof XSSFCell) {
			XSSFCell cell = (XSSFCell) obj;
			int type = cell.getCellType();
			switch (type) {
			case XSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case XSSFCell.CELL_TYPE_NUMERIC:
				value = cell.getNumericCellValue();
				break;
			case XSSFCell.CELL_TYPE_BOOLEAN:
				value = cell.getBooleanCellValue();
				break;
			case XSSFCell.CELL_TYPE_FORMULA:
				value = cell.getNumericCellValue();
				break;
			default:
				value = null;
			}
		}

		return value;
	}

}
