package com.umniedziala.reportgenerator.services.report;

import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Project;
import com.umniedziala.reportgenerator.datamodel.Reports.ReportModel;
import com.umniedziala.reportgenerator.datamodel.Task;
import com.umniedziala.reportgenerator.storage.DataStorage;
import lombok.val;
import org.apache.poi.ss.usermodel.CellType;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Report2 implements IReport {

  private static final ReportModel.Cell COLUMN_1_NAME= new ReportModel.Cell("L.p.", CellType.STRING);
  private static final ReportModel.Cell COLUMN_2_NAME= new ReportModel.Cell("Projekt", CellType.STRING);
  private static final ReportModel.Cell COLUMN_3_NAME= new ReportModel.Cell("Godziny", CellType.STRING);

  @Override
  public ReportModel generateReport(DataStorage dataStorage, String year) {
    val reportModel = new ReportModel();
    reportModel.setReportName("Report 2");

    val data = dataStorage.getEmployees();
    val projects= data.stream()
        .map(Employee::getListOfProjects)
        .flatMap(Collection::stream)
        .collect(groupingBy(Project::getName, TreeMap::new,
            Collectors.mapping(project -> {
              try {
                return project.getSumOfHours(year);
              } catch (ParseException e) {
                e.printStackTrace();
              }
              return null;
            },Collectors.summingDouble(Double::doubleValue))));

    LinkedList <ReportModel.Row> rows = new LinkedList<>();

    LinkedList<ReportModel.Cell> columnNames = new LinkedList<>();
    columnNames.add(COLUMN_1_NAME);
    columnNames.add(COLUMN_2_NAME);
    columnNames.add(COLUMN_3_NAME);
    rows.add(new ReportModel.Row(columnNames));

    var i=1;
    for (val project: projects.entrySet()) {
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

    val sumOfHours = projects.values().stream().reduce(0d, Double::sum);
    LinkedList<ReportModel.Cell> sum = new LinkedList<>();
    sum.add(new ReportModel.Cell("Suma", CellType.STRING));
    sum.add(new ReportModel.Cell("", CellType.STRING));
    sum.add(new ReportModel.Cell(String.valueOf(sumOfHours), CellType.NUMERIC));
    rows.add(new ReportModel.Row(sum));

    reportModel.setRows(rows);
    return reportModel;
  }

}
