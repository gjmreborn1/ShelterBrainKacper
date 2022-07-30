package com.gjm.shelterbrainbackend.account;

import java.util.List;

public interface AccountManagementService {
    void register(AccountDto accountDto);
    String login(LoginDto loginDto);
    Account getAccount(long accountId);
    List<Account> getAccounts();

    void checkIfAccountIsValid(String jwt);
    void checkIfAccountIsValid(long id);
}
