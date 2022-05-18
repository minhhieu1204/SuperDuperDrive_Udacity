package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.dto.NoteDTO;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.INoteService;
import com.udacity.jwdnd.course1.cloudstorage.utils.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService implements INoteService {

    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserUtil userUtil;

    @Override
    public JSONObject saveNote(NoteDTO noteDTO) {
        JSONObject jsonObject = new JSONObject();
        try {
            User user = userMapper.getUser(userUtil.getUserCurrent());

            if (CommonUtil.isEmpty(noteDTO.getNoteDescription()) || CommonUtil.isEmpty(noteDTO.getNoteTitle())) {
                throw new ResourceNotFoundException(Message.NOTE_INVALID);
            }

            if(CommonUtil.validLength(noteDTO.getNoteTitle(),20) ||
                    CommonUtil.validLength(noteDTO.getNoteDescription(),1000)){
                throw new ResourceNotFoundException(Message.FIELD_LENGTH_INVALID);
            }

            Note note = mapperUtil.map(noteDTO, Note.class);
            note.setUserId(user.getUserId());
            Integer noteId = noteMapper.insert(note);
            if (CommonUtil.isEmpty(noteId) || noteId < 1) {
                throw new ResourceNotFoundException(Message.SAVE_NOTE_FAIL);
            }
            jsonObject.put(Constants.STATUS, Constants.STATUS_SUCCESS);
            jsonObject.put(Constants.MESSAGE, Message.NOTE_SAVE_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonObject.put(Constants.STATUS, Constants.STATUS_FAIL);
            jsonObject.put(Constants.MESSAGE, e.getMessage());
        }
        return jsonObject;
    }

    @Override
    public JSONObject updateNote(NoteDTO noteDTO) {
        JSONObject jsonObject = new JSONObject();
        try {
            User user = userMapper.getUser(userUtil.getUserCurrent());

            if (CommonUtil.isEmpty(noteDTO.getNoteDescription()) || CommonUtil.isEmpty(noteDTO.getNoteTitle())
                    || CommonUtil.isEmpty(noteDTO.getNoteId())) {
                throw new ResourceNotFoundException(Message.NOTE_INVALID);
            }

            Note note = mapperUtil.map(noteDTO, Note.class);
            note.setUserId(user.getUserId());
            noteMapper.update(note);
            jsonObject.put(Constants.STATUS, Constants.STATUS_SUCCESS);
            jsonObject.put(Constants.MESSAGE, Message.NOTE_UPDATE_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonObject.put(Constants.STATUS, Constants.STATUS_FAIL);
            jsonObject.put(Constants.MESSAGE, e.getMessage());
        }
        return jsonObject;
    }

    @Override
    public JSONObject deleteNote(Integer noteId) {
        JSONObject jsonObject = new JSONObject();
        try {
            User user = userMapper.getUser(userUtil.getUserCurrent());
            Note note = noteMapper.findByNoteIdAndUserId(noteId, user.getUserId());
            if (CommonUtil.isEmpty(note)) {
                throw new ResourceNotFoundException(Message.NOTE_NOT_EXIST);
            }
            noteMapper.delete(noteId, user.getUserId());
            jsonObject.put(Constants.STATUS, Constants.STATUS_SUCCESS);
            jsonObject.put(Constants.MESSAGE, Message.NOTE_DELETE_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonObject.put(Constants.STATUS, Constants.STATUS_FAIL);
            jsonObject.put(Constants.MESSAGE, e.getMessage());
        }
        return jsonObject;
    }

    @Override
    public List<NoteDTO> getListNoteByUserId() {
        try {
            User user = userMapper.getUser(userUtil.getUserCurrent());
            List<Note> notes = noteMapper.findByUserId(user.getUserId());
            return mapperUtil.mapToList(notes, NoteDTO.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
