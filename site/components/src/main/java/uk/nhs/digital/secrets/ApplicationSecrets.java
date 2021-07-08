package uk.nhs.digital.secrets;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.ParameterNotFoundException;

import java.util.Map;

public class ApplicationSecrets {

    private final Region region;
    private Map<String, String> cache;

    public ApplicationSecrets(Map<String, String> cache, String region) {
        this.cache = cache;
        this.region = Region.of(region);
    }

    public String getValue(String key) {
        if (!this.cache.containsKey(key)) {
            this.cache.put(key, getRemoteValue(key));
        }
        return this.cache.get(key);
    }

    private String getRemoteValue(String key) {
        try (SsmClient ssmClient = SsmClient.builder()
            .region(this.region)
            .build()) {
            GetParameterRequest parameterRequest = GetParameterRequest.builder()
                .name(key)
                .withDecryption(true)
                .build();
            GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);
            return parameterResponse.parameter().value();
        } catch (ParameterNotFoundException e) {
            //log.warn(format("The parameter '%s' was not found", key));
            return null;
        }
    }

}
