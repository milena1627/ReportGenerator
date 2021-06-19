package datamodel;

import java.util.Date;

public class Task {
	
	private Date date;
	private String description;
	private Double numberOfHours;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getNumberOfHours() {
		return numberOfHours;
	}
	public void setNumberOfHours(Double numberOfHours) {
		this.numberOfHours = numberOfHours;
	}
	
	

	
}
