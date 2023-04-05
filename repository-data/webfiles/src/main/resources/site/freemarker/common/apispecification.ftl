<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../nhsd-common/macro/component/lastModified.ftl">
<#include "../nhsd-common/macro/heroes/hero.ftl">
<#include "../nhsd-common/macro/heroes/hero-options.ftl">

<!DOCTYPE html>
<html lang="en" class="nhsd-no-js">
<meta charset="utf-8">

<body>

<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#if document?? >
    <script type="text/javascript"
            src="<@hst.webfile path="/apispecification/rapidoc-min.js"/>"></script>

    <script type="text/javascript"
            src="<@hst.webfile path="/apispecification/rapidoc.js"/>"></script>

    <style>
        <#--
        Most of the custom RapiDoc styling overrides have been implemented in `src/styles/custom-styles.js` by building RapiDoc from source.
        See https://rapidocweb.com/api.html#developers

        A copy of this file can be found at `site/apispecification/rapidoc-custom-styles.js`. This file has no purpose other than to
        document the styling overrides. To update the styling, clone the RapiDoc repo (https://github.com/rapi-doc/RapiDoc) and copy the
        contents of the `rapidoc-custom-styles.js` file into the `src/styles/custom-styles.js` source file. After adding your custom styles,
        build RapiDoc and the generated `rapidoc-min.js` in the `dist` folder will have your styles. Copy and paste this file into the
        `site/apispecification` folder, overwriting the old one. Make sure to keep our `rapidoc-custom-styles.js` copy up to date.

        We may look to automate this in the future.

        CSS parts can also be used to style RapiDoc (https://rapidocweb.com/css-parts.html). However, this didn't give us to level of
        customisation required and has been used only where required below.
        -->

        .rapi-doc-component {
            overflow: unset;
        }

        rapi-doc::part(section-navbar-search) {
            justify-content: flex-start;
            padding: 0;
            margin-bottom: 15px;

        }

        rapi-doc::part(btn-search) {
            margin: 0;
            width: unset;
        }

        rapi-doc::part(btn-try) {
            margin: 16px 5px 0 0;
        }

        rapi-doc::part(wrap-request-btn) {
            flex-direction: row-reverse;
            flex-wrap: wrap;
            justify-content: flex-end;
            padding-top: 12px;
        }
    </style>

    <@hero getExtendedHeroOptions(document) />

    <div class="nhsd-t-grid nhsd-!t-display-no-js-show">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12">
                <p>Enable Javascript to view the interactive API specification.</p>
                <p>Alternatively, you can download the OpenAPI Specification (OAS)
                    file for this API using the button above.</p>
            </div>
        </div>
    </div>

    <div class="nhsd-t-grid nhsd-!t-display-no-js-hide">
        <div class="nhsd-t-row">
            <rapi-doc
                class="rapi-doc-component nhsd-!t-margin-top-6"
                id="rapi-doc-spec"
                theme="light"
                bg-color="#ffffff"
                text-color="#3f525f"
                primary-color="#005bbb"
                regular-font="Frutiger W01, Arial, sans-serif"
                load-fonts="false"
                font-size="largest"
                nav-bg-color="#ffffff"
                nav-text-color="#3f525f"
                info-description-headings-in-navbar="true"
                show-header="false"
                allow-search="false"
                allow-advanced-search="true"
                allow-server-selection="false"
                schema-description-expanded="true"
                render-style="focused"
                sort-endpoints-by="none"
                css-file="nhsd-frontend.css"
                show-curl-before-try="true"
            >

                <div slot="footer">
                    <p class="nhsd-t-body nhsd-!t-margin-top-3"><a class="nhsd-a-link" href="">Back to top</a></p>
                    <@lastModified document.lastPublicationDate></@lastModified>
                </div>

            </rapi-doc>

            <script>
                const specification = ${document.json?no_esc};
                const isDevEnv = ${isDevEnv?c};

                document.addEventListener('DOMContentLoaded', () => {
                    let rapiDocEl = document.getElementById("rapi-doc-spec");
                    rapiDocEl.loadSpec(specification);

                    // add some basic styling to the header
                    if (isDevEnv) {
                        rapiDocEl.setAttribute("show-header", "true");
                    }

                    // Move these to a different event listener so will be applied when uploading a spec via the header
                    let tryThisApiDisabled = specification["x-spec-publication"]?.["try-this-api"]?.disabled;
                    if (tryThisApiDisabled === true) {
                        rapiDocEl.setAttribute("allow-try", "false");
                    }

                    let allowAuth = specification.components?.securitySchemes;
                    if (!allowAuth) {
                        rapiDocEl.setAttribute("allow-authentication", "false");
                    }
                })
            </script>
        </div>
    </div>
</#if>
</body>
</html>

<#function getExtendedHeroOptions document>
    <#assign options = getHeroOptions(document) />

    <@hst.html var="htmlSummary" hippohtml=options.summary contentRewriter=brContentRewriter />
    <@hst.link var="oasHintLink" path='developer/guides-and-documentation/our-api-technologies#oas/'/>
    <#assign oasHintText = "<p style=\"margin-left:0px; margin-right:0px\">This specification is written from an <a href=\"${oasHintLink}\">OAS</a> file.</p>">

    <@hst.link var="oasLink" path='/restapi/oas/${document.getSpecificationId()}/'/>

    <#assign options += {
    "summary": (htmlSummary + oasHintText)?no_esc,
    "buttons": [{
    "src": oasLink,
    "text": "Get OAS file",
    "target": "_blank"
    }]
    }/>
    <#return options/>
</#function>
