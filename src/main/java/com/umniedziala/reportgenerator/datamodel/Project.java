package com.umniedziala.reportgenerator.datamodel;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
@Getter
@Setter
public class Project {
	
	private String name;
	private HashSet<Task> listOfTasks;

}
