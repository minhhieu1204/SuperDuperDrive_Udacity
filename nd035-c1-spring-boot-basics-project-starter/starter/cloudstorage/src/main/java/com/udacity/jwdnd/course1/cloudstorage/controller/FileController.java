package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDTO;
import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.dto.NoteDTO;
import com.udacity.jwdnd.course1.cloudstorage.services.ICredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.IFileService;
import com.udacity.jwdnd.course1.cloudstorage.services.INoteService;
import com.udacity.jwdnd.course1.cloudstorage.utils.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/home")
public class FileController {

    @Autowired
    private IFileService fileService;

    @Autowired
    private INoteService noteService;

    @Autowired
    private ICredentialService credentialService;

    @GetMapping("")
    public String showHome(@ModelAttribute("message") String message, @ModelAttribute("status") String status, Model model) {
        List<FileDTO> files = fileService.getListFileByUser();
        model.addAttribute("files", files);
        List<NoteDTO> notes = noteService.getListNoteByUserId();
        model.addAttribute("notes", notes);
        List<CredentialDTO> credentials = credentialService.getListCredential();
        model.addAttribute("credentials", credentials);
        return "home";
    }

    @PostMapping("/upload-file")
    public String uploadFile(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        JSONObject jsonObject = fileService.saveFile(request);
        String message = jsonObject.get(Constants.MESSAGE).toString();
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, message);
        redirectAttributes.addFlashAttribute(Constants.STATUS, jsonObject.get(Constants.STATUS));
        return "redirect:/home";
    }

    @GetMapping("/download/{fileId}")
    public void downloadFile(@PathVariable Integer fileId, HttpServletResponse response) {
        fileService.downloadFile(fileId, response);
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, RedirectAttributes redirectAttributes) {
        JSONObject jsonObject = fileService.deleteFile(fileId);
        String message = jsonObject.get(Constants.MESSAGE).toString();
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, message);
        redirectAttributes.addFlashAttribute(Constants.STATUS, jsonObject.get(Constants.STATUS));
        return "redirect:/home";
    }

}
