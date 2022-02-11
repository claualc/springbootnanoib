package com.nanoib.infra.http.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUsernameDTO {
    private Integer branch_id;
    private Integer id;
}
