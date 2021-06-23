package com.umniedziala.reportgenerator.app;

import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Reports.ReportModel;
import com.umniedziala.reportgenerator.services.report.Report3;
import com.umniedziala.reportgenerator.services.report.IReport;
import com.umniedziala.reportgenerator.services.report.Report1;
import com.umniedziala.reportgenerator.services.report.Report2;
import com.umniedziala.reportgenerator.storage.DataStorage;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ReportGeneratorApp {

    private static final String REPORT_1 = "1 - Raport przepracowanych godzin dla pracowników";
    private static final String REPORT_2 = "2 - Raport kosztowności czasowej projektów";
    private static final String REPORT_3 = "3 - Szczegółowy wykaz pracy każdego pracownika";
    private static final String EXIT = "0 - Wyjście";
    private IReport report;

    public static void main(String[] args) {

        val availableOptions = Arrays.asList(REPORT_1, REPORT_2, REPORT_3, EXIT);
        Scanner scanner = new Scanner(System.in);
        ReportGeneratorApp reportGeneratorApp = new ReportGeneratorApp();
        System.out.println("RAPORT GENERATOR \n");
        System.out.println("Podaj ścieżkę do folderu zawierającego roczne zestawienia pracowników");
        val chooser = new JFileChooser();
        val absolutePath = reportGeneratorApp.choosePath(chooser, scanner);
        if (absolutePath == null) return;
        System.out.println("Wybrano ścieżkę: " + absolutePath);
        DataStorage storage = DataStorage.getInstance();
        storage.loadFiles(absolutePath);
//        storage.printAll();
        reportGeneratorApp.showMenu(availableOptions, scanner, storage);
        scanner.close();
    }

    private boolean generateRaport1(DataStorage storage, Scanner scanner) {
        Map<String, String> filters = new HashMap<>();
        val year = pickAvailableYears(storage.getAvailableYears(), scanner);
        filters.put("year", year);
        report = new Report1();
        val report1 = report.generateReport(storage, filters);
        printReport(report1);
        exportReport(report1, scanner);
        return true;
    }

    private boolean generateRaport2(DataStorage storage, Scanner scanner) {
        Map<String, String> filters = new HashMap<>();
        val year = pickAvailableYears(storage.getAvailableYears(), scanner);
        filters.put("year", year);
        report = new Report2();
        val report2 = report.generateReport(storage, filters);
        printReport(report2);
        exportReport(report2, scanner);
        return false;
    }

    private boolean generateRaport3(DataStorage storage, Scanner scanner) {
        report = new Report3();
        Map<String, String> filters = new HashMap<>();
        String employee = selectEmployee(storage);
        if (employee == null || employee.equals("0")){
            return false;
        }
        String year = pickAvailableYears(storage.getAvailableYears(), scanner);
        filters.put("employee", employee);
        filters.put("year", year);
        val report3 = report.generateReport(storage, filters);
        if(report3.getRows().size()>1){
            printReport(report3);
            exportReport(report3, scanner);
        }
        return false;
    }

    private String choosePath(JFileChooser chooser, Scanner scanner) {
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retval = chooser.showDialog(null, "Select");
        if (retval == 1) {
            System.out.println("Nie można przejść dalej bez podania ścieżki\n" +
                    "Chcesz wybrać jeszcze raz? [t-tak, n-nie]");
            var tryAgain = scanner.next().toLowerCase();
            while (!tryAgain.equals("n") && !tryAgain.equals("t")) {
                System.out.println("Wpisano błędną komendę, wpisz jeszcze raz");
                tryAgain = scanner.next().toLowerCase();
            }
            if (tryAgain.equals("t")) {
                choosePath(chooser, scanner);
            }
            return null;
        }
        return chooser.getSelectedFile().getAbsolutePath();
    }

    private void showMenu(List<String> availableOptions, Scanner scanner, DataStorage storage) {
        boolean finishWork = false;
        int action;
        while (!finishWork) {
            System.out.println("\nDostępne akcje:");
            availableOptions.forEach(System.out::println);
            System.out.print("Wybierz akcje: ");
            action = scanner.nextInt();
            switch (action) {
                case 1 -> generateRaport1(storage, scanner);
                case 2 -> generateRaport2(storage, scanner);
                case 3 -> generateRaport3(storage, scanner);
                case 0 -> finishWork = true;
                default -> System.out.println("Wybrana akcja nie inieje");
            }
        }
    }

    private void printReport(ReportModel reportModel) {
        for (val row : reportModel.getRows()) {
            val values = row.getCellsInRow().stream()
                    .map(ReportModel.Cell::getValue)
                    .collect(Collectors.toList());
            for (int i = 0; i < values.size(); i++) {
                System.out.printf("%s ", values.get(i));
            }
            System.out.printf("\n");
        }
    }

    private void exportReport(ReportModel reportModel, Scanner scanner) {

        System.out.println("Czy chcesz wyeksportować tabelę do pliku xlsx? [t-tak, n-nie]");
        var answer = scanner.next().toLowerCase();
        while (!answer.equals("n") && !answer.equals("t")) {
            System.out.println("Wpisano błędną komendę, wpisz jeszcze raz");
            answer = scanner.next().toLowerCase();
        }
        if (answer.equals("t")) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(reportModel.getReportName());
            String fileName = reportModel.getReportName() + ".xlsx";

            for (int i = 0; i < reportModel.getRows().size(); i++) {
                sheet.createRow(i);
                val values = reportModel.getRows().get(i).getCellsInRow();
                for (int j = 0; j < values.size(); j++) {
                    sheet.getRow(i).createCell(j).setCellType(values.get(j).getType());
                    sheet.getRow(i).createCell(j).setCellValue(values.get(j).getValue());
                }
            }

            try {
                OutputStream fileOut = new FileOutputStream(fileName);
                workbook.write(fileOut);
                System.out.println("Zapisano plik " + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nie zapisano pliku");
        }
    }

    private String pickAvailableYears(Set<String> years, Scanner scanner) {
        System.out.println("Wybierz rok dla którego chcesz pobrać raport, aby wyjść wpisz 0");
        years.forEach(System.out::println);
        var year = scanner.next();
        while (!years.contains(year) && !year.equals("0")) {
            System.out.println("Dane dla wybranego roku nie istnieją wybierz inną datę lub wpisz 0 aby wyjść");
            year = scanner.next();
        }
        if (year.equals("0")) {
            return null;
        }
        return year;
    }

    private String selectEmployee (DataStorage storage){
        String userSelection="";
        val data = storage.getEmployees();

        listEmployees(data);

        while (!userSelection.equals("0")) {
            userSelection = getUserInput("\nWpisz Imię i Nazwisko (0 - Wyjście):");
            if(userSelection.equals("0")){
                break;
            }
            String validationResult = validateUserInput(userSelection, data);
            if(validateUserInput(userSelection, data).equals("OK")){
                return userSelection;
            } else {
                System.out.println(validationResult);
            }
        }
        return null;
    }

    private void listEmployees(HashSet<Employee> data) {
        List<String> employees = data.stream()
                .map(Employee::getName)
                .sorted()
                .collect(Collectors.toList());

        System.out.println("\n********************+" +
                "\nPRACOWNICY:");
        employees.forEach(System.out::println);
    }

    private String validateUserInput(String selectedEmployee, HashSet<Employee> data) {
        for (Employee emp : data){
            if(emp.getName().equals(selectedEmployee)){
                return "OK";
            }
        }
        return " > Nieprawidłowa nazwa pracownika. Spróbuj ponownie.";
    }

    public static String getUserInput(String title){
        Scanner sc = new Scanner(System.in);
        System.out.println(title);
        String input;
        input = sc.nextLine();

        return input;
    }

}