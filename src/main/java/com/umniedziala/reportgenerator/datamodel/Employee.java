package com.umniedziala.reportgenerator.datamodel;

import java.util.HashSet;

public class Employee {
	
	private String name;
	private String surname;
	private HashSet<Task> ListOfTasks;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public HashSet<Task> getListOfTasks() {
		return ListOfTasks;
	}
	public void setListOfTasks(HashSet<Task> listOfTasks) {
		ListOfTasks = listOfTasks;
	}
	
	

}
