package com.umniedziala.reportgenerator.storage;

import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Project;
import com.umniedziala.reportgenerator.datamodel.Task;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

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

    public ArrayList<Employee> getDataFromFiles() {

        Employee employee1 = new Employee("Maria", "Kwiatek");
        Employee employee2 = new Employee("Mariusz", "Kwiatkowski");

        Task task = new Task((new Date()), "Nowy Task", 9.5);
        employee1.add(task);
        employee2.add(task);

        ArrayList<Employee> employees = new ArrayList<Employee>();
        employees.add(employee1);
        employees.add(employee2);

        return employees;
    }

    // wczytywanie plików xls z podanej ściezki (także z podfolderów)

    public static ArrayList<File> loadFiles(String path) {
        File yearFolder = new File(path);
        File[] listOfMonthFolders = yearFolder.listFiles();
        ArrayList<File> files = new ArrayList<>();

        for (File file : listOfMonthFolders) {
            File[] listOfXmlFilesInMonthFolder = file.listFiles();
            for (File XmlFile : listOfXmlFilesInMonthFolder) {
                files.add(XmlFile);
            }
        }
        for (File file : files) {
            System.out.println(file.getName());
        }
        return files;
    }

    public static ArrayList<Workbook> loadWorkbooks(ArrayList<File> files) {
        ArrayList<Workbook> workbooks = new ArrayList<>();
        for (File file : files) {
            try {
                workbooks.add(WorkbookFactory.create(file));
            } catch (EncryptedDocumentException | IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return workbooks;
    }
}


