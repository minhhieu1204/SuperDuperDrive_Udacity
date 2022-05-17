package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    Integer insert(Note note);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId} ORDER BY noteid DESC")
    List<Note> findByUserId(Integer userId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId} AND noteid = #{noteId} ")
    Note findByNoteIdAndUserId(Integer noteId, Integer userId);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription}  WHERE userid = #{userId} AND noteid = #{noteId}")
    void update(Note note);

    @Delete("DELETE FROM NOTES WHERE userid = #{userId} AND noteid = #{noteId}")
    void delete(Integer noteId, Integer userId);
}
