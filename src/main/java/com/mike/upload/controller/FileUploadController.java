package com.mike.upload.controller;

import com.mike.upload.model.UploadResponseInfo;
import com.mike.upload.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @Description:
 * @Author: Mike Dong
 * @Date: 2019/12/2 17:50.
 */
@Controller
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/uploadFile")
    public ResponseEntity<UploadResponseInfo> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                               RedirectAttributes redirectAttributes,
                                                               HttpServletRequest request) {
        UploadResponseInfo uploadResponseInfo = new UploadResponseInfo();
        try {
            uploadResponseInfo = fileUploadService.uploadFile(request, file);
            uploadResponseInfo.setResponseState("success");
        } catch (Exception e) {
            e.printStackTrace();
            uploadResponseInfo.setResponseState("fail");
        }
        return new ResponseEntity<>(uploadResponseInfo, HttpStatus.OK);
    }

    @GetMapping("/filedownload")
    public ResponseEntity<byte[]> getFile() {
        File file = new File("C:\\Users\\HouBinDong\\Desktop\\finaltask\\sample_stock_data_true.xlsx");
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();

            //  return ResponseEntity.status(404).body(null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(buffer);
    }
    }
