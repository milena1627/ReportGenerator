import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Project;
import com.umniedziala.reportgenerator.datamodel.Reports.ReportModel;
import com.umniedziala.reportgenerator.datamodel.Task;
import com.umniedziala.reportgenerator.services.report.Report1;
import com.umniedziala.reportgenerator.services.report.Report2;
import com.umniedziala.reportgenerator.services.report.Report3;
import com.umniedziala.reportgenerator.storage.DataStorage;
import lombok.val;
import org.apache.poi.ss.usermodel.DateUtil;
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
        dataStorage.loadFiles("./src/test/resources");
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
        dataStorage.loadFiles("./src/test/resources");
        Report1 report1 = new Report1();

        Map<String, String> filters = new HashMap<>();
        val year = "1966";
        filters.put("year", year);

        ReportModel reportModel = report1.generateReport(dataStorage, filters);

        assertEquals("Report 1", reportModel.getReportName());
        LinkedList<ReportModel.Row> rows = reportModel.getRows();
        assertEquals(2, rows.size());
        LinkedList<ReportModel.Cell> cells = rows.get(0).getCellsInRow();
        assertEquals(3, cells.size());
    }

    @Test
    public void shouldGenerateHeaderRowForReport2() {

        Report2 report2 = new Report2();

        Map<String, String> filters = new HashMap<>();
        filters.put("year", "2021");

        ReportModel reportModel = report2.generateReport(dataStorage, filters);

        assertEquals("Report 2", reportModel.getReportName());
        assertEquals("L.p.", reportModel.getRows().get(0).getCellsInRow().get(0).getValue());
        assertEquals("Projekt", reportModel.getRows().get(0).getCellsInRow().get(1).getValue());
        assertEquals("Godziny", reportModel.getRows().get(0).getCellsInRow().get(2).getValue());

    }

    @Test
    public void shouldGenerateHeaderRowForReport3() {

        Report3 report3 = new Report3();

        Map<String, String> filters = new HashMap<>();
        filters.put("employee", "Szymon Batko");
        filters.put("year", "2021");

        ReportModel reportModel = report3.generateReport(dataStorage, filters);

        assertEquals("Report 3", reportModel.getReportName());
        assertEquals("Miesiąc", reportModel.getRows().get(0).getCellsInRow().get(0).getValue());
        assertEquals("Projekt", reportModel.getRows().get(0).getCellsInRow().get(1).getValue());
        assertEquals("Godziny", reportModel.getRows().get(0).getCellsInRow().get(2).getValue());

    }

    @Test
    public void shouldGenerateReport2ForOneTask() {
        Employee employee1 = new Employee("Aaa Bbb");
        Task task1 = new Task(new GregorianCalendar(2021, Calendar.JANUARY, 11).getTime(), "task1", 55.0);
        ArrayList<Task> listOfTasks = new ArrayList<>();
        listOfTasks.add(task1);
        Project project1 = new Project("Projekt1", listOfTasks);
        employee1.add(project1);
        HashSet<Employee> employees = new HashSet<>();
        employees.add(employee1);
        dataStorage.setEmployees(employees);

        Report2 report2 = new Report2();

        Set<String> availableYears = new TreeSet<>();
        availableYears.add("2021");

        Map<String, String> filters = new HashMap<>();
        filters.put("year", "2021");

        ReportModel reportModel = report2.generateReport(dataStorage, filters);

        ReportModel.Cell cell = reportModel.getRows().get(1).getCellsInRow().get(0);
        assertEquals("1", cell.getValue());
        cell = reportModel.getRows().get(1).getCellsInRow().get(1);
        assertEquals("Projekt1", cell.getValue());
        cell =reportModel.getRows().get(1).getCellsInRow().get(2);
        assertEquals("55.0", cell.getValue());

    }

    @Test
    public void shouldGenerateReport2ForTwoTasksInOneProject() {
        Employee employee1 = new Employee("Aaa Bbb");
        Task task1 = new Task(new GregorianCalendar(2021, Calendar.JANUARY, 11).getTime(), "task1", 55.0);
        Task task2 = new Task(new GregorianCalendar(2021, Calendar.MAY, 11).getTime(), "task5", 100.0);
        ArrayList<Task> listOfTasks = new ArrayList<>();
        listOfTasks.add(task1);
        listOfTasks.add(task2);
        Project project1 = new Project("Projekt1", listOfTasks);
        employee1.add(project1);
        HashSet<Employee> employees = new HashSet<>();
        employees.add(employee1);
        dataStorage.setEmployees(employees);

        Report2 report2 = new Report2();

        Set<String> availableYears = new TreeSet<>();
        availableYears.add("2021");

        Map<String, String> filters = new HashMap<>();
        filters.put("year", "2021");

        ReportModel reportModel = report2.generateReport(dataStorage, filters);

        ReportModel.Cell cell = reportModel.getRows().get(1).getCellsInRow().get(0);
        assertEquals("1", cell.getValue());
        cell = reportModel.getRows().get(1).getCellsInRow().get(1);
        assertEquals("Projekt1", cell.getValue());
        cell =reportModel.getRows().get(1).getCellsInRow().get(2);
        assertEquals("155.0", cell.getValue());

    }

    @Test
    public void shouldGenerateReport2ForTwoProjects() {
        Employee employee1 = new Employee("Aaa Bbb");
        Task task1 = new Task(new GregorianCalendar(2021, Calendar.JANUARY, 11).getTime(), "task1", 55.0);
        Task task21 = new Task(new GregorianCalendar(2021, Calendar.MAY, 11).getTime(), "task5", 100.0);
        Task task22 = new Task(new GregorianCalendar(2021, Calendar.DECEMBER, 11).getTime(), "task6", 20.0);

        ArrayList<Task> listOfTasks1 = new ArrayList<>();
        ArrayList<Task> listOfTasks2 = new ArrayList<>();
        listOfTasks1.add(task1);
        listOfTasks2.add(task21);
        listOfTasks2.add(task22);
        Project project1 = new Project("Projekt1", listOfTasks1);
        Project project2= new Project("ProjektX", listOfTasks2);
        employee1.add(project1);
        employee1.add(project2);
        HashSet<Employee> employees = new HashSet<>();
        employees.add(employee1);
        dataStorage.setEmployees(employees);

        Report2 report2 = new Report2();

        Set<String> availableYears = new TreeSet<>();
        availableYears.add("2021");

        Map<String, String> filters = new HashMap<>();
        filters.put("year", "2021");

        ReportModel reportModel = report2.generateReport(dataStorage, filters);

        ReportModel.Cell cell = reportModel.getRows().get(1).getCellsInRow().get(0);
        assertEquals("1", cell.getValue());
        cell = reportModel.getRows().get(1).getCellsInRow().get(1);
        assertEquals("Projekt1", cell.getValue());
        cell =reportModel.getRows().get(1).getCellsInRow().get(2);
        assertEquals("55.0", cell.getValue());

        cell = reportModel.getRows().get(2).getCellsInRow().get(0);
        assertEquals("2", cell.getValue());
        cell = reportModel.getRows().get(2).getCellsInRow().get(1);
        assertEquals("ProjektX", cell.getValue());
        cell =reportModel.getRows().get(2).getCellsInRow().get(2);
        assertEquals("120.0", cell.getValue());

    }

    @Test
    public void shouldGenerateReport2ForTwoTasksInOneProjectWhenOneTaskIsWrongYear() {
        Employee employee1 = new Employee("Aaa Bbb");
        Task task1 = new Task(new GregorianCalendar(2020, Calendar.JANUARY, 11).getTime(), "task1", 55.0);
        Task task2 = new Task(new GregorianCalendar(2021, Calendar.MAY, 11).getTime(), "task5", 100.0);
        ArrayList<Task> listOfTasks = new ArrayList<>();
        listOfTasks.add(task1);
        listOfTasks.add(task2);
        Project project1 = new Project("Projekt1", listOfTasks);
        employee1.add(project1);
        HashSet<Employee> employees = new HashSet<>();
        employees.add(employee1);
        dataStorage.setEmployees(employees);

        Report2 report2 = new Report2();

        Set<String> availableYears = new TreeSet<>();
        availableYears.add("2021");

        Map<String, String> filters = new HashMap<>();
        filters.put("year", "2021");

        ReportModel reportModel = report2.generateReport(dataStorage, filters);

        ReportModel.Cell cell = reportModel.getRows().get(1).getCellsInRow().get(0);
        assertEquals("1", cell.getValue());
        cell = reportModel.getRows().get(1).getCellsInRow().get(1);
        assertEquals("Projekt1", cell.getValue());
        cell =reportModel.getRows().get(1).getCellsInRow().get(2);
        assertEquals("100.0", cell.getValue());

    }

    @Test
    public void shouldGenerateReport3() {
        Employee employee1 = new Employee("Szymon Batko");
        Task task1 = new Task(new GregorianCalendar(2021, Calendar.JANUARY, 11).getTime(), "task1", 2.0);
        Task task2 = new Task(new GregorianCalendar(2021, Calendar.FEBRUARY, 11).getTime(), "task2", 5.0);
        ArrayList<Task> listOfTasks = new ArrayList<>();
        listOfTasks.add(task1);
        listOfTasks.add(task2);
        Project project1 = new Project("Projekt1", listOfTasks);
        employee1.add(project1);
        HashSet<Employee> employees = new HashSet<>();
        employees.add(employee1);
        dataStorage.setEmployees(employees);

        Report3 report3 = new Report3();

        Set<String> availableYears = new TreeSet<>();
        availableYears.add("2021");

        Map<String, String> filters = new HashMap<>();
        filters.put("employee", "Szymon Batko");
        filters.put("year", "2021");
//
//        Report3 report3 = new Report3();
//
//        Map<String, String> filters = new HashMap<>();
//        filters.put("employee", "test employee");
//        filters.put("year", "2012");


        ReportModel reportModel = report3.generateReport(dataStorage, filters);

        assertEquals("Report 3", reportModel.getReportName());
        assertEquals("Miesiąc", reportModel.getRows().get(0).getCellsInRow().get(0).getValue());
        assertEquals("Projekt", reportModel.getRows().get(0).getCellsInRow().get(1).getValue());
        assertEquals("Godziny", reportModel.getRows().get(0).getCellsInRow().get(2).getValue());

//        ReportModel.Cell cell = reportModel.getRows().get(1).getCellsInRow().get(0);
//        assertEquals("1", cell.getValue());
//        cell = reportModel.getRows().get(1).getCellsInRow().get(1);
//        assertEquals("Projekt1", cell.getValue());
//        cell =reportModel.getRows().get(1).getCellsInRow().get(2);
//        assertEquals("5.0", cell.getValue());

        ReportModel.Cell cell = reportModel.getRows().get(1).getCellsInRow().get(0);
        assertEquals("1", cell.getValue());
        cell = reportModel.getRows().get(1).getCellsInRow().get(1);
        assertEquals("Projekt1", cell.getValue());
        cell =reportModel.getRows().get(1).getCellsInRow().get(2);
        assertEquals("2.0", cell.getValue());

        cell = reportModel.getRows().get(2).getCellsInRow().get(0);
        assertEquals("2", cell.getValue());
        cell = reportModel.getRows().get(2).getCellsInRow().get(1);
        assertEquals("Projekt1", cell.getValue());
        cell =reportModel.getRows().get(2).getCellsInRow().get(2);
        assertEquals("5.0", cell.getValue());

    }


}
