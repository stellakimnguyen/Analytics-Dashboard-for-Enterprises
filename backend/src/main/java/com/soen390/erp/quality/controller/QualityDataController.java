package com.soen390.erp.quality.controller;

import com.soen390.erp.quality.exception.QualityReportException;
import com.soen390.erp.quality.service.QualityDataService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;

@RestController
public class QualityDataController {

    String reportName = "quality-data-2021";
    private final QualityDataService qualityDataService;

    public QualityDataController(QualityDataService qualityDataService)
    {
        this.qualityDataService = qualityDataService;
    }

    @GetMapping(value = "/quality/report", produces = "text/csv")
    public ResponseEntity getQualityDataReport() {
        try {
            File file = qualityDataService.getQualityDataReport();

            qualityDataService.checkQualityDataCurrentMonth();

            return ResponseEntity.ok()
                    .header("Content-Disposition",
                            "attachment; filename=" + reportName + ".csv")
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(new FileSystemResource(file));

        } catch (QualityReportException | IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to generate report: " + reportName, ex);
        }
    }
}
