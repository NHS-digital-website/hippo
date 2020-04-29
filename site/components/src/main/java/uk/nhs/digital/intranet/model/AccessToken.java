package uk.nhs.digital.intranet.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class AccessToken implements Serializable {

    public static final AccessToken EMPTY_ACCESS_TOKEN = new AccessToken("", "", 0);

    private final String token;
    private final String refreshToken;
    private final LocalDateTime expirationDate;

    public AccessToken(String token, String refreshToken, int expirationSeconds) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expirationDate = LocalDateTime.now().plusSeconds(expirationSeconds);
    }

    public AccessToken(String token, String refreshToken, LocalDateTime expirationDate) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessToken that = (AccessToken) o;
        return token.equals(that.token)
            && Objects.equals(refreshToken, that.refreshToken)
            && expirationDate.equals(that.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, refreshToken, expirationDate);
    }
}
