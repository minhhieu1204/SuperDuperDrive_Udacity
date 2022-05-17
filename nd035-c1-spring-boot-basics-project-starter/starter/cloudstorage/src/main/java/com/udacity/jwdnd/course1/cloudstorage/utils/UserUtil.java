package com.udacity.jwdnd.course1.cloudstorage.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserUtil {

    private static final Logger logger = LoggerFactory.getLogger(UserUtil.class);
    public String getUserCurrent() {
        String username = null;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        } catch (Exception e){
            logger.error(e.getMessage());
        }
        return username;
    }


}
