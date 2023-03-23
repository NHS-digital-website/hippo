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
        .rapi-doc-component {
            overflow: unset;
        }

        rapi-doc::part(section-navbar) {
            padding: 6px 0.833rem;
        }

        rapi-doc::part(section-main-content) {
            padding: 6px 0.833rem;
        }

        rapi-doc::part(section-auth) {
            padding: 0;
        }

        rapi-doc::part(section-operations-in-tag) {
            padding: 0;
        }

        rapi-doc::part(section-overview) {
            padding: 0;
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

        rapi-doc::part(btn) {
            color: #ffffff;
            background: #005bbb;
            display: inline-flex;
            position: relative;
            align-items: center;
            justify-content: center;
            max-width: 15.55rem;
            padding: .5555555556rem 1.1111111111rem;
            border: 2px solid #005bbb;
            border-radius: 1.22rem;
            outline: 0;
            box-shadow: 0 0 0 .167rem transparent;
            font-size: .78rem;
            font-weight: 600;
            line-height: 1.11;
            text-align: center;
            text-decoration: none;
            /*vertical-align: text-bottom;*/
            cursor: pointer;
            -webkit-appearance: none;
            appearance: none;
            -webkit-user-select: none;
            user-select: none;
            transition-property: background-color,box-shadow,border-color;
            transition-duration: .15s;
        }

        rapi-doc::part(btn-selected-response-status) {
            color: #ffffff !important;
            background: #005bbb !important;
        }

        rapi-doc::part(btn):after {
            content: "";
            display: block;
            position: absolute;
            top: 50%;
            left: 50%;
            width: 2.5rem;
            height: 2.5rem;
            transform: translateX(-50%) translateY(-50%);
            border-radius: 100%;
        }

        rapi-doc::part(btn):focus,
        rapi-doc::part(btn):hover {
            border-color: #003087;
            background:#003087;
            box-shadow: 0 0 0 0.167rem #fae100;
        }

        rapi-doc::part(btn-selected-response-status):focus,
        rapi-doc::part(btn-selected-response-status):hover {
            border-color: #003087 !important;
            background:#003087 !important;
        }

        rapi-doc::part(btn):active {
            border-color: #005bbb;
            background: #005bbb;
            transition-property: none;
            box-shadow: 0 0 0 0.167rem transparent;
            transform: translateY(0.111rem);
        }

        rapi-doc::part(btn-response-status),
        rapi-doc::part(btn-outline) {
            color: #005bbb;
            background: #ffffff;
        }

        rapi-doc::part(btn-response-status):focus,
        rapi-doc::part(btn-response-status):hover,
        rapi-doc::part(btn-outline):focus,
        rapi-doc::part(btn-outline):hover {
            border-color: #231f20;
            color: #005bbb;
            background: #ffffff;
        }

        rapi-doc::part(btn-response-status):active,
        rapi-doc::part(btn-outline):active {
            border-color: #005bbb;
            color: #005bbb;
            background: #ffffff;
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

        rapi-doc::part(schema-multiline-toggle) {
            display: none;
        }

        @media only screen and (min-width: 1024px) {
            rapi-doc::part(section-navbar) {
                position: sticky;
                top: 1rem;
                max-height: 95vh;
            }
        }

        @media only screen and (max-width: 1023px) {
            rapi-doc::part(section-navbar) {
                width: 100%;
                display: flex;
            }
        }

        .btn {
            width: 70px;
            height: 70px;
            background-color: #005bbb;
            color: #fff;
            font-size: 12px;
            display: block;
            border: none;
            margin: 2px;
            border-radius: 2px;
            cursor: pointer;
            outline: none;
        }
    </style>

<#-- Configuration demo -->
<#--    <script>-->
<#--        function getRapiDoc() {-->
<#--            return document.getElementById("rapi-doc-spec");-->
<#--        }-->

<#--        function changeRenderStyle() {-->
<#--            let currRender = getRapiDoc().getAttribute('render-style');-->
<#--            let newRender = currRender === "focused" ? "read" : "focused";-->
<#--            getRapiDoc().setAttribute('render-style', newRender);-->
<#--        }-->

<#--        function changeSchemaStyle() {-->
<#--            let currSchema = getRapiDoc().getAttribute('schema-style');-->
<#--            let newSchema = currSchema === "tree" ? "table" : "tree";-->
<#--            getRapiDoc().setAttribute('schema-style', newSchema);-->
<#--        }-->

<#--        function changeSchemaExpandLevel() {-->
<#--            let currSchema = getRapiDoc().getAttribute('schema-expand-level');-->
<#--            let newSchema = currSchema === "999" ? "1" : "999";-->
<#--            getRapiDoc().setAttribute('schema-expand-level', newSchema);-->
<#--        }-->

<#--        function toggleAttr(attr) {-->
<#--            if (getRapiDoc().getAttribute(attr) === 'false') {-->
<#--                getRapiDoc().setAttribute(attr, "true");-->
<#--            } else {-->
<#--                getRapiDoc().setAttribute(attr, "false");-->
<#--            }-->
<#--        }-->
<#--    </script>-->

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
                show-curl-before-try="false"
                schema-style="table"
                schema-expand-level="999"
                default-schema-tab="example"
            >

<#--                <div-->
<#--                    style="display:flex; margin:10px; justify-content:center;flex-wrap: wrap;">-->
<#--                    <button class="btn" onclick="changeRenderStyle()">-->
<#--                        Render style-->
<#--                    </button>-->
<#--                    <button class="btn" onclick="toggleAttr('show-header')">-->
<#--                        Toggle header-->
<#--                    </button>-->
<#--                    <button class="btn" onclick="toggleAttr('allow-search')">-->
<#--                        Toggle search-->
<#--                    </button>-->
<#--                    <button class="btn"-->
<#--                            onclick="toggleAttr('allow-advanced-search')">-->
<#--                        Toggle advanced search-->
<#--                    </button>-->
<#--                    <button class="btn"-->
<#--                            onclick="toggleAttr('allow-server-selection')">-->
<#--                        Toggle server selection-->
<#--                    </button>-->
<#--                    <button class="btn"-->
<#--                            onclick="toggleAttr('show-curl-before-try')">Toggle-->
<#--                        CURL before try-->
<#--                    </button>-->
<#--                    <button class="btn" onclick="changeSchemaStyle()">Schema-->
<#--                        style-->
<#--                    </button>-->
<#--                    <button class="btn" onclick="changeSchemaExpandLevel()">-->
<#--                        Schema expand level 1-->
<#--                    </button>-->
<#--                </div>-->

                <div slot="footer">
                    <p class="nhsd-t-body nhsd-!t-margin-top-3"><a class="nhsd-a-link" href="#top">Back to top</a></p>
                    <@lastModified document.lastPublicationDate></@lastModified>
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
