package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.NoteDTO;
import com.udacity.jwdnd.course1.cloudstorage.services.INoteService;
import com.udacity.jwdnd.course1.cloudstorage.utils.CommonUtil;
import com.udacity.jwdnd.course1.cloudstorage.utils.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private INoteService noteService;

    @PostMapping("")
    public String createNote(@ModelAttribute NoteDTO noteDTO, RedirectAttributes redirectAttributes) {
        JSONObject jsonObject;
        if(CommonUtil.isEmpty(noteDTO.getNoteId()))
            jsonObject = noteService.saveNote(noteDTO);
        else
            jsonObject = noteService.updateNote(noteDTO);
        String message = jsonObject.get(Constants.MESSAGE).toString();
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, message);
        redirectAttributes.addFlashAttribute(Constants.STATUS, jsonObject.get(Constants.STATUS));
        return "redirect:/home";
    }
    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, RedirectAttributes redirectAttributes) {
        JSONObject jsonObject = noteService.deleteNote(noteId);
        String message = jsonObject.get(Constants.MESSAGE).toString();
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, message);
        redirectAttributes.addFlashAttribute(Constants.STATUS, jsonObject.get(Constants.STATUS));
        return "redirect:/home";
    }


}
