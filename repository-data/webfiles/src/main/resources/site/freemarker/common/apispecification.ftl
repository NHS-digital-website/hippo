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

    <style>
        .rapi-doc-component {
            max-width: 106.666rem;
            width: 100%;
            margin: 0 auto;
            overflow: unset;
            box-sizing: border-box;
        }

        .rapi-doc-section {
            padding: 24px 4px;
        }

        rapi-doc::part(btn) {
            border-radius: 1.22rem;
        }

        rapi-doc::part(schema-multiline-toggle) {
            display: none;
        }

        rapi-doc::part(btn-try) {
            margin-right: 5px;
        }

        rapi-doc::part(wrap-request-btn) {
            flex-direction: row-reverse;
            flex-wrap: wrap;
            justify-content: flex-end;
        }

        @media only screen and (min-width: 768px) {
            rapi-doc::part(section-navbar) {
                position: sticky;
                top: 1rem;
                max-height: 95vh;
            }

            .rapi-doc-section {
                padding: 24px 8px;
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

        .btn{
            width: 70px;
            height: 70px;
            background-color: #005bbb;
            color: #fff;
            font-size: 12px;
            display: block;
            border: none;
            margin: 2px;
            border-radius: 2px;
            cursor:pointer;
            outline:none;
        }
    </style>

    <#-- Configuration demo -->
    <script>
        function getRapiDoc(){
            return document.getElementById("rapi-doc-spec");
        }

        function changeRenderStyle() {
            let currRender = getRapiDoc().getAttribute('render-style');
            let newRender = currRender === "focused" ? "read" : "focused";
            getRapiDoc().setAttribute('render-style', newRender );
        }

        function changeSchemaStyle() {
            let currSchema = getRapiDoc().getAttribute('schema-style');
            let newSchema = currSchema === "tree" ? "table" : "tree";
            getRapiDoc().setAttribute('schema-style', newSchema );
        }

        function changeSchemaExpandLevel() {
            let currSchema = getRapiDoc().getAttribute('schema-expand-level');
            let newSchema = currSchema === "999" ? "1" : "999";
            getRapiDoc().setAttribute('schema-expand-level', newSchema );
        }

        function toggleAttr(attr){
            if (getRapiDoc().getAttribute(attr) === 'false'){
                getRapiDoc().setAttribute(attr,"true");
            }
            else{
                getRapiDoc().setAttribute(attr,"false");
            }
        }
    </script>

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

    <rapi-doc
        class="rapi-doc-component nhsd-!t-margin-top-6 nhsd-!t-display-no-js-hide"
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
        allow-advanced-search="false"
        allow-server-selection="false"
        schema-description-expanded="true"
        render-style="focused"
        sort-endpoints-by="none"
        css-file="nhsd-frontend.css"
        show-curl-before-try="false"
        schema-style="tree"
        schema-expand-level="999"
    >

        <div style="display:flex; margin:10px; justify-content:center;flex-wrap: wrap;">
            <button class="btn" onclick="changeRenderStyle()">Render style</button>
            <button class="btn" onclick="toggleAttr('show-header')">Toggle header</button>
            <button class="btn" onclick="toggleAttr('allow-search')">Toggle search</button>
            <button class="btn" onclick="toggleAttr('allow-advanced-search')">Toggle advanced search</button>
            <button class="btn" onclick="toggleAttr('allow-server-selection')">Toggle server selection</button>
            <button class="btn" onclick="toggleAttr('show-curl-before-try')">Toggle CURL before try</button>
            <button class="btn" onclick="changeSchemaStyle()">Schema style</button>
            <button class="btn" onclick="changeSchemaExpandLevel()">Schema expand level 1</button>
        </div>

        <div slot="footer">
            <div class="rapi-doc-section"><a class="nhsd-a-link" href="#top">Back to
                    top</a></div>

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

            // This is temp until we do more advanced styling
            // See: https://rapidocweb.com/api.html#developers
            let customStyles = new CSSStyleSheet();
            customStyles.replaceSync(`
                        #the-main-body {
                            overflow: unset;
                            flex-flow: wrap;
                        }

                        #api-title, #api-info, #link-overview {
                            display: none;
                        }

                        .method-fg.options,
                        .method-fg.head,
                        .method-fg.patch {
                            color: #827717;
                        }

                        .m-markdown h2, .m-markdown h3, .m-markdown h4, .m-markdown h5, .m-markdown h6 {
                            font-weight: 600;
                            color: #231f20;
                        }

                        .m-markdown h2 {
                            font-size: 2rem;
                            letter-spacing: -.0622rem;
                            line-height: 1.1666;
                        }

                        .m-markdown h3 {
                            font-size: 1.6669rem;
                            letter-spacing: -.063rem;
                            line-height: 1.20048;
                        }

                        .m-markdown h4 {
                            font-size: 1.444rem;
                            letter-spacing: -.0277rem;
                            line-height: 1.19267;
                        }

                        .m-markdown h5 {
                            font-size: 1.222rem;
                            letter-spacing: -.0277rem;
                            line-height: 1.3125;
                        }

                        .m-markdown h6 {
                            font-size: 1rem;
                            letter-spacing: -.0277rem;
                            line-height: 1.3125;
                        }

                        @media (max-width:63.99em) {
                            .m-markdown h2 {
                                font-size: 1.669rem;
                                letter-spacing: -.01666rem;
                                line-height: 1.1337;
                            }

                            .m-markdown h3 {
                                font-size: 1.444rem;
                                letter-spacing: -.01666rem;
                                line-height: 1.19267;
                            }

                            .m-markdown h4 {
                                font-size: 1.222rem;
                                letter-spacing: -.01666rem;
                                line-height: 1.22749;
                            }

                            .m-markdown h5 {
                                font-size: 1rem;
                                letter-spacing: -.01666rem;
                                line-height: 1.3125;
                            }

                            .m-markdown h6 {
                                font-size: .888rem;
                                letter-spacing: -.01666rem;
                                line-height: 1.3125;
                            }
                        }
                    `);
            rapiDocEl.shadowRoot.adoptedStyleSheets.push(customStyles);
        })
    </script>
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
