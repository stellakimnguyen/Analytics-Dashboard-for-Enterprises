package com.soen390.erp.accounting.report;

import com.google.api.services.drive.model.File;
import com.soen390.erp.GoogleDrive.FileManager;
import com.soen390.erp.GoogleDrive.GoogleDriveController;
import com.soen390.erp.GoogleDrive.GoogleDriveManager;

import java.util.List;

/**
 * For Google Drive Testing Purposes Only
 */
public class FileDriver {

    private static GoogleDriveManager googleDriveManager =
            new GoogleDriveManager();

    private static FileManager fileManager =
            new FileManager();

    private static GoogleDriveController controller =
            new GoogleDriveController(fileManager);

    public static void main(String... args) throws Exception
    {
        // Simple file upload, requires path
//        String pathname = "C:\\Users\\owner\\Downloads" +
//                "/ledgersReport.pdf";
//        MultipartFile multipartFile = fileManager.createMultipartFile(pathname);
//        fileManager.uploadFile(multipartFile);



        // download file
        // for frontend, have a drop down listing all filenames, then
        // whichever is selected will use its id
//        String id = "1DxyfZ3i7EKmzb5f5D0L8CAXgm9Nrm0FO"; // pdf
//        String id = "1pGuGXELs9J5-djoaC4-g0k8PfQxPd6n6"; // csv
//        OutputStream outputStream = new ByteArrayOutputStream();
//        fileManager.downloadFile(fileId, outputStream);



        // get a file
//        File f = fileManager.getFile(id);



        // delete a file
//        fileManager.deleteFile("1KNXAah-7i0W2Nof9Ew7-b2tJwb9fgd1c");



        // Print the names and IDs for up to 10 files.
        List<File> files = fileManager.getFiles(10);
        fileManager.printFilesNameId(files);
    }
}
