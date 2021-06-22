import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Project;
import com.umniedziala.reportgenerator.datamodel.Reports.ReportModel;
import com.umniedziala.reportgenerator.datamodel.Task;
import com.umniedziala.reportgenerator.services.report.Report1;
import com.umniedziala.reportgenerator.services.report.Report3;
import com.umniedziala.reportgenerator.storage.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ReportTest {

    private DataStorage dataStorage;
    private Employee employee;
    private Project project;

    @BeforeEach
    public void setUp() {
        //dataStorage =  DataStorage.getInstance();
        dataStorage = new DataStorage();
        employee = null;
        project = null;
    }

    @Test
    public void shouldLoadFiles() {
        dataStorage.loadFiles("./src/test/resources/loadFilesTest");
        HashSet<Employee> employees = dataStorage.getEmployees();

        assertEquals(employees.size(), 1);

        Optional<Employee> first = employees.stream().findFirst();
        employee = first.get();

        assertEquals(employee.getName(), "test employee");
        assertEquals(employee.sumOfHours("2012"), 5);
        assertEquals(employee.getListOfProjects().size(), 1);

        Optional<Project> firstProject = employee.getListOfProjects().stream().findFirst();
        project = firstProject.get();

        assertEquals(project.getListOfTasks().size(), 2);
        Task task = project.getListOfTasks().get(0);
        assertEquals(task.getNumberOfHours(), 3);
    }

    @Test
    public void shouldGenerateReport1() {
        dataStorage.loadFiles("./src/test/resources/loadFilesTest");
        Report1 report1 = new Report1();
        ReportModel reportModel = report1.generateReport(dataStorage, "2012");

        assertEquals(reportModel.getReportName(), "Report 1");
        LinkedList<ReportModel.Row> rows = reportModel.getRows();
        assertEquals(rows.size(), 3);
        LinkedList<ReportModel.Cell> cells = rows.get(0).getCellsInRow();
        assertEquals(cells.size(), 3);
    }
    @Test
    public void shouldGenerateReport3() {

        Employee employee1 = new Employee("Szymon Batko");
        Task task1 = new Task(new Date(2021-1900,01,01), "task1", 2.0);
        Task task2 = new Task(new Date(2021-1900,02,28), "task2", 3.0);
        ArrayList<Task> listOfTasks = new ArrayList<>();
        listOfTasks.add(task1);
        listOfTasks.add(task2);
        Project project1 = new Project("Projekt1", listOfTasks);
        employee1.add(project1);
        HashSet<Employee> employees = new HashSet<>();
        employees.add(employee1);
       // dataStorage.setEmployees(employees);

        Report3 report3 = new Report3();
        ReportModel reportModel = report3.generateReport(dataStorage, "2012");

        assertEquals("Report 3", reportModel.getReportName());
        assertEquals("Miesiąc", reportModel.getRows().get(0).getCellsInRow().get(0).getValue());
        assertEquals("Projekt", reportModel.getRows().get(0).getCellsInRow().get(1).getValue());
        assertEquals("Godziny", reportModel.getRows().get(0).getCellsInRow().get(2).getValue());

        ReportModel.Cell cell = reportModel.getRows().get(1).getCellsInRow().get(0);
        assertEquals("1", cell.getValue());
        cell = reportModel.getRows().get(1).getCellsInRow().get(1);
        assertEquals("Projekt1", cell.getValue());
        cell =reportModel.getRows().get(1).getCellsInRow().get(2);
        assertEquals("2.0", cell.getValue());

    }

}
