package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.IFileService;
import com.udacity.jwdnd.course1.cloudstorage.utils.*;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService implements IFileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject saveFile(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();

        try {
            if(!ServletFileUpload.isMultipartContent(request)) {
                throw new ResourceNotFoundException(Message.SAVE_FILE_FAIL);
            }

            ServletFileUpload upload = new ServletFileUpload();

            FileItemIterator iter;

            iter = upload.getItemIterator(request);

            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                User user = userMapper.getUser(userUtil.getUserCurrent());
                if (!item.isFormField()) {
                    InputStream fileStream = item.openStream();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    // Process the input stream
                    int length;
                    byte[] buffer = new byte[8192];
                    while ((length = fileStream.read(buffer, 0, buffer.length)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }

                    if (outputStream.size() == 0) {
                        throw new ResourceNotFoundException(Message.FILE_EMPTY);
                    }

                    if (outputStream.size() >= (10*1024*1024)) {
                        throw new ResourceNotFoundException(Message.FILE_MAXIMUM);
                    }

                    if (fileMapper.findByFileNameAndUserId(item.getName(), user.getUserId()) != null) {
                        throw new ResourceNotFoundException(Message.FILE_EXIST);
                    }

                    File file = new File();
                    file.setFileSize(String.valueOf(outputStream.size()));
                    file.setFileName(item.getName());
                    file.setContentType(item.getContentType());
                    file.setFileData(outputStream.toByteArray());
                    file.setUserId(user.getUserId());
                    Integer fileId = fileMapper.insert(file);

                    if (CommonUtil.isEmpty(fileId) || fileId < 0) {
                        throw new ResourceNotFoundException(Message.SAVE_FILE_FAIL);
                    }
                    jsonObject.put(Constants.STATUS, Constants.STATUS_SUCCESS);
                    jsonObject.put(Constants.MESSAGE, Message.FILE_SAVE_SUCCESS);
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonObject.put(Constants.STATUS, Constants.STATUS_FAIL);
            jsonObject.put(Constants.MESSAGE, e.getMessage());
        }
        return jsonObject;
    }

    @Override
    public List<FileDTO> getListFileByUser() {
        try {
            User user = userMapper.getUser(userUtil.getUserCurrent());
            List<File> files = fileMapper.findByUserId(user.getUserId());
            return mapperUtil.mapToList(files, FileDTO.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void downloadFile(Integer fileId, HttpServletResponse response) {
        try {
            User user = userMapper.getUser(userUtil.getUserCurrent());
            File file = fileMapper.findByFileIdAndUserId(fileId,user.getUserId());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename = "+file.getFileName();
            response.setHeader(headerKey, headerValue);
            response.setContentType(file.getContentType());
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(file.getFileData());
            outputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public JSONObject deleteFile(Integer fileId) {
        JSONObject jsonObject = new JSONObject();
        try {
            User user = userMapper.getUser(userUtil.getUserCurrent());
            File file = fileMapper.findByFileIdAndUserId(fileId,user.getUserId());
            if(CommonUtil.isEmpty(file)){
                throw new ResourceNotFoundException(Message.FILE_NOT_EXIST);
            }
            fileMapper.delete(fileId, user.getUserId());
            jsonObject.put(Constants.STATUS, Constants.STATUS_SUCCESS);
            jsonObject.put(Constants.MESSAGE, Message.FILE_DELETE_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonObject.put(Constants.STATUS, Constants.STATUS_FAIL);
            jsonObject.put(Constants.MESSAGE, e.getMessage());
        }
        return jsonObject;
    }
}
