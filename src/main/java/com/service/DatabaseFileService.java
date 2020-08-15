package com.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.customexceptions.FileNotFoundException;
import com.customexceptions.FileStorageException;
import com.dao.repository.DatabaseFileRepository;
import com.pojo.DatabaseFile;

@Service
public class DatabaseFileService {

    @Autowired
    private DatabaseFileRepository dao;

    public DatabaseFile storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println("enteres into databsefileservice stroefile method");

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            DatabaseFile dbFile = new DatabaseFile(fileName, file.getContentType(), file.getBytes());
            System.out.println("exit from databasefileservice storefile method");
            return dao.save(dbFile);
        } catch (IOException ex) {
        	
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
        
    }

    public DatabaseFile getFile(String fileId) {
    	System.out.println("enteres into getfile method");
        return dao.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
    }
}
