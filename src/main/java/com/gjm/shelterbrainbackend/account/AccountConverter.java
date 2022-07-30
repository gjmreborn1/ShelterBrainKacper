package com.gjm.shelterbrainbackend.account;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AccountConverter {
    public Account accountDtoToAccount(AccountDto accountDto) {
        Account account = new Account();

        account.setName(accountDto.getName());
        account.setPassword(accountDto.getPassword());

        return account;
    }

    public AccountEntity accountToAccountEntity(Account account) {
        AccountEntity entity = new AccountEntity();

        entity.setName(account.getName());

        String password = account.getPassword();
        String bcryptedPassword = BCrypt.withDefaults()
                        .hashToString(12, password.toCharArray());
        entity.setPassword(bcryptedPassword);

        return entity;
    }

    public Account accountEntityToAccount(AccountEntity accountEntity) {
        Account account = new Account();

        account.setName(accountEntity.getName());

        // Can't decrypt with BCrypt
        account.setPassword(accountEntity.getPassword());

        return account;
    }

    public AccountDto accountToAccountDto(Account account) {
        // FIXME: not needed?
        return null;
    }
}
