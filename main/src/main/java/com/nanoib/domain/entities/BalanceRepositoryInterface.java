package com.nanoib.domain.entities;

import java.util.Optional;

public interface BalanceRepositoryInterface {

    Optional<Balance> findByUser(Integer acc_branch_id, Integer acc_id);
    Boolean transferBalance(TransferRequest transferRequest, Integer origAccId, Integer origAccBranchId);
}
