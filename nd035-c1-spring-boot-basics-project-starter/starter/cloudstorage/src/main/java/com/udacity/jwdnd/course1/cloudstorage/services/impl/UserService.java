package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.HashService;
import com.udacity.jwdnd.course1.cloudstorage.services.IUserService;
import com.udacity.jwdnd.course1.cloudstorage.utils.CommonUtil;
import com.udacity.jwdnd.course1.cloudstorage.utils.Constants;
import com.udacity.jwdnd.course1.cloudstorage.utils.Message;
import com.udacity.jwdnd.course1.cloudstorage.utils.ResourceNotFoundException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private HashService hashService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject register(User user) {
        JSONObject jsonObject = new JSONObject();
        try{
            if(CommonUtil.isEmpty(user.getPassword()) || CommonUtil.isEmpty(user.getUsername()) ||
                    CommonUtil.isEmpty(user.getFirstName()) || CommonUtil.isEmpty(user.getLastName())){
                throw new ResourceNotFoundException(Message.USER_FIELD_EMPTY);
            }
            if(!CommonUtil.validUsername(user.getUsername())){
                throw new ResourceNotFoundException(Message.USERNAME_IN_USER_INVALID);
            }
            User userOld = userMapper.getUser(user.getUsername());
            if(!CommonUtil.isEmpty(userOld)){
                throw new ResourceNotFoundException(Message.USER_EXIST);
            }

            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            String encodedSalt = Base64.getEncoder().encodeToString(salt);
            user.setSalt(encodedSalt);

            String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
            user.setPassword(hashedPassword);

            Integer newUserId = userMapper.insert(user);
            if(CommonUtil.isEmpty(newUserId) || newUserId < 1){
                jsonObject.put(Constants.STATUS,Constants.STATUS_FAIL);
                jsonObject.put(Constants.MESSAGE,"Invalid information");
            }
            jsonObject.put(Constants.STATUS,Constants.STATUS_SUCCESS);
            return jsonObject;
        }catch (Exception e){
            logger.error(e.getMessage());
            jsonObject.put(Constants.STATUS,Constants.STATUS_FAIL);
            jsonObject.put(Constants.MESSAGE,e.getMessage());
            return jsonObject;
        }

    }
}
