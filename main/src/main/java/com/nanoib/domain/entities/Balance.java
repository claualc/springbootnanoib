package com.nanoib.domain.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor @NoArgsConstructor
/* saldo da conta */
public class Balance {
    
    /* primary key */
    Integer acc_branch_id;
    Integer acc_id;
    BigDecimal balance;

}
