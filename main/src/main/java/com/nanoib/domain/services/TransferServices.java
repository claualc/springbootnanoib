package com.nanoib.domain.services;

import com.nanoib.domain.entities.TransferRequest;
import com.nanoib.domain.entities.Account;

public class TransferServices {

    public TransferRequest transfer(
        Account acc, TransferRequest transfer ) {
        
        /* logica de transacao */
        TransferRequest transfer_done = new TransferRequest(
            transfer.destAccBranchId, transfer.destAccId, transfer.value, transfer.comment);
        // user.accBranchId
        // user.accId
        // transfer.destAccBranchId
        // transfer.destAccId
        // transfer.value
        // transfer.comment

        return transfer_done;
    }
    
}
