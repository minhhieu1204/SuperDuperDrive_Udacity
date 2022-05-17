package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDTO;
import com.udacity.jwdnd.course1.cloudstorage.dto.NoteDTO;
import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.ICredentialService;
import com.udacity.jwdnd.course1.cloudstorage.utils.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CredentialService implements ICredentialService {

    private static final Logger logger = LoggerFactory.getLogger(CredentialService.class);

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CredentialMapper credentialMapper;

    @Override
    public JSONObject saveCredential(CredentialDTO credentialDTO) {
        JSONObject jsonObject = new JSONObject();
        try{

            if(!CommonUtil.validURL(credentialDTO.getUrl())){
                throw new ResourceNotFoundException(Message.URL_INVALID);
            }

            if(!CommonUtil.validUsername(credentialDTO.getUsername())){
                throw new ResourceNotFoundException(Message.USERNAME_INVALID);
            }

            if(CommonUtil.isEmpty(credentialDTO.getPassword()) || CommonUtil.isEmpty(credentialDTO.getUrl())
                    || CommonUtil.isEmpty(credentialDTO.getUsername())){
                throw new ResourceNotFoundException(Message.CREDENTIAL_INVALID);
            }


            User user = userMapper.getUser(userUtil.getUserCurrent());

            Credential credential = mapperUtil.map(credentialDTO, Credential.class);

            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            String encodedKey = Base64.getEncoder().encodeToString(key);
            credential.setKey(encodedKey);
            String encodePassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
            credential.setPassword(encodePassword);
            credential.setUserId(user.getUserId());
            Integer credentialId = credentialMapper.insert(credential);
            if(CommonUtil.isEmpty(credentialId) || credentialId < 0){
                throw new ResourceNotFoundException(Message.SAVE_CREDENTIAL_FAIL);
            }
            jsonObject.put(Constants.STATUS, Constants.STATUS_SUCCESS);
            jsonObject.put(Constants.MESSAGE, Message.CREDENTIAL_SAVE_SUCCESS);
        } catch (Exception e){
            logger.error(e.getMessage());
            jsonObject.put(Constants.STATUS, Constants.STATUS_FAIL);
            jsonObject.put(Constants.MESSAGE, e.getMessage());
        }
        return jsonObject;
    }

    @Override
    public JSONObject updateCredential(CredentialDTO credentialDTO) {
        JSONObject jsonObject = new JSONObject();
        try{

            if(CommonUtil.isEmpty(credentialDTO.getPassword()) || CommonUtil.isEmpty(credentialDTO.getUrl())
                    || CommonUtil.isEmpty(credentialDTO.getUsername())){
                throw new ResourceNotFoundException(Message.CREDENTIAL_INVALID);
            }

            if(!CommonUtil.validURL(credentialDTO.getUrl())){
                throw new ResourceNotFoundException(Message.URL_INVALID);
            }

            if(!CommonUtil.validUsername(credentialDTO.getUsername())){
                throw new ResourceNotFoundException(Message.USERNAME_INVALID);
            }

            User user = userMapper.getUser(userUtil.getUserCurrent());
            Credential credential = mapperUtil.map(credentialDTO, Credential.class);

            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            String encodedKey = Base64.getEncoder().encodeToString(key);
            credential.setKey(encodedKey);
            credential.setPassword(encryptionService.encryptValue(credential.getPassword(),encodedKey));
            credential.setUserId(user.getUserId());
            credentialMapper.update(credential);
            jsonObject.put(Constants.STATUS, Constants.STATUS_SUCCESS);
            jsonObject.put(Constants.MESSAGE, Message.CREDENTIAL_UPDATE_SUCCESS);
        } catch (Exception e){
            logger.error(e.getMessage());
            jsonObject.put(Constants.STATUS, Constants.STATUS_FAIL);
            jsonObject.put(Constants.MESSAGE, e.getMessage());
        }
        return jsonObject;
    }

    @Override
    public List<CredentialDTO> getListCredential() {
        try {
            User user = userMapper.getUser(userUtil.getUserCurrent());
            List<Credential> credentials = credentialMapper.findByUserId(user.getUserId());
            List<CredentialDTO> results = mapperUtil.mapToList(credentials, CredentialDTO.class);
            return results.stream().map(x -> {
                String rawPassword = encryptionService.decryptValue(x.getPassword(), x.getKey());
                x.setRawPassword(rawPassword);
                return x;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public JSONObject deleteCredential(Integer credentialId) {
        JSONObject jsonObject = new JSONObject();
        try {
            User user = userMapper.getUser(userUtil.getUserCurrent());
            Credential credential = credentialMapper.findByCredentialIdAndUserId(credentialId, user.getUserId());
            if (CommonUtil.isEmpty(credential)) {
                throw new ResourceNotFoundException(Message.CREDENTIAL_NOT_EXIST);
            }
            credentialMapper.delete(credentialId, user.getUserId());
            jsonObject.put(Constants.STATUS, Constants.STATUS_SUCCESS);
            jsonObject.put(Constants.MESSAGE, Message.CREDENTIAL_DELETE_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonObject.put(Constants.STATUS, Constants.STATUS_FAIL);
            jsonObject.put(Constants.MESSAGE, e.getMessage());
        }
        return jsonObject;
    }
}
