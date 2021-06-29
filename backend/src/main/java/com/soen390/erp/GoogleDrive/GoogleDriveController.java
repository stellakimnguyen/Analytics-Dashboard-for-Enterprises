package com.soen390.erp.GoogleDrive;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.google.api.services.drive.model.File;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
public class GoogleDriveController {

    private final FileManager fileManager;

    public GoogleDriveController(FileManager fileManager)
    {
        this.fileManager = fileManager;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadSingleFile(@RequestBody MultipartFile file)
            throws IOException
    {
        String fileId = fileManager.uploadFile(file);

        if(fileId == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok("Uploaded FileId : "+ fileId + " " +
                "Successfully");
    }

    /**
     * example pathname : C:\\Users\\owner\\Downloads\\purchaseOrderReport.pdf
     * @param pathName
     * @return
     * @throws IOException
     */
//    @PostMapping(value = "/upload")
//    public ResponseEntity<String> uploadSingleFile(@RequestBody String pathName)
//            throws IOException
//    {
//        MultipartFile file = fileManager.createMultipartFile(pathName);
//
//        String fileId = fileManager.uploadFile(file);
//
//        if(fileId == null){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok("Uploaded FileId : "+ fileId + " " +
//                "Successfully");
//    }

    /**
     * for frontend : can loop through Files and call
     * file.getName(), file.getId() in dropdown
     * @param response
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    @GetMapping("/files")
    public ResponseEntity<?> getAllFiles(HttpServletResponse response)
            throws IOException, GeneralSecurityException
    {
        return ResponseEntity.ok().body(fileManager.getAllFiles());
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<?> getFile(@PathVariable String id,
                                     HttpServletResponse response)
            throws IOException, GeneralSecurityException
    {
        return ResponseEntity.ok().body(fileManager.getFile(id));
    }

    @GetMapping("/download/{id}")
    public void downloadReport(@PathVariable String id,
                        HttpServletResponse response)
            throws IOException, GeneralSecurityException
    {
        File f = fileManager.getFile(id);

        String headerKey = "Content-Disposition";
        String headerValue =
                "attachment; filename=" +  f.getName();
        response.setHeader(headerKey, headerValue);

        fileManager.downloadFile(id, response.getOutputStream());
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable String id)
            throws Exception
    {
        fileManager.deleteFile(id);
        return ResponseEntity.ok().body("File Deleted Successfully");
    }
}
