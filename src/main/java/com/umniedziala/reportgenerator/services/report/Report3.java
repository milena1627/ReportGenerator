package com.umniedziala.reportgenerator.services.report;

import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Project;
import com.umniedziala.reportgenerator.datamodel.Reports.ReportModel;
import com.umniedziala.reportgenerator.storage.DataStorage;
import lombok.val;
import org.apache.poi.ss.usermodel.CellType;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


public class Report3 implements IReport {

  private static final ReportModel.Cell COLUMN_1_NAME= new ReportModel.Cell("Miesiąc", CellType.STRING);
  private static final ReportModel.Cell COLUMN_2_NAME= new ReportModel.Cell("Projekt", CellType.STRING);
  private static final ReportModel.Cell COLUMN_3_NAME= new ReportModel.Cell("Godziny", CellType.STRING);

  @Override
  public ReportModel generateReport(DataStorage dataStorage, Map<String, String> filters) {
    val reportModel = new ReportModel();
    reportModel.setReportName("Report 3");

    val employeeName = filters.get("employee");
    val year = filters.get("year");

    val data = dataStorage.getEmployees();

    val employeeProjects = data.stream()
            .filter(employee -> employee.getName().equals(employeeName))
            .map(Employee::getListOfProjects)
            .flatMap(Collection::stream)
            .sorted(Comparator.comparing(Project::getName))
            .collect(Collectors.toList());

    LinkedList <ReportModel.Row> rows = new LinkedList<>();

    LinkedList<ReportModel.Cell> columnNames = new LinkedList<>();
    columnNames.add(COLUMN_1_NAME);
    columnNames.add(COLUMN_2_NAME);
    columnNames.add(COLUMN_3_NAME);
    rows.add(new ReportModel.Row(columnNames));

    for (int monthIterator = 0; monthIterator < 12; monthIterator++){
      for (int i=0; i<employeeProjects.size(); i++) {
        double hoursInMonth = 0;
        try {
          hoursInMonth = employeeProjects.get(i).getSumOfMonthlyProjectHoursInSpecificYear(String.valueOf(monthIterator), year);
        } catch (ParseException e) {
          e.printStackTrace();
        }
        if(hoursInMonth !=0){
          LinkedList<ReportModel.Cell> cells = new LinkedList<>();
          cells.add(new ReportModel.Cell(String.valueOf(monthIterator), CellType.NUMERIC));
          cells.add(new ReportModel.Cell(employeeProjects.get(i).getName(), CellType.STRING));
          cells.add(new ReportModel.Cell(String.valueOf(hoursInMonth), CellType.NUMERIC));
          rows.add(new ReportModel.Row(cells));
        }
      }
    }
    reportModel.setRows(rows);

    return reportModel;
  }

}