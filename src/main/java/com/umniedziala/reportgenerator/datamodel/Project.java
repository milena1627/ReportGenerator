package com.umniedziala.reportgenerator.datamodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
@AllArgsConstructor
public class Project {
	
	private String name;
	private ArrayList<Task> listOfTasks;

	public void add(Task task) {
		listOfTasks.add(task);
	}

	public double getSumOfHours() {
		return listOfTasks.stream()
				.map(Task::getNumberOfHours)
				.mapToDouble(Double::doubleValue).sum();
	}

}
