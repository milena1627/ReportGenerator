package datamodel;

import java.util.HashSet;

public class Project {
	
	private String name;
	private HashSet<Task> listOfTasks;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashSet<Task> getListOfTasks() {
		return listOfTasks;
	}
	public void setListOfTasks(HashSet<Task> listOfTasks) {
		this.listOfTasks = listOfTasks;
	}
	
	

}
