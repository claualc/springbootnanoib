package com.nanoib.infra.http.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewLoginDTO {
    private Integer branch_id;
    private Integer id;
    private String pwd;

    //unico que importa
    public String getCredentials() {
        return this.pwd;
    }

    public LoginUsernameDTO getDetails() {
        return new LoginUsernameDTO(this.branch_id, this.id);
    }

}
