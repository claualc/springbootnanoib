package com.nanoib.domain.entities;

import java.util.List;

public interface StatementRepositoryInterface {
    List<Statement> getByUser(Integer accBranchId, Integer accId);
}
