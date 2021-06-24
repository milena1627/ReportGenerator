package com.umniedziala.reportgenerator.services.report;

import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Reports.ReportModel;
import com.umniedziala.reportgenerator.storage.DataStorage;
import lombok.val;
import org.apache.poi.ss.usermodel.CellType;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Report1 implements IReport {

	private static final ReportModel.Cell COLUMN_1_NAME= new ReportModel.Cell("L.p.", CellType.STRING);
	private static final ReportModel.Cell COLUMN_2_NAME= new ReportModel.Cell("Nazwisko i Imię", CellType.STRING);
	private static final ReportModel.Cell COLUMN_3_NAME= new ReportModel.Cell("Godziny", CellType.STRING);

	@Override
	public ReportModel generateReport(DataStorage dataStorage, Map<String, String> filters) {
		val reportModel = new ReportModel();
		reportModel.setReportName("Report 1");

		val year = filters.get("year");
		val data = dataStorage.getEmployees();
		val employees= data.stream()
				.collect(Collectors.toMap(Employee::getName,employee -> employee.sumOfHours(year),(v1, v2)->v1, TreeMap::new));

		LinkedList<ReportModel.Row> rows = new LinkedList<>();

		LinkedList<ReportModel.Cell> columnNames = new LinkedList<>();
		columnNames.add(COLUMN_1_NAME);
		columnNames.add(COLUMN_2_NAME);
		columnNames.add(COLUMN_3_NAME);
		rows.add(new ReportModel.Row(columnNames));

		var i=1;
		for (val project: employees.entrySet()) {
			if (project.getValue() == 0) {
				continue;
			}
			LinkedList<ReportModel.Cell> cells = new LinkedList<>();
			cells.add(new ReportModel.Cell(String.valueOf(i), CellType.NUMERIC));
			cells.add(new ReportModel.Cell(project.getKey(), CellType.STRING));
			cells.add(new ReportModel.Cell(String.valueOf(project.getValue()), CellType.NUMERIC));
			rows.add(new ReportModel.Row(cells));
			i++;
		}

		val sumOfHours = employees.values().stream().reduce(0d, Double::sum);
		LinkedList<ReportModel.Cell> sum = new LinkedList<>();
		sum.add(new ReportModel.Cell("Suma", CellType.STRING));
		sum.add(new ReportModel.Cell("", CellType.STRING));
		sum.add(new ReportModel.Cell(String.valueOf(sumOfHours), CellType.NUMERIC));
		rows.add(new ReportModel.Row(sum));

		reportModel.setRows(rows);
		return reportModel;
	}
	
	
	
	

}
