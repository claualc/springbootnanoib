package com.nanoib.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* requisição de transacao */
@Data
@AllArgsConstructor @NoArgsConstructor
public class TransferRequest {

    public long destAccBranchId;
    public long destAccId;
    public double value;
    public String comment;
};