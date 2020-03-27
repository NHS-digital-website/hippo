package uk.nhs.digital.intranet.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AccessToken implements Serializable {

    private final String token;
    private final String refreshToken;
    private final LocalDateTime expirationDate;

    public AccessToken(String token, String refreshToken, int expirationSeconds) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expirationDate = LocalDateTime.now().plusSeconds(expirationSeconds);
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }
}
