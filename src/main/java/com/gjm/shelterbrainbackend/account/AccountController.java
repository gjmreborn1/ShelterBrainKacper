package com.gjm.shelterbrainbackend.account;

import static com.gjm.shelterbrainbackend.account.AccountController.ACCOUNT_ENDPOINT;
import com.gjm.shelterbrainbackend.security.ShelterBeanValidationException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ACCOUNT_ENDPOINT)
@CrossOrigin
@RequiredArgsConstructor
public class AccountController {
    public static final String ACCOUNT_ENDPOINT = "/api/account";

    private final AccountManagementService accountManagementService;
    private final AccountSecurityService accountSecurityService;

    @PostMapping
    public void register(@RequestBody @Valid AccountDto accountDto, BindingResult validationResult) {
        if(validationResult.hasErrors()) {
            throw new ShelterBeanValidationException(validationResult);
        }

        accountManagementService.register(accountDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginDto loginDto, BindingResult validationResult) {
        if(validationResult.hasErrors()) {
            throw new ShelterBeanValidationException(validationResult);
        }

        String jwt = accountManagementService.login(loginDto);

        HttpHeaders httpHeaders = createJwtResponseHeaders(jwt);
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    @GetMapping
    public Account getAccount(@RequestHeader(value = "x-auth", required = false) String jwt) {
        long accountId = accountSecurityService.checkAndExtractAccountIdFromToken(jwt);

        return accountManagementService.getAccount(accountId);
    }

    private HttpHeaders createJwtResponseHeaders(String jwt) {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Access-Control-Expose-Headers", "x-auth");
        headers.set("x-auth", jwt);

        return headers;
    }
}
