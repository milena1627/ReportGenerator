package com.umniedziala.reportgenerator.datamodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class Project {
	
	private String name;
	private ArrayList<Task> listOfTasks;

	public void add(Task task) {
		listOfTasks.add(task);
	}

	public double getSumOfHours(String year) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		val startOfYear = formatter.parse("01.01." + year);
		val endOfYear = formatter.parse("31.12." + year);
		return listOfTasks.stream()
				.filter(task -> task.getDate().after(startOfYear) && task.getDate().before(endOfYear))
				.map(Task::getNumberOfHours)
				.mapToDouble(Double::doubleValue).sum();
	}

	public double getSumOfMonthlyProjectHoursInSpecificYear (String month, String year) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		val startOfYear = formatter.parse("01.01." + year);
		val endOfYear = formatter.parse("31.12." + year);

		List<Task> listOfTaskInYear = listOfTasks.stream()
				.filter(task -> task.getDate().after(startOfYear) && task.getDate().before(endOfYear))
				.collect(Collectors.toList());

		return listOfTaskInYear.stream()
				.filter(task -> task.getMonth().equals(month))
				 .map(Task::getNumberOfHours)
				 .mapToDouble(Double::doubleValue).sum();

	}
}
