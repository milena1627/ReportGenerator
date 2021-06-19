package com.umniedziala.reportgenerator.report;

import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Task;
import com.umniedziala.reportgenerator.storage.DataStorage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class Report1 implements IReport {
	
	String reportName;
	
	
	
	
	public void generateReport() {
		DataStorage dataStorage = DataStorage.getInstance();
		ArrayList<Employee> dataFromFiles = dataStorage.getDataFromFiles();

		File file = new File("./src/main/resources/report1.xlsx");
		try {
			// aby zapisac do istniejacego pliku!!
			/*if(!file.exists()) {
				file.createNewFile();
			}
			FileInputStream inputStream = new FileInputStream(file);*/
			//Workbook workbook = new HSSFWorkbook(inputStream);
			// aby zapisac do nowego pliku, czystego
			Workbook workbook = new XSSFWorkbook();

			Sheet sheet = workbook.createSheet("arkusz1");

			// uzupelnienie arkusza danymi
			for (int i = 0; i < dataFromFiles.size(); i++) {
				Row row = sheet.createRow(i); // pusty wiersz (o numerze 'i')
				int column = 0; // zaczynamy od lewej strony
				Cell cell = row.createCell(column++); // pierwsza komorka
				cell.setCellValue(dataFromFiles.get(i).getName());
				cell = row.createCell(column++); // druga komorka (w prawo)
				cell.setCellValue(dataFromFiles.get(i).getSurname());
				HashSet<Task> listOfTasks = dataFromFiles.get(i).getListOfTasks();
				for(Task t : listOfTasks) {
					cell = row.createCell(column++); // kolejne komorki w prawo
					cell.setCellValue(t.getNumberOfHours());
				}
			}


			FileOutputStream outputStream = new FileOutputStream(file);
			workbook.write(outputStream);
			outputStream.close();

			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	

}
