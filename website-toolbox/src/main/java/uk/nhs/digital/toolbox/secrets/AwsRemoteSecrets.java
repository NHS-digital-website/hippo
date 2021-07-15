package uk.nhs.digital.toolbox.secrets;

import static java.lang.String.format;
import static java.lang.System.getProperty;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.ParameterNotFoundException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AwsRemoteSecrets implements RemoteSecrets {

    private static final Logger log = getLogger(AwsRemoteSecrets.class);
    private final Region region;

    private static final Properties properties = new Properties();

    {
        try {
            properties.load(new FileInputStream(getProperty("secure.properties.location") + "/aws-credentials.properties"));
        } catch (IOException e) {
            log.warn("The 'apigee-secrets.properties' file was not found.");
        }
    }

    public AwsRemoteSecrets(String region) {
        this.region = Region.of(region);
    }

    @Override
    public String getRemoteValue(String key) {
        AwsCredentialsProviderChain provider =  AwsCredentialsProviderChain.of(
            StaticCredentialsProvider.create(AwsBasicCredentials.create(properties.getProperty("accessKey"), properties.getProperty("secretKey"))),
            EnvironmentVariableCredentialsProvider.create() // used by localhost
        );

        try (SsmClient ssmClient = SsmClient.builder()
            .credentialsProvider(provider)
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
