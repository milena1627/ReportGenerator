package datamodel;

import java.util.HashSet;

public class DataStorage {
	private HashSet<Employee> employees;
	private HashSet<Project> projects;
	
	
	public HashSet<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(HashSet<Employee> employees) {
		this.employees = employees;
	}
	public HashSet<Project> getProjects() {
		return projects;
	}
	public void setProjects(HashSet<Project> projects) {
		this.projects = projects;
	
	}
	
	public void getDataFromFiles() {
		
		
	}
	
	
	
}
