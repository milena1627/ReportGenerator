package com.umniedziala.reportgenerator.storage;

import com.umniedziala.reportgenerator.datamodel.Employee;
import com.umniedziala.reportgenerator.datamodel.Project;
import com.umniedziala.reportgenerator.datamodel.Task;
import lombok.Getter;
import lombok.Setter;

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

	private DataStorage() { }

	public static DataStorage getInstance() {
		if (instance == null) {
			instance = new DataStorage();
		}
		return instance;
	}
	
	public ArrayList<Employee> getDataFromFiles() {
		
		Employee employee1 = new Employee("Maria","Kwiatek");
		Employee employee2 = new Employee("Mariusz","Kwiatkowski");

		Task task = new Task((new Date()), "Nowy Task", 9.5 );
		employee1.add(task);
		employee2.add(task);

		ArrayList<Employee> employees = new ArrayList<Employee>();
		employees.add(employee1);
		employees.add(employee2);

		return employees;
	}
	
	
	
}
