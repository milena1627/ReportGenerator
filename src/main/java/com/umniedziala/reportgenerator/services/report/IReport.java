package com.umniedziala.reportgenerator.services.report;

import com.umniedziala.reportgenerator.datamodel.Reports.ReportModel;
import com.umniedziala.reportgenerator.storage.DataStorage;

public interface IReport {

    ReportModel generateReport(DataStorage dataStoragem, String filter);

}
