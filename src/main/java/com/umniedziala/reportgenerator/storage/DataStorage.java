package com.umniedziala.reportgenerator.storage;

import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Project;
import com.umniedziala.reportgenerator.datamodel.Task;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.commons.codec.binary.StringUtils;
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

//	private ArrayList<Employee> employee = new ArrayList<Employee>();

    private DataStorage() {
        employees = new HashSet<>();
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

    public static void loadFiles(String path) {

        File rootFolder = new File(path);
        File[] listOfFiles = rootFolder.listFiles();
        if (listOfFiles == null) {
            return;
        }
        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                loadFiles(file.toString());
            } else {
                System.out.println("Starting processing file: " + file.getName());
                val workbook = loadWorkbook(file);
                val employeeName = FilenameUtils.removeExtension(file.getName()).split("_");
                val employee = new Employee(employeeName[1], employeeName[0]);
                val projects = new HashSet<Project>();
                for (int i = 0; i< Objects.requireNonNull(workbook).getNumberOfSheets(); i++) {
                    val sheet = workbook.getSheetAt(i);
                    val project = new Project();
                    HashSet<Task> taskSet = new HashSet<>();
                    project.setName(workbook.getSheetName(i));
                    project.setListOfTasks(taskSet);
                    for (int j=2; j<sheet.getLastRowNum(); j++) {
                        val row = sheet.getRow(j);
                        val firstCell = row.getCell(0);
                        if (!firstCell.getCellType().equals(CellType.NUMERIC) || firstCell.getNumericCellValue() == 0.0) {
                            continue;
                        }
                        val taskDate = DateUtil.getJavaDate(row.getCell(0).getNumericCellValue());
                        val taskName = row.getCell(1).getStringCellValue();
                        val duration = row.getCell(2).getNumericCellValue();
                        taskSet.add(new Task(taskDate, taskName, duration));
                    }
                    projects.add(project);
                }
                employee.setListOfProjects(projects);
            }
        }
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


