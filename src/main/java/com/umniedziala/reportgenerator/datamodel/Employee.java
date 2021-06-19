package com.umniedziala.reportgenerator.datamodel;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
public class Employee {
	
	private String name;
	private String surname;
	private HashSet<Task> listOfTasks;

	public Employee(String name, String surname) {
		super();
		this.name = name;
		this.surname = surname;
		this.listOfTasks = new HashSet<>();
	}
	
	public void add(Task task) {
		listOfTasks.add(task);
	}

}
