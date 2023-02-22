<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../nhsd-common/macro/component/lastModified.ftl">
<#include "../nhsd-common/macro/heroes/hero.ftl">
<#include "../nhsd-common/macro/heroes/hero-options.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">
<meta charset="utf-8">

<body>

<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#if document?? >
    <script type="text/javascript" src="<@hst.webfile path="/apispecification/rapidoc-min.js"/>"></script>

    <style>
        .rapi-doc-section {
            padding: 24px 4px
        }

        rapi-doc::part(btn) {
            border-radius: 1.22rem;
        }

        rapi-doc::part(btn-try) {
            margin-right:5px;
        }

        rapi-doc::part(wrap-request-btn) {
            flex-direction: row-reverse;
            flex-wrap: wrap;
            justify-content: flex-end
        }

        @media only screen and (min-width: 768px) {
            rapi-doc::part(section-navbar) {
                position: sticky;
                top: 1rem;
                max-height: 95vh
            }

            .rapi-doc-section {
                padding: 24px 8px
            }

            rapi-doc::part(section-navbar-scroll) {
                padding: 6px;
            }
        }

        @media only screen and (max-width: 768px) {
            rapi-doc::part(section-navbar) {
                width: 100%;
                display: flex;
            }

            rapi-doc::part(section-main-content) {
                overflow: unset;
            }
        }

        @media only screen and (min-width: 1024px) {
            .rapi-doc-section {
                padding: 24px 80px 12px;
            }
        }
    </style>

    <@hero getExtendedHeroOptions(document) />
    <div class="nhsd-t-grid nhsd-!t-no-gutters nhsd-!t-margin-top-6" style="max-width:106.666rem">
        <div class="nhsd-t-row" style="height:auto">
            <rapi-doc
                style="overflow:unset"
                id="rapi-doc-spec"
                theme="light"
                bg-color="#ffffff"
                text-color="#3f525f"
                header-color="#231f20"
                primary-color="#005bbb"
                regular-font="Frutiger W01, Arial, sans-serif"
                load-fonts="false"
                font-size="largest"
                nav-bg-color="#ffffff"
                nav-text-color="#3f525f"
                info-description-headings-in-navbar="true"
                show-header="false"
                allow-search="false"
                allow-advanced-search="false"
                allow-server-selection="false"
                schema-description-expanded="true"
                render-style="focused"
                sort-endpoints-by="none"
            >

                <div slot="footer" >
                    <div class="rapi-doc-section"><a class="nhsd-a-link" href="#top">Back to top</a></div>

                    <div class="rapi-doc-section">
                        <@lastModified document.lastPublicationDate></@lastModified>
                    </div>
                </div>

            </rapi-doc>

            <script>
                const specification = ${document.json?no_esc}

                document.addEventListener('DOMContentLoaded', () => {
                    let rapiDocEl = document.getElementById("rapi-doc-spec");
                    rapiDocEl.loadSpec(specification);

                    let tryThisApiDisabled = specification["x-spec-publication"]?.["try-this-api"]?.disabled;
                    if (tryThisApiDisabled === true) {
                        rapiDocEl.setAttribute("allow-try", "false");
                    }

                    let allowAuth = specification.components?.securitySchemes;
                    if (!allowAuth) {
                        rapiDocEl.setAttribute("allow-authentication", "false");
                    }

                    let customStyles = new CSSStyleSheet();
                    customStyles.replaceSync(`
                        #the-main-body {
                            overflow: unset;
                            flex-flow: wrap;
                        }

                        #api-title, #api-info, #link-overview {
                            display: none;
                        }

                        a {
                            color: #005bbb !important;
                        }
                    `);
                    const rapidDocStyleSheets = rapiDocEl.shadowRoot.adoptedStyleSheets;
                    rapiDocEl.shadowRoot.adoptedStyleSheets = [...rapidDocStyleSheets, customStyles];
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
