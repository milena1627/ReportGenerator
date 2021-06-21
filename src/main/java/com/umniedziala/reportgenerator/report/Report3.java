package com.umniedziala.reportgenerator.report;

import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Project;
import com.umniedziala.reportgenerator.datamodel.Reports.ReportModel;
import com.umniedziala.reportgenerator.storage.DataStorage;
import lombok.val;
import org.apache.poi.ss.usermodel.CellType;

import java.util.*;
import java.util.stream.Collectors;


public class Report3 implements IReport {

  private static final ReportModel.Cell COLUMN_1_NAME= new ReportModel.Cell("Miesiąc", CellType.STRING);
  private static final ReportModel.Cell COLUMN_2_NAME= new ReportModel.Cell("Projekt", CellType.STRING);
  private static final ReportModel.Cell COLUMN_3_NAME= new ReportModel.Cell("Godziny", CellType.STRING);
  private static String employeeName;

  @Override
  public ReportModel generateReport(DataStorage dataStorage) {
    val reportModel = new ReportModel();
    reportModel.setReportName("Report 3");

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

    for (int j = 0; j < 12; j++){
      for (int i=0; i<employeeProjects.size(); i++) {
        double hoursInMonth = employeeProjects.get(i).getSumOfProjectHoursMonthly(j);
        if(hoursInMonth !=0){
          LinkedList<ReportModel.Cell> cells = new LinkedList<>();
          cells.add(new ReportModel.Cell(String.valueOf(j), CellType.NUMERIC));
          cells.add(new ReportModel.Cell(employeeProjects.get(i).getName(), CellType.STRING));
          cells.add(new ReportModel.Cell(String.valueOf(hoursInMonth), CellType.NUMERIC));
          rows.add(new ReportModel.Row(cells));
        }
      }
    }
    reportModel.setRows(rows);

    return reportModel;
  }

  public void selectEmployee (DataStorage storage){
    employeeName = ""; // cleaning employee name to prevent from executing unwanted Report creation
    String selectedEmployee="";
    val data = storage.getEmployees();
    List<String> employees = data.stream()
            .map(Employee::getName)
            .sorted()
            .collect(Collectors.toList());

    System.out.println("\nPRACOWNICY:");
    employees.forEach(System.out::println);

      while (!selectedEmployee.equals("0")) {
        selectedEmployee = getUserInput("\nWpisz Imię i Nazwisko (0 - Wyjście):");
        if(validateUserInput(selectedEmployee, data)){
          employeeName = selectedEmployee;
          break;
        } else if(!selectedEmployee.equals("0")){
          System.out.println("Nieprawidłowa nazwa pracownika. Spróbuj ponownie.");
        }
      }
  }

  private boolean validateUserInput(String selectedEmployee, HashSet<Employee> data) {
    boolean result = false;
    for (Employee emp : data){
      if(emp.getName().equals(selectedEmployee)){
        result = true;
        break;
      }
    }
    return result;
  }

  public static String getUserInput(String title){
    Scanner sc = new Scanner(System.in);
    System.out.println(title);
    String input;
    input = sc.nextLine();

    return input;
  }
}