package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File findByFileId(Integer fileId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId} ORDER BY fileid DESC")
    List<File> findByUserId(Integer userId);

    @Select("SELECT * FROM FILES WHERE  filename = #{fileName} AND userid = #{userId}")
    File findByFileNameAndUserId(String fileName, Integer userId);

    @Select("SELECT * FROM FILES WHERE  fileid = #{fileId} AND userid = #{userId}")
    File findByFileIdAndUserId(Integer fileId, Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insert(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId} AND userid = #{userId}")
    void delete(Integer fileId,  Integer userId);
}
