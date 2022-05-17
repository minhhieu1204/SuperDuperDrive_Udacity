package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDTO;
import org.json.JSONObject;

import java.util.List;

public interface ICredentialService {

    JSONObject saveCredential (CredentialDTO credentialDTO);
    JSONObject updateCredential (CredentialDTO credentialDTO);
    List<CredentialDTO> getListCredential ();
    JSONObject deleteCredential (Integer credentialId);
}
