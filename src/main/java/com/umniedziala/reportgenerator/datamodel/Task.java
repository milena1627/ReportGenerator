package com.umniedziala.reportgenerator.datamodel;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Task {
	
	private Date date;
	private String description;
	private Double numberOfHours;
	
	
	public Task(Date date, String description, Double numberOfHours) {
		super();
		this.date = date;
		this.description = description;
		this.numberOfHours = numberOfHours;
	}
}
