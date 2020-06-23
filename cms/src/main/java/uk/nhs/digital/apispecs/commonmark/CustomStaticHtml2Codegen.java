package uk.nhs.digital.apispecs.commonmark;

import io.swagger.codegen.v3.generators.html.StaticHtml2Codegen;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomStaticHtml2Codegen extends StaticHtml2Codegen {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomStaticHtml2Codegen.class);

    @Override
    public void preprocessOpenAPI(OpenAPI openApi) {
        this.openAPI = openApi;

        if (openAPI.getInfo() != null) {
            Info info = openAPI.getInfo();
            if (StringUtils.isBlank(jsProjectName) && info.getTitle() != null) {
                // when jsProjectName is not specified, generate it from info.title
                jsProjectName = sanitizeName(dashize(info.getTitle()));
            }
        }

        // default values
        if (StringUtils.isBlank(jsProjectName)) {
            jsProjectName = "swagger-js-client";
        }
        if (StringUtils.isBlank(jsModuleName)) {
            jsModuleName = camelize(underscore(jsProjectName));
        }

        additionalProperties.put("jsProjectName", jsProjectName);
        additionalProperties.put("jsModuleName", jsModuleName);

        preparHtmlForGlobalDescription(openAPI);

    }

    /**
     * Parse Markdown to HTML for the main "Description" attribute
     *
     * @param openApi
     *            The base object containing the global description through
     *            "Info" class
     * @return Void
     */
    private void preparHtmlForGlobalDescription(OpenAPI openApi) {
        String currentDescription = openApi.getInfo().getDescription();
        if (currentDescription != null && !currentDescription.isEmpty()) {
            CommonmarkMarkdownConverter markInstance = new CommonmarkMarkdownConverter();
            openApi.getInfo().setDescription(markInstance.toHtml(currentDescription));
        } else {
            LOGGER.error("Swagger object description is empty [" + openApi.getInfo().getTitle() + "]");
        }
    }
}
