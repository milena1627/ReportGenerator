import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Project;
import com.umniedziala.reportgenerator.datamodel.Reports.ReportModel;
import com.umniedziala.reportgenerator.datamodel.Task;
import com.umniedziala.reportgenerator.report.Report1;
import com.umniedziala.reportgenerator.storage.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ReportTest {

    private DataStorage dataStorage;
    private Employee employee;
    private Project project;

    @BeforeEach
    public void setUp() {
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
        assertEquals(employee.sumOfHours(), 20);
        assertEquals(employee.getListOfProjects().size(), 2);

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
        ReportModel reportModel = report1.generateReport(dataStorage);

        assertEquals(reportModel.getReportName(), "Report 1");
        LinkedList<ReportModel.Row> rows = reportModel.getRows();
        assertEquals(rows.size(), 2);
        LinkedList<ReportModel.Cell> cells = rows.get(0).getCellsInRow();
        assertEquals(cells.size(), 3);
    }

}
