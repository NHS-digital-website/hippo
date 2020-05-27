package uk.nhs.digital.apispecs.apigee;

public class ApigeeClientConfig {

    private String allSpecUrl;
    private String singleSpecUrl;
    private String tokenUrl;
    private String username;
    private String password;
    private String basicToken;
    private String otpKey;


    public ApigeeClientConfig(String allSpecUrl, String singleSpecUrl, String tokenUrl, String username, String password, String basicToken, String otpKey) {
        this.allSpecUrl = allSpecUrl;
        this.singleSpecUrl = singleSpecUrl;
        this.tokenUrl = tokenUrl;
        this.username = username;
        this.password = password;
        this.basicToken = basicToken;
        this.otpKey = otpKey;
    }

    public String getAllSpecUrl() {
        return allSpecUrl;
    }

    public String getSingleSpecUrl() {
        return singleSpecUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBasicToken() {
        return basicToken;
    }

    public String getOtpKey() {
        return otpKey;
    }

}
