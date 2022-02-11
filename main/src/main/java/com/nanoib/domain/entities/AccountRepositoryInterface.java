package com.nanoib.domain.entities;

import java.util.List;

public interface AccountRepositoryInterface {
    Account  findByLogin(Integer branch_id, Integer id);
    List<Account> findAll();
}
