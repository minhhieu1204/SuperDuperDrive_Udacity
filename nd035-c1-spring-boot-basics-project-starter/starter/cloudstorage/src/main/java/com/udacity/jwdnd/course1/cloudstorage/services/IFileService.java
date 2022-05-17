package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IFileService {
    JSONObject saveFile (HttpServletRequest request);
    List<FileDTO> getListFileByUser();

    void downloadFile (Integer fileId, HttpServletResponse response);
    JSONObject deleteFile (Integer fileId);
}
