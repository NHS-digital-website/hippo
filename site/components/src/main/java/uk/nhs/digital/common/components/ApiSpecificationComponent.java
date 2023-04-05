package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

import java.util.Arrays;
import java.util.Optional;

public class ApiSpecificationComponent extends ContentRewriterComponent {

    @Override
    public void doBeforeRender(final HstRequest request,
                               final HstResponse response) {
        super.doBeforeRender(request, response);

        String environmentName = System.getProperty("brc.environmentname") != null
            ? System.getProperty("brc.environmentname") :
            System.getProperty("hippo.environment");

        String[] devEnvironments =
            {"local-dev", "tst", "test", "development", "dev", "training", "cs"};

        Boolean isDevEnv = Arrays.asList(devEnvironments).contains(environmentName);

        Optional.ofNullable(request.getRequestContext().getContentBean())
            .ifPresent(document -> {
                request.setAttribute("document", document);
                request.setAttribute("path", request.getPathInfo());
                request.setAttribute("isDevEnv", isDevEnv);
            });
    }
}
