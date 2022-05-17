package com.udacity.jwdnd.course1.cloudstorage.services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface IUserService {

    JSONObject register (User user);
}
