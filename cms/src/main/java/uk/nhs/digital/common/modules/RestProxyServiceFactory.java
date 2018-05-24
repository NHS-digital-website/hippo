package uk.nhs.digital.common.modules;

import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.lang.*;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.hippoecm.frontend.service.restproxy.RestProxyServicePlugin;
import org.hippoecm.repository.util.JcrUtils;
import org.slf4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class RestProxyServiceFactory {

    private static final Logger log = getLogger(RestProxyServiceFactory.class);

    public static final String HST_REST_PROXY_SERVICE_LOCATION = "/hippo:configuration/hippo:frontend/cms/cms-services/hstRestProxyService";

    public static <T> T createRestProxy(final Class<T> restServiceApiClass, final Session session) {

        final String serviceBaseUrl = getHstCmsServiceBaseUrl(session);

        //using the 'internal' hostgroup explicitly defined in the hst:hosts
        log.debug("Creating proxy using this url {}", serviceBaseUrl + "restservices");
        return JAXRSClientFactory.create(serviceBaseUrl + "restservices", restServiceApiClass);
    }

    public static <T> T createRestProxy(final Class<T> restServiceApiClass, final Session session, final String serviceUrl) {
        if (StringUtils.isNotBlank(serviceUrl)) {
            log.debug("Creating proxy using this url {}", serviceUrl);
            return JAXRSClientFactory.create(serviceUrl, restServiceApiClass);
        } else {
            return createRestProxy(restServiceApiClass, session);
        }
    }

    private static String getHstCmsServiceBaseUrl(final Session session) {
        try {
            //getting the base url from the hst REST proxy service
            Node hstRestProxyService = session.getNode(HST_REST_PROXY_SERVICE_LOCATION);
            String hstRestProxyServiceUrl = JcrUtils
                .getStringProperty(hstRestProxyService, RestProxyServicePlugin.CONFIG_REST_URI, "");
            log.debug("Using the hst rest proxy server URL {}", hstRestProxyServiceUrl);
            //removing the _cmsrest string from the url. Moreover, while deploy behind a proxy the context path must be included as well
            if (new URI(hstRestProxyServiceUrl).getPort() != -1) {
                //in case the url contains the port number, probably the site application is not served behind a proxy
                return hstRestProxyServiceUrl.replaceAll("_cmsrest", "");
            }
            //forcing the port and site context path in case there is a proxy
            return hstRestProxyServiceUrl.replaceAll("/(site\\/)?_cmsrest", ":8080/site/");
        } catch (RepositoryException repositoryException) {
            log.error("Something went wrong while fetch the service url from the following node {}"
                + "Is the node there {}", HST_REST_PROXY_SERVICE_LOCATION, repositoryException);
        } catch (URISyntaxException uriSyntaxException) {
            log.error("Something went wrong while creating the URI object {}", uriSyntaxException);
        }
        //if something went wrong while fetching the URL, then return the "standard" one
        return "http://127.0.0.1:8080/";
    }
}
