package com.umniedziala.reportgenerator.datamodel.Reports;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.CellType;

import java.util.LinkedList;

@Data
public class ReportModel {
  String reportName;
  LinkedList<Row> rows;

  @Data
  @AllArgsConstructor
  public static class Row {
    LinkedList<Cell> cellsInRow;
  }

  @Data
  @AllArgsConstructor
  public static class Cell {
    private String value;
    private CellType type;
  }
}
