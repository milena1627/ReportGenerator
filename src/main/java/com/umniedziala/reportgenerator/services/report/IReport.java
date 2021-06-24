package com.umniedziala.reportgenerator.services.report;

import com.umniedziala.reportgenerator.datamodel.Reports.ReportModel;
import com.umniedziala.reportgenerator.storage.DataStorage;

import java.util.Map;

public interface IReport {

    ReportModel generateReport(DataStorage dataStorage, Map<String, String> filters);

}
