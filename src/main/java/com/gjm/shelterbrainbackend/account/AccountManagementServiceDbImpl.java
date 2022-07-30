package com.gjm.shelterbrainbackend.account;

import java.util.List;
import java.util.stream.Collectors;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.gjm.shelterbrainbackend.security.NotAuthenticatedException;
import com.gjm.shelterbrainbackend.security.ShelterBrainException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountManagementServiceDbImpl implements AccountManagementService {
    private final AccountRepository accountRepository;
    private final AccountSecurityService accountSecurityService;
    private final AccountConverter accountConverter;

    @Override
    public void register(AccountDto accountDto) {
        String name = accountDto.getName();

        if(accountRepository.existsByName(name)) {
            throw new ShelterBrainException(HttpStatus.CONFLICT, String.format("Pracownik \"%s\" jest juz zarejestrowany!", name));
        }

        Account account = accountConverter.accountDtoToAccount(accountDto);
        AccountEntity accountEntity = accountConverter.accountToAccountEntity(account);

        accountRepository.save(accountEntity);
    }

    @Override
    public String login(LoginDto loginDto) {
        String name = loginDto.getName();

        if(!accountRepository.existsByName(name)) {
            throw new ShelterBrainException(HttpStatus.NOT_FOUND, String.format("Pracownik \"%s\" nie istnieje!", name));
        }

        AccountEntity accountEntity = accountRepository.findByName(name);
        comparePasswords(loginDto.getPassword(), accountEntity.getPassword());
        return accountSecurityService.generateTokenFromAccountId(accountEntity.getId());
    }

    private void comparePasswords(String fromDto, String fromPersistence) {
        boolean comparisonResult = BCrypt.verifyer()
                .verify(fromDto.toCharArray(), fromPersistence)
                .verified;

        if(!comparisonResult) {
            throw new ShelterBrainException(HttpStatus.UNAUTHORIZED, "Zle haslo!");
        }
    }

    @Override
    public Account getAccount(long accountId) {
        AccountEntity accountEntity = accountRepository.findById(accountId).orElseThrow();

        return accountConverter.accountEntityToAccount(accountEntity);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(accountConverter::accountEntityToAccount)
                .collect(Collectors.toList());
    }

    @Override
    public void checkIfAccountIsValid(String jwt) {
        checkIfAccountIsValid(accountSecurityService.checkAndExtractAccountIdFromToken(jwt));
    }

    @Override
    public void checkIfAccountIsValid(long id) {
        if(!accountRepository.existsById(id)) {
            throw new NotAuthenticatedException();
        }
    }
}
