package uk.nhs.digital.apispecs.services.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.Validate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.auth.ClientAuthenticationHandler;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Date;
import java.util.UUID;


public class ProxygenJwtClientAuthenticationHandler implements ClientAuthenticationHandler {
    private static final String CLIENT_ASSERTION_TYPE = "client_assertion_type";
    private static final String CLIENT_ASSERTION = "client_assertion";
    private static final String CLIENT_ASSERTION_TYPE_JWT = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";

    private final String privateKey;
    private final String clientId;
    private final String audUrl;

    public ProxygenJwtClientAuthenticationHandler(final String privateKey, String clientId, String audUrl) {
        this.privateKey = privateKey;
        this.clientId = clientId;
        this.audUrl = audUrl;

        ensureRequiredArgProvided("Proxygen Private Key", privateKey);
        ensureRequiredArgProvided("Proxygen Client ID", clientId);
        ensureRequiredArgProvided("Proxygen Audience URL", audUrl);
    }

    @Override
    public void authenticateTokenRequest(
        final OAuth2ProtectedResourceDetails resource,
        final MultiValueMap<String, String> form,
        final HttpHeaders headers
    ) {
        try {
            form.add(CLIENT_ASSERTION_TYPE, CLIENT_ASSERTION_TYPE_JWT);
            form.add(CLIENT_ASSERTION, generateJws(parsePrivateKey()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateJws(PrivateKey key) {
        return Jwts.builder()
            .setSubject(clientId)
            .setIssuer(clientId)
            .setId(String.valueOf(UUID.randomUUID()))
            .setAudience(audUrl)
            .setExpiration(new Date(System.currentTimeMillis() + 300 * 1000))
            .signWith(key, SignatureAlgorithm.RS512)
            .compact();
    }

    private PrivateKey parsePrivateKey() throws IOException {
        Security.addProvider(new BouncyCastleProvider());
        StringReader reader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(reader);
        PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
        if (pemKeyPair == null) {
            throw new IOException("Proxygen private key cannot be read.");
        }
        KeyPair kp = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
        pemParser.close();

        return kp.getPrivate();
    }

    private void ensureRequiredArgProvided(final String argName, final String argValue) {
        Validate.notBlank(argValue, "Required configuration argument is missing: %s", argName);
    }
}
