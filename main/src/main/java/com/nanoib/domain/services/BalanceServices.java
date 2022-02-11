package com.nanoib.domain.services;

import java.util.Optional;

import com.nanoib.domain.entities.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceServices {

    @Autowired
    private BalanceRepositoryInterface rep;

    /* returns statements os especific user */
    public Optional<Balance> getByUser( 
        Integer accBranchIdUser, Integer accIdUser) {
        return rep.findByUser(accBranchIdUser, accIdUser);
    };

    public Boolean transferBalance(
        Integer origAccBranchId,
        Integer destAccBranchId,
        Integer origAccId,
        Integer destAccId,
        Float value,
        String comment
    ) {
        TransferRequest transferReq = new TransferRequest(
            destAccBranchId,
            destAccId,
            value,
            comment
        );

        return rep.transferBalance(transferReq, origAccId, origAccBranchId);
    }

}
