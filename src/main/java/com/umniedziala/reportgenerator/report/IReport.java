package com.umniedziala.reportgenerator.report;

import com.umniedziala.reportgenerator.datamodel.Reports.ReportModel;
import com.umniedziala.reportgenerator.storage.DataStorage;

public interface IReport {

    ReportModel generateReport(DataStorage dataStorage);

}
