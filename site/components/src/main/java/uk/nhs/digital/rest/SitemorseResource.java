package uk.nhs.digital.rest;

import static org.hippoecm.hst.site.HstServices.getComponentManager;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.sitemorse.sci.SciClient;
import uk.nhs.digital.sitemorse.sci.SciException;
import uk.nhs.digital.toolbox.secrets.ApplicationSecrets;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;


@Path("/")
public class SitemorseResource extends org.hippoecm.hst.jaxrs.services.AbstractResource {

    private static final Logger LOG = LoggerFactory.getLogger(SitemorseResource.class);

    @GET
    public String get(@QueryParam("url") String url, @QueryParam("token") String token) {

        try {
            final String sitemorseToken = ((ApplicationSecrets) getComponentManager().getComponent("applicationSecrets")).getValue("SITEMORSE_API_TOKEN");

            if (StringUtils.isBlank(sitemorseToken)) {
                LOG.warn("Sitemorse API key is empty or could not be retrieved");
                return null;
            }
            SciClient client = new SciClient(sitemorseToken);

            String sessionCookie = "Cookies: add your session cookie";
            String[] headerArray = new String[1];
            headerArray[0] = sessionCookie;
            client.setExtraHeaders( headerArray );

            client.setExtendedResponse(true);
            return client.performTest(url);
        } catch (SciException e) {
            LOG.warn("Exception occurred in Sitemorse SCI client: ", e);
        }
        return null;
    }

    @GET
    @Path("/test")
    public String test() {
        return "{\n" + "    \"result\": {\n" + "        \"finishcode\": \"max_pages\",\n" + "        \"links\": 278,\n" + "        \"pages\": 1,\n" + "        \"finished\": \"2018-01-19T09:54:25\",\n" + "        \"scores\": {\n" + "            \"function\": {\n" + "                \"score\": 0,\n" + "                \"title\": \"Function\"\n" + "            },\n" + "            \"codequality\": {\n" + "                \"score\": 7,\n" + "                \"title\": \"Code Quality\"\n" + "            },\n" + "            \"brand\": {\n" + "                \"score\": null,\n" + "                \"title\": \"Brand\"\n" + "            },\n" + "            \"overall\": {\n" + "                \"exactscore\": 3.8600000000000003,\n" + "                \"score\": 4,\n" + "                \"title\": \"Overall\"\n" + "            },\n" + "            \"accessibility\": {\n" + "                \"score\": 0,\n" + "                \"title\": \"Accessibility\"\n" + "            },\n" + "            \"pdf\": {\n" + "                \"score\": null,\n" + "                \"title\": \"PDF\"\n" + "            },\n" + "            \"performance\": {\n" + "                \"score\": 10,\n" + "                \"title\": \"Performance\"\n" + "            },\n" + "            \"spelling\": {\n" + "                \"score\": null,\n" + "                \"title\": \"Spelling\"\n" + "            },\n" + "            \"email\": {\n" + "                \"score\": null,\n" + "                \"title\": \"Email\"\n" + "            },\n" + "            \"metadata\": {\n" + "                \"score\": 10,\n" + "                \"title\": \"Metadata\"\n" + "            }\n" + "        },\n" + "        \"ID\": 1467165484,\n" + "        \"url\": \"https://secure.sitemorse.com/reportkey.html?id=1467165484&sci&key=5c11b465&url=http%3A%2F%2Fwww.onehippo.org%2Flibrary%2Fabout%2Funderstand-hippo.html\",\n" + "        \"priorities\": {\n" + "            \"seo\": {\n" + "                \"diagnostics\": [],\n" + "                \"total\": 0,\n" + "                \"title\": \"Search engine optimization\"\n" + "            },\n" + "            \"grc\": {\n" + "                \"diagnostics\": [\n" + "                    {\n" + "                        \"category\": \"Accessibility\",\n" + "                        \"total\": 1,\n" + "                        \"title\": \"WCAG 2: H44 - Using label elements to associate text labels with form controls\"\n" + "                    }\n" + "                ],\n" + "                \"total\": 1,\n" + "                \"title\": \"Governance, risk and compliance\"\n" + "            },\n" + "            \"ux\": {\n" + "                \"diagnostics\": [\n" + "                    {\n" + "                        \"category\": \"Broken link\",\n" + "                        \"total\": 3,\n" + "                        \"title\": \"File not found\"\n" + "                    }\n" + "                ],\n" + "                \"total\": 1,\n" + "                \"title\": \"User experience\"\n" + "            }\n" + "        },\n" + "        \"totals\": {\n" + "            \"function\": 3,\n" + "            \"email\": 0,\n" + "            \"performance\": 0,\n" + "            \"seo\": 0,\n" + "            \"codequality\": 6,\n" + "            \"brand\": 0,\n" + "            \"quality\": 3,\n" + "            \"wcag2\": 10,\n" + "            \"spelling\": 0\n" + "        },\n" + "        \"urls\": 178,\n" + "        \"telnumbers\": {}\n" + "    }\n" + "}";
    }

}
