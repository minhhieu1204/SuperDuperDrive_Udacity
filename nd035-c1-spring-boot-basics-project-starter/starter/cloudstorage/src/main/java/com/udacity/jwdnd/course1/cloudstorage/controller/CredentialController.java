package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDTO;
import com.udacity.jwdnd.course1.cloudstorage.services.ICredentialService;
import com.udacity.jwdnd.course1.cloudstorage.utils.CommonUtil;
import com.udacity.jwdnd.course1.cloudstorage.utils.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    @Autowired
    private ICredentialService credentialService;

    @PostMapping("")
    public String createCredential (@ModelAttribute CredentialDTO credentialDTO, RedirectAttributes redirectAttributes){
        JSONObject jsonObject;
        if(CommonUtil.isEmpty(credentialDTO.getCredentialId())){
            jsonObject = credentialService.saveCredential(credentialDTO);
        }
        else{
            jsonObject = credentialService.updateCredential(credentialDTO);
        }
        String message = jsonObject.get(Constants.MESSAGE).toString();
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, message);
        redirectAttributes.addFlashAttribute(Constants.STATUS, jsonObject.get(Constants.STATUS));
        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteCredential (@PathVariable Integer id, RedirectAttributes redirectAttributes){
        JSONObject jsonObject = credentialService.deleteCredential(id);
        String message = jsonObject.get(Constants.MESSAGE).toString();
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, message);
        redirectAttributes.addFlashAttribute(Constants.STATUS, jsonObject.get(Constants.STATUS));
        return "redirect:/home";
    }
}
