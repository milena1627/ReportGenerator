package com.umniedziala.reportgenerator.datamodel;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
public class Employee {
	
	private String name;
	private HashSet<Project> listOfProjects;

	public Employee(String name) {
		this.name = name;
		this.listOfProjects = new HashSet<>();
	}
	
	public void add(Project project) {
		listOfProjects.add(project);
	}

	public double sumOfHours() {
		return listOfProjects.stream()
				.map(Project::getSumOfHours)
				.mapToDouble(Double::doubleValue).sum();
	}

}
