package datamodel;

import java.util.ArrayList;
import java.util.HashSet;

public class DataStorage {
	private HashSet<Employee> employees;
	private HashSet<Project> projects;
	
//	private ArrayList<Employee> employee = new ArrayList<Employee>();
	
	
	
	public HashSet<Employee> getEmployees() {
		return employees;
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
