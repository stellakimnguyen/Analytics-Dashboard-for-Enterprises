package com.soen390.erp.quality.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.soen390.erp.configuration.service.LogService;
import com.soen390.erp.email.model.EmailToSend;
import com.soen390.erp.email.service.EmailService;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QualityDataService {

    private String pathToQualityReport;
    private String qualityReportFilename;
    private File qualityDataReport;
    private String recipient;
    private String category;
    private final LogService logService;
    private final EmailService emailService;
    private String filePath;


    public QualityDataService(EmailService emailService,
                              LogService logService)
    {
        final String OS = System.getProperty("os.name");
        if (OS.startsWith("Windows")) {
            this.pathToQualityReport = this.pathToQualityReport = "\\src\\main\\resources\\quality\\";
        }
        else {
            String property = System.getProperty("file.separator");
            this.pathToQualityReport = String.format("%sbackend%ssrc%smain%sresources%squality%s", property, property, property, property, property, property);
        }
        this.qualityReportFilename = "quality-data-2021.csv";
        this.category = "quality data";
        this.recipient = "manager@gmail.com";
        this.logService = logService;
        this.emailService = emailService;
        this.filePath = new File("").getAbsolutePath();
        this.filePath =
                filePath.concat(pathToQualityReport + qualityReportFilename);
        this.qualityDataReport = new File(filePath);
    }

    /**
     * uses prset temp file, mocking production machinery sending report
     * @return
     */
    public File getQualityDataReport() throws IOException
    {
        return qualityDataReport;
    }

    /**
     * checks quality data for current month ie last row
     *
     * headers :
     * 0 Month,
     * 1 Manufacturing Cycle Time in days,
     * 2 Maintenance Costs $,
     * 3 Unit Costs $,
     * 4 Right First Time %,
     * 5 Defect Density %,
     * 6 Capacity Utilization %,
     * 7 Overall Equipment Effectiveness %,
     * 8 Production Volume
     */
    public void checkQualityDataCurrentMonth() throws IOException
    {
        System.out.println("Checking quality data ...........................");

        // read quality data report
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath));)
        {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (CsvValidationException e)
        {
            e.printStackTrace();
        }

        List<String> currentMonthRecord = records.get(records.size()-1);

        String maintenanceCosts = currentMonthRecord.get(2);
        String unitCosts = currentMonthRecord.get(3);
        String defectDensity = currentMonthRecord.get(5);

        String emailMessage = "";

        // check business rules
        if (Integer.parseInt(maintenanceCosts) > 5000)
        {
            String message = "Warning, maintenance costs exceed $5000.";
            emailMessage = emailMessage + message;
        }
        if (Integer.parseInt(unitCosts) > 160)
        {
            String message = "Warning, unit costs exceed $160.";
            emailMessage = emailMessage + " " + message;
        }
        if (Integer.parseInt(defectDensity) > 15)
        {
            String message = "Warning, defect density exceeds 15%.";
            emailMessage = emailMessage + " " + message;
        }

        if (!emailMessage.equals("")){
            EmailToSend email = EmailToSend.builder().to(recipient).subject(
                    "Quality Data Warning").body(emailMessage).build();
            emailService.sendMail(email);

            logService.addLog(emailMessage, category);
        }
    }
}
