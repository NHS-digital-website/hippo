package uk.nhs.digital.externalstorage.modules;

import java.util.function.Function;

public class S3ConnectorServiceRegistrationModuleParams<T> {

    // AWS PARAMS

    static final S3ConnectorServiceRegistrationModuleParams<String> AWS_BUCKET = init(
        "externalstorage.aws.bucket", "s3Bucket", stringValue -> stringValue, ""
    );
    static final S3ConnectorServiceRegistrationModuleParams<String> AWS_REGION = init(
        "externalstorage.aws.region","s3Region", stringValue -> stringValue, ""
    );
    static final S3ConnectorServiceRegistrationModuleParams<String> AWS_S3_ENDPOINT = init(
        "externalstorage.aws.s3.endpoint", "s3Endpoint", stringValue -> stringValue, ""
    );

    // DOWNLOAD PARAMS

    static final S3ConnectorServiceRegistrationModuleParams<Integer> DOWNLOAD_MAX_CONC_COUNT = init(
        "externalstorage.download.maxConcurrentDownloads", "maxConcurrentDownloadsCount", Integer::valueOf, 0
    );
    static final S3ConnectorServiceRegistrationModuleParams<Long> DOWNLOAD_SHUTDOWN_TIMEOUT_IN_SECS = init(
        "externalstorage.download.shutdown.timeoutInSecs", "downloadShutdownTimeoutInSecs", Long::valueOf, 0L
    );

    // UPLOAD PARAMS

    static final S3ConnectorServiceRegistrationModuleParams<Integer> UPLOAD_MAX_CONC_COUNT = init(
        "externalstorage.upload.maxConcurrentUploads", "maxConcurrentUploadsCount", Integer::valueOf, 0
    );
    static final S3ConnectorServiceRegistrationModuleParams<Long> UPLOAD_SHUTDOWN_TIMEOUT_IN_SECS = init(
        "externalstorage.upload.shutdown.timeoutInSecs", "uploadShutdownTimeoutInSecs", Long::valueOf, 0L
    );

    private final String systemPropertyKey;
    private final String jcrModulePropertyKey;
    private final Function<String, T> fromStringConverter;
    private final T defaultValue;

    private S3ConnectorServiceRegistrationModuleParams(final String systemPropertyKey,
                                                       final String jcrModulePropertyKey,
                                                       final Function<String, T> fromStringConverter,
                                                       final T defaultValue
    ) {
        this.systemPropertyKey = systemPropertyKey;
        this.jcrModulePropertyKey = jcrModulePropertyKey;
        this.fromStringConverter = fromStringConverter;
        this.defaultValue = defaultValue;
    }

    public String getSystemPropertyKey() {
        return systemPropertyKey;
    }

    public String getJcrModulePropertyKey() {
        return jcrModulePropertyKey;
    }

    public T fromString(final String stringValue) {
        return stringValue == null ? defaultValue : fromStringConverter.apply(stringValue);
    }

    private static <T> S3ConnectorServiceRegistrationModuleParams<T> init(final String systemPropertyKey,
                                                                          final String jcrModulePropertyKey,
                                                                          final Function<String, T> fromStringConverter,
                                                                          final T defaultValue
    ) {
        return new S3ConnectorServiceRegistrationModuleParams<>(
            systemPropertyKey, jcrModulePropertyKey, fromStringConverter, defaultValue
        );
    }
}
