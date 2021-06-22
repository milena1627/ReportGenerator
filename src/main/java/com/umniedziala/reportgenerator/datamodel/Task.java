package com.umniedziala.reportgenerator.datamodel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@Builder
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

	public int getMonth(){
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int month = localDate.getMonthValue();
		return month;
	}
}
