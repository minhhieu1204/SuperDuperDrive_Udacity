package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.dto.NoteDTO;
import org.json.JSONObject;

import java.util.List;

public interface INoteService {
    JSONObject saveNote (NoteDTO noteDTO);
    JSONObject updateNote (NoteDTO noteDTO);
    JSONObject deleteNote (Integer noteId);
    List<NoteDTO> getListNoteByUserId ();
}
