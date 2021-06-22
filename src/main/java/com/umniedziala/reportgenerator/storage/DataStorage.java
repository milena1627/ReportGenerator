package com.umniedziala.reportgenerator.storage;

import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Project;
import com.umniedziala.reportgenerator.datamodel.Task;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
public class DataStorage {
    private static DataStorage instance;
    private Set<String> availableYears;
    private HashSet<Employee> employees;
    private Map<String, HashSet<Employee>> employeesXD;

    public DataStorage() {
        employees = new HashSet<>();
        availableYears = new TreeSet<>();
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    // wczytywanie plików xls z podanej ściezki (także z podfolderów)

    public void loadFiles(String path) {

        File rootFolder = new File(path);
        File[] listOfFiles = rootFolder.listFiles();
        if (listOfFiles == null) {
            return;
        }
        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                if (file.getName().length() == 4 && StringUtils.isNumeric(file.getName())) {
                    availableYears.add(file.getName());
                }
                loadFiles(file.toString());
            } else if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx") ) {
                System.out.println("Starting processing file: " + file.getName());
                val workbook = loadWorkbook(file);
                val employeeName = FilenameUtils.removeExtension(file.getName()).replace("_", " ");
                val employee = employees.stream()
                    .filter(emp -> emp.getName().equals(employeeName))
                    .findFirst()
                    .orElse(new Employee(employeeName));
                val projects = new HashSet<Project>();
                for (int i = 0; i< Objects.requireNonNull(workbook).getNumberOfSheets(); i++) {
                    val sheet = workbook.getSheetAt(i);
                    val projectName = workbook.getSheetName(i);
                    val project = employee.getListOfProjects().stream()
                        .filter(prj -> prj.getName().equals(projectName))
                        .findFirst()
                        .orElse(new Project(projectName, new ArrayList<>()));
                    val taskSet = project.getListOfTasks();
                    for (int j=1; j<=sheet.getLastRowNum(); j++) {
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
                employees.add(employee);
            }
        }
    }
    public void printAll(){
        for (Employee employee : employees) {
            System.out.println(employee.getName());
            for (Project project : employee.getListOfProjects()){
                System.out.println(project.getName());
                for (Task task : project.getListOfTasks()){
                    System.out.println(task.getDescription() + " " + task.getDate().toString() + " " + task.getNumberOfHours());
                }
            }
        }
    }

    private static Workbook loadWorkbook(File file) {
            try {
                return WorkbookFactory.create(file);
            } catch (EncryptedDocumentException | IOException e) {
                e.printStackTrace();
                return null;
            }
    }
}


