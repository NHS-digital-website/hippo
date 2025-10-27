package uk.nhs.digital.apispecs.services.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.Validate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ProxygenJwtTokenProvider {

    private static final String CLIENT_ASSERTION_TYPE = "client_assertion_type";
    private static final String CLIENT_ASSERTION = "client_assertion";
    private static final String CLIENT_ASSERTION_TYPE_JWT = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";

    private final String privateKey;
    private final String clientId;
    private final String audUrl;
    private final String kid;

    private final RestTemplate restTemplate;

    public ProxygenJwtTokenProvider(final String privateKey, String clientId, String audUrl, String kid,
                                    RestTemplate restTemplate) {
        this.privateKey = privateKey;
        this.clientId = clientId;
        this.audUrl = audUrl;
        this.kid = kid;
        this.restTemplate = restTemplate;

        ensureRequiredArgProvided("Proxygen Private Key", privateKey);
        ensureRequiredArgProvided("Proxygen Client ID", clientId);
        ensureRequiredArgProvided("Proxygen Audience URL", audUrl);
        ensureRequiredArgProvided("Proxygen Auth KID", kid);
    }

    public String getAccessToken(String tokenUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, java.nio.charset.StandardCharsets.UTF_8));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add(CLIENT_ASSERTION_TYPE, CLIENT_ASSERTION_TYPE_JWT);
        try {
            body.add(CLIENT_ASSERTION, generateJws(parsePrivateKey()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse private key");
        }

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, Map.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        }

        throw new RuntimeException("Failed to retrieve access token: " + response.getStatusCode());
    }

    private String generateJws(PrivateKey key) {
        return Jwts.builder()
            .setSubject(clientId)
            .setIssuer(clientId)
            .setHeaderParam("kid", kid)
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


