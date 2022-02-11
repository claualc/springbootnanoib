package com.nanoib.infra.http.dtos;

import com.nanoib.domain.entities.Account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private Integer branch_id;
    private Integer id;
    private String holders_name;
    private String token;
    private String login_id;
    private String pwd;

    //unico que importa
    public String getCredentials() {
        return this.pwd;
    }

     public void create(Account account, String token) {
         this.mapfromAccount(account);
         this.token = token;
     }

     public void mapfromAccount(Account acc) {
         this.branch_id = acc.getBranch_id();
         this.id = acc.getId();
         this.holders_name = acc.getHolders_name();
         this.login_id = this.branch_id + "." + this.id;
     }


     public void setId(Integer id) {
         this.login_id = this.branch_id+"."+id;
     }

     public Boolean isSameAuthId(Integer Id) {
         return this.login_id == Integer.toString(Id);
     }

    public String toJson() throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        return m.writeValueAsString(this);
    };
}
