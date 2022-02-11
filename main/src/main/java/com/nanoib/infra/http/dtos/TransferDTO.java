package com.nanoib.infra.http.dtos;

import lombok.Data;

@Data
public class TransferDTO {
    Integer destAccBranchId;
    Integer destAccId;
    Float value;
    String comment;
    
}
