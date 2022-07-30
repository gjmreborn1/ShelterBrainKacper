package com.gjm.shelterbrainbackend.account;

import java.security.Key;
import com.gjm.shelterbrainbackend.security.NotAuthenticatedException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountSecurityServiceDbImpl implements AccountSecurityService {
    private final Key key;

    @Override
    public long checkAndExtractAccountIdFromToken(String jwt) {
        if(jwt == null) {
            throw new NotAuthenticatedException();
        }

        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject());
    }

    @Override
    public String generateTokenFromAccountId(long accountId) {
        return Jwts.builder()
                .setSubject(Long.toString(accountId))
                .signWith(key)
                .compact();
    }
}
