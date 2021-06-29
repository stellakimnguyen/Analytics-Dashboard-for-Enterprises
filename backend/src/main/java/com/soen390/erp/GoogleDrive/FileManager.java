package com.soen390.erp.GoogleDrive;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class FileManager {

    GoogleDriveManager googleDriveManager =
            new GoogleDriveManager();;

    /**
     * Simple file upload
     * @param file
     * @return
     */
    public String uploadFile(MultipartFile file)
    {
        try
        {
            if (null != file) {
                File fileMetadata = new File();
                fileMetadata.setName(file.getOriginalFilename());
                File uploadFile = googleDriveManager.getInstance()
                        .files()
                        .create(fileMetadata, new InputStreamContent(
                                file.getContentType(),
                                new ByteArrayInputStream(file.getBytes()))
                        )
                        .setFields("id")
                        .execute();
                return uploadFile.getId();
            }
        } catch (Exception e)
        {
            System.out.println("Error: " + e);
        }
        return null;
    }

    public void downloadFile(String id, OutputStream outputStream)
            throws IOException, GeneralSecurityException
    {
        if (id != null)
        {
            String fileId = id;
            googleDriveManager.getInstance().files().get(fileId)
                    .executeMediaAndDownloadTo(outputStream);
        }
    }

    public List<File> getFiles(int limit) throws GeneralSecurityException, IOException
    {
        FileList result = googleDriveManager.getInstance().files().list()
                .setPageSize(limit)
                .setFields("nextPageToken, files(id, name)")
                .execute();

         return result.getFiles();
    }

    public List<File> getAllFiles() throws GeneralSecurityException,
            IOException
    {
        FileList result = googleDriveManager.getInstance().files().list()
                .setFields("nextPageToken, files(id, name)")
                .execute();

        return result.getFiles();
    }

    public File getFile(String id) throws GeneralSecurityException,
            IOException
    {
        return  googleDriveManager.getInstance().files().get(id).execute();
    }

    public void deleteFile(String fileId) throws Exception
    {
        googleDriveManager.getInstance().files().delete(fileId).execute();
    }

    /**
     *  Print the names and IDs of files
     * @param files
     */
    public void printFilesNameId(List<File> files)
    {
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
    }

    public MultipartFile createMultipartFile(String pathname) throws IOException
    {
        java.io.File file = new java.io.File(pathname);
        FileInputStream inputStream = new FileInputStream(file);
        return new MockMultipartFile(file.getName(), file.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
    }


}
