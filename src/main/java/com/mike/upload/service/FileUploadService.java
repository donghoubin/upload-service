package com.mike.upload.service;

import com.mike.upload.model.UploadResponseInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description:
 * @Author: Mike Dong
 * @Date: 2019/12/3 9:51.
 */
public interface FileUploadService {

    UploadResponseInfo uploadFile(HttpServletRequest request, MultipartFile file) throws Exception;
}
