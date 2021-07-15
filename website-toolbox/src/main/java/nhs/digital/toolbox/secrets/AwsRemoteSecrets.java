package nhs.digital.toolbox.secrets;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.ParameterNotFoundException;

public class AwsRemoteSecrets implements RemoteSecrets {

    private static final Logger log = getLogger(AwsRemoteSecrets.class);
    private final Region region;

    public AwsRemoteSecrets(String region) {
        this.region = Region.of(region);
    }

    @Override
    public String getRemoteValue(String key) {
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
            log.warn(format("The remote parameter '%s' was not found", key));
            return null;
        }
    }

}
