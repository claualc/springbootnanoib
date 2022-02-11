package com.nanoib.infra.http.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;

public class JwtUtil {

    @Data
    @AllArgsConstructor
    public static class SessionToken {
        RemoteTenant tenant;
        
        //These will be important later
        //String[] scopes;JSONObject
        //LocalDateTime issuedAt, expiresAt;


        static SessionToken fromJSONString(String jsonString) {
            try {
                JSONObject tokenStr = new JSONObject(jsonString);
                JSONObject tenStr = (JSONObject) tokenStr.get("ten");
                
                return new SessionToken(
                        new RemoteTenant(
                                tenStr.getInt("accBranchId"),
                                tenStr.getInt("accId"),
                                tenStr.getString("accHolderName")
                        )
                );
            } catch(JSONException e) {
                throw new RuntimeException("Token error",e);
            }
        }

        String toJsonString() {
            JSONObject jsonObj = new JSONObject();
            
            try {
                JSONObject tenJsonObj = new JSONObject();
                tenJsonObj.put("accBranchId", tenant.accBranchId);
                tenJsonObj.put("accId", tenant.accId);
                tenJsonObj.put("accHolderName", tenant.accHolderName);
                jsonObj.put("ten", tenJsonObj);
            } catch(JSONException e) {
                throw new RuntimeException("Token error",e);
            }
            
            return jsonObj.toString();
        }
    }

    private static Base64.Encoder b64Encoder = Base64.getEncoder().withoutPadding();
    private static Base64.Decoder b64Decoder = Base64.getDecoder();    
    //A regex to verify if the session token is well formed
    private static Pattern sessTokenPattern = Pattern.compile(
            "^([a-zA-Z0-9+\\/]*).([a-zA-Z0-9+\\/]+)$"
    );

    private static final byte[] MAC_KEY = new byte[] {
        (byte) 0xf2, (byte) 0x8c, (byte) 0xb5, (byte) 0xc7, (byte) 0x9f, (byte) 0x1c, 
        (byte) 0x97, (byte) 0xb0, (byte) 0x4f, (byte) 0xdc, (byte) 0x1f, (byte) 0x99,
        (byte) 0xe4, (byte) 0x22, (byte) 0x5f, (byte) 0xe8, (byte) 0x82, (byte) 0xb9,
        (byte) 0x1e, (byte) 0x4c, (byte) 0x93, (byte) 0xa2, (byte) 0x26, (byte) 0x0f,
        (byte) 0x26, (byte) 0x0b, (byte) 0x46, (byte) 0x5c, (byte) 0xec, (byte) 0x94,
        (byte) 0xa3, (byte) 0x0a 
    };    
    
    public static String issueSessionToken(RemoteTenant tenant) {
        byte[] b64EncodedContent = b64Encoder.encode(
                new SessionToken(tenant).toJsonString().getBytes()
        );
        
        byte[] b64EncodedMAC = b64Encoder.encode(
                calculateMAC(MAC_KEY, b64EncodedContent)
        );
        
        byte[] buffer = new byte[b64EncodedContent.length + b64EncodedMAC.length + 1];
        
        System.arraycopy(
                b64EncodedContent, 0,
                buffer, 0,
                b64EncodedContent.length
        );
        System.arraycopy(
                b64EncodedMAC, 0,
                buffer, b64EncodedContent.length + 1,
                b64EncodedMAC.length
        );
        
        buffer[b64EncodedContent.length] = (byte) '.';
        
        return new String(buffer);
    }

    public static String createToken(RemoteTenant tenant /* Account accoun */) {
        byte[] b64EncodedContent = b64Encoder.encode(
                new SessionToken(tenant).toJsonString().getBytes()
        );
        
        byte[] b64EncodedMAC = b64Encoder.encode(
                calculateMAC(MAC_KEY, b64EncodedContent)
        );
        
        byte[] buffer = new byte[b64EncodedContent.length + b64EncodedMAC.length + 1];
        
        System.arraycopy(
                b64EncodedContent, 0,
                buffer, 0,
                b64EncodedContent.length
        );
        System.arraycopy(
                b64EncodedMAC, 0,
                buffer, b64EncodedContent.length + 1,
                b64EncodedMAC.length
        );
        
        buffer[b64EncodedContent.length] = (byte) '.';
        
        return new String(buffer);
    }

    public static SessionToken verifyAndParseSessionToken(String sessTokenStr) {
        SessionToken result = null;
        
        Matcher m = sessTokenPattern.matcher(sessTokenStr);
        
        if (m.find()) {
            String b64EncodedContent = m.group(1);
            
            result = SessionToken.fromJSONString(
                    new String(b64Decoder.decode(b64EncodedContent)
                )
            );
            
            byte[] recMac = calculateMAC(MAC_KEY, b64EncodedContent.getBytes());
            
            byte[] macBytes = b64Decoder.decode(m.group(2));
            
            if (!Arrays.equals(recMac, macBytes))
                throw new RuntimeException();
        }

        return result;
    }

    private static byte[] calculateMAC(byte[] key, byte[] message) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return mac.doFinal(message);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Crypto error", e);
        }
    } 

    @Data
    @AllArgsConstructor
    public static class RemoteTenant {
        Integer accBranchId;
        Integer accId;
        String accHolderName;
    }

}
