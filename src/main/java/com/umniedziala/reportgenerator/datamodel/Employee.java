package com.umniedziala.reportgenerator.datamodel;

import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Objects;

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

	public double sumOfHours(String year) {
		return listOfProjects.stream()
				.map(project -> {
					try {
						return project.getSumOfHours(year);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return null;
				})
				.filter(Objects::nonNull)
				.mapToDouble(Double::doubleValue).sum();
	}

}
