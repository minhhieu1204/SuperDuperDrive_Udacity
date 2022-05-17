package com.udacity.jwdnd.course1.cloudstorage.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperUtil{

    @Autowired
    private ModelMapper modelMapper;

    public <T, E> T map (E source, Class<T> destination){
        return modelMapper.map(source, destination);
    }
    public <T, E> List<T> mapToList (List<E> sources, Class<T> destination){
        return (List<T>) sources.stream().filter(source -> !CommonUtil.isEmpty(source)).map(source -> modelMapper.map(source, destination)).collect(Collectors.toList());
    }


}
