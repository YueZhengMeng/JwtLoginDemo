package com.shou.jwtlogindemo.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JwtResponseMessage  {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public  static String LoginSuccess(ResultEnum respCode, String jwtToken) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("jwtToken",jwtToken);
        map.put("code", respCode.getCode());
        map.put("message", respCode.getMessage());
        return objectMapper.writeValueAsString(map);
    }

    public  static String result(ResultEnum respCode) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("code", respCode.getCode());
        map.put("message", respCode.getMessage());
        return objectMapper.writeValueAsString(map);
    }

    public  static String deny(ResultEnum respCode,String cause) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("code", respCode.getCode());
        map.put("message", respCode.getMessage());
        map.put("cause", cause);
        return objectMapper.writeValueAsString(map);
    }

}
