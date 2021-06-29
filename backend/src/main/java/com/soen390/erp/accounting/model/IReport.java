package com.soen390.erp.accounting.model;

import com.soen390.erp.accounting.report.IReportGenerator;

import java.io.IOException;

public interface IReport {

    void accept(IReportGenerator reportGenerator) throws IOException;
}
