package com.udacity.jwdnd.course1.cloudstorage.utils;

import org.jetbrains.annotations.Contract;

import java.util.Optional;
import java.util.regex.Pattern;

public final class CommonUtil {
    @Contract(pure = true)
    public static final <T>  boolean isEmpty (Object object){
        if (object == null) {
            return true;
        }
        return false;
    }

    public static boolean validUsername (String username){
        Pattern pattern = Pattern.compile(Constants.USERNAME_PATTERN);
        return pattern.matcher(username).matches();
    }

    public static boolean validURL (String url){
        if((url.startsWith(Constants.HTTPS) || url.startsWith(Constants.HTTP)) && !url.contains(" ")){
            return true;
        }
        return false;
    }

}
