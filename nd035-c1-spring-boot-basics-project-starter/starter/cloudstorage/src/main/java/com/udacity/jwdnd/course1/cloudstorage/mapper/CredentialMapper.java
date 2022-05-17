package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    Integer insert(Credential credential);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId} ORDER BY credentialid DESC")
    List<Credential> findByUserId(Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId} AND credentialid = #{credentialId}")
    Credential findByCredentialIdAndUserId(Integer credentialId, Integer userId);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, password = #{password}, key = #{key}  WHERE userid = #{userId} AND credentialid = #{credentialId}")
    void update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE userid = #{userId} AND credentialid = #{credentialId}")
    void delete(Integer credentialId, Integer userId);

}
