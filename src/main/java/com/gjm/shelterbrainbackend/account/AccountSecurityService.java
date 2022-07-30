package com.gjm.shelterbrainbackend.account;

public interface AccountSecurityService {
    long checkAndExtractAccountIdFromToken(String jwt);
    String generateTokenFromAccountId(long accountId);
}
