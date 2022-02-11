package com.nanoib.infra.http.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponseDTO {
    
    public long accBranchId;
    public long accId;
    public String accHolderName;
    public String sessToken;
    
    
    // public static LoginResponseBean fromJSONString(String jsonString) {
    //     JSONObject obj = new JSONObject(jsonString);
        
    //     return new LoginResponseBean(
    //             obj.getLong("accBranchId"),
    //             obj.getLong("accId"),
    //             obj.getString("accHolder"),
    //             obj.getString("sessToken")
    //     );
    // }
    
    public String toJson() throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        return m.writeValueAsString(this);
    }

    public String toJsonString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("accBranchId", accBranchId);
            obj.put("accId", accId);
            obj.put("accHolderName", accHolderName);
            obj.put("sessToken", sessToken);
        } catch(JSONException e) {
            throw new RuntimeException("Incorrect Login", e);
        }
        
        return obj.toString();
    }
    
}
