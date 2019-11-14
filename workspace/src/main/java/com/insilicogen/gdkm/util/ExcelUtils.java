package com.insilicogen.gdkm.util;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtils {
	private static Sheet s;
	public static final int HSSF = 0;
	public static final int XSSF = 1;
	public static final int SXSSF = 2;
	private static Workbook wb = new HSSFWorkbook();

	public static Row getRow(int i) {
		Row r = s.getRow(i);
		if (r == null)
			r = s.createRow(i);
		return r;
	}	

	public static Cell getCell(int row, int cell) {
		Row r = getRow(row);
		Cell c = r.getCell(cell);
		if (c == null)
			c = r.createCell(cell);
		return c;
	}


	/*public static void writeWorkbook(String fileName) {
		long start = System.currentTimeMillis();
		try {
			s = wb.createSheet("sample Sheet");
			for (int i = 0; i < 10000; i++) {
				setCellValue(i, 0, "Test_Title_" + i);
				setCellValue(i, 1, "Test_Title_" + i);
				setCellValue(i, 2, "Test_Title_" + i);
				setCellValue(i, 3, "Test_Title_" + i);
				setCellValue(i, 4, "Test_Title_" + i);
				setCellValue(i, 5, "Test_Title_" + i);
				setCellValue(i, 6, "Test_Title_" + i);
				setCellValue(i, 7, "Test_Title_" + i);
				setCellValue(i, 8, "Test_Title_" + i);
				setCellValue(i, 9, "Test_Title_" + i);
				setCellValue(i, 10, "Test_Title_" + i);
			}
			wb.write(new FileOutputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		long end = System.currentTimeMillis();
		System.out.println("writeHSSFWorkbook : " + (end - start));
	}*/

}
