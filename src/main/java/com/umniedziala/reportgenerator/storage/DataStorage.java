package com.umniedziala.reportgenerator.storage;

import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Project;
import com.umniedziala.reportgenerator.datamodel.Task;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
public class DataStorage {
    private static DataStorage instance;

    private HashSet<Employee> employees;
    private HashSet<Project> projects;

//	private ArrayList<Employee> employee = new ArrayList<Employee>();

    private DataStorage() {
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

//    public ArrayList<Employee> getDataFromFiles() {
//
//        Employee employee1 = new Employee("Maria", "Kwiatek");
//        Employee employee2 = new Employee("Mariusz", "Kwiatkowski");
//
//        Task task = new Task((new Date()), "Nowy Task", 9.5);
//        employee1.add(task);
//        employee2.add(task);
//
//        ArrayList<Employee> employees = new ArrayList<Employee>();
//        employees.add(employee1);
//        employees.add(employee2);
//
//        return employees;
//    }

    // wczytywanie plików xls z podanej ściezki (także z podfolderów)

    public static ArrayList<File> loadFiles(String path) {

        File rootFolder = new File(path);
        File[] listOfFiles = rootFolder.listFiles();
        ArrayList<File> files = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                loadFiles(file.toString());
            } else {
                files.add(file);
                val workbook = loadWorkbook(file);
                val employeeName = FilenameUtils.removeExtension(file.getName()).split("_");
//                val employee = Employee.builder().surname(employeeName[0]).name(employeeName[1]);
                for (int i=0; i<workbook.getNumberOfSheets(); i++) {
                    val sheet = workbook.getSheetAt(i);
                    val project = new Project();
                    HashSet<Task> taskSet = new HashSet<>();
                    project.setName(workbook.getSheetName(i));
                    project.setListOfTasks(taskSet);
                    for (int j=2; j<sheet.getLastRowNum(); j++) {
                        val row = sheet.getRow(j);
                        val taskDate = DateUtil.getJavaDate(row.getCell(0).getNumericCellValue());
                        val taskName = row.getCell(1).getStringCellValue();
                        val duration = row.getCell(2).getNumericCellValue();
                        taskSet.add(new Task(taskDate, taskName, duration));
                    }
                }
                System.out.println(file.getName());
            }
        }
        return files;
    }

    public static Workbook loadWorkbook(File file) {
            try {
                return WorkbookFactory.create(file);
            } catch (EncryptedDocumentException | IOException e) {
                e.printStackTrace();
                return null;
            }
    }
}


