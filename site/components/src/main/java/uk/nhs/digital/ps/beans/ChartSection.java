package uk.nhs.digital.ps.beans;

import static org.hippoecm.hst.site.HstServices.getComponentManager;

import org.apache.commons.text.StringSubstitutor;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoResource;
import org.hippoecm.hst.core.linking.HstLinkCreator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.toolbox.crypto.Hmac;
import uk.nhs.digital.toolbox.secrets.ApplicationSecrets;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@HippoEssentialsGenerated(internalName = "publicationsystem:chartSection")
@Node(jcrType = "publicationsystem:chartSection")
public class ChartSection extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "publicationsystem:dataFile")
    public HippoResource getDataFile() {
        return getChildBeansByName("publicationsystem:dataFile", HippoResource.class).get(0);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:title")
    public String getTitle() {
        return getSingleProperty("publicationsystem:title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:type")
    public String getType() {
        return getSingleProperty("publicationsystem:type");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:yTitle")
    public String getYTitle() {
        return getSingleProperty("publicationsystem:yTitle");
    }

    @HippoEssentialsGenerated(internalName = "website:htmlCode")
    public String getHtmlCode() {
        return getSingleProperty("website:htmlCode");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:chartConfig")
    public String getChartConfig() {
        return getSingleProperty("publicationsystem:chartConfig");
    }

    public String getImageUrl() throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        final String datavizHighchartsUrl = ((ApplicationSecrets) getComponentManager().getComponent("applicationSecrets")).getValue("DATAVIZ_HIGHCHARTS_EXPORTER_URL");
        final String datavizExporterKey = ((ApplicationSecrets) getComponentManager().getComponent("applicationSecrets")).getValue("DATAVIZ_EXPORTER_KEY");

        if (datavizHighchartsUrl == null || datavizExporterKey == null) {
            return null;
        }

        String config = getSingleProperty("publicationsystem:chartConfig");
        String base64Data = Base64.getEncoder().encodeToString(config.getBytes());
        String urlEncodedData = URLEncoder.encode(base64Data, StandardCharsets.UTF_8.toString());
        if (urlEncodedData.length() > 1500) {
            String chartOptionsPath = "DataViz/" + getParentBean().getCanonicalUUID() + "/" + getUniqueId();

            HstRequestContext requestContext = RequestContextProvider.get();
            HstLinkCreator linkCreator = requestContext.getHstLinkCreator();
            String chartOptionsUri = linkCreator.create(chartOptionsPath, requestContext.getMount("restapi")).toUrlForm(requestContext, true);

            base64Data = Base64.getEncoder().encodeToString(chartOptionsUri.getBytes());
            urlEncodedData = URLEncoder.encode(base64Data, StandardCharsets.UTF_8.toString());
        }

        // Create hash
        String hash = Hmac.base64Hash(base64Data, datavizExporterKey);
        hash = URLEncoder.encode(hash, StandardCharsets.UTF_8.toString());

        Map<String, String> urlVals = new HashMap<>();
        urlVals.put("hash", hash);
        urlVals.put("options", urlEncodedData);
        StringSubstitutor sub = new StringSubstitutor(urlVals);
        return sub.replace(datavizHighchartsUrl);
    }

    /**
     * This is needed for the HTML element that contains the chart to have a unique ID
     */
    public String getUniqueId() {
        return String.valueOf(getPath().hashCode());
    }

    public String getSectionType() {
        return "chart";
    }
}
