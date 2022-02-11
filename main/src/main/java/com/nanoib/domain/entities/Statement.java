package com.nanoib.domain.entities;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* transferencia */
@Data
@Entity
@AllArgsConstructor 
@NoArgsConstructor
public class Statement {

    public Integer accBranchId;
    public Integer accId;
    public String timestamp;
    public boolean in_out;
    public double value;
    public String comment;
    public Integer cptpAccBranchId;
    public Integer cptpAccId;
    public String cptpName;

}
