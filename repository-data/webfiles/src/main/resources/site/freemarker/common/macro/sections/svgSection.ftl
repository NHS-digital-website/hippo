<#ftl output_format="HTML">

<#include "../../../include/imports.ftl">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.SvgSection" -->

<#macro svgSection section>
    This is test <br/>
    ${section} <br/>
    <@hst.link var="link" hippobean=section.link/>
   Link is -->  ${link}
    <#if link?contains("svg")>
        <script type="text/javascript">
            $.get('${link}', function (svg) {
                var newsvg = svg
                var newsvg1 = null;
                if (newsvg.includes("</title>")) {
                    var regex = /(<title\b[^>]*>)[^<>]*(<\/title>)/i;
                    newsvg1 = newsvg.replace(regex, "<title>${section.altText}</title>");
                } else {
                    newsvg1 = newsvg.replace("</svg>", "<title>${section.altText}</title></svg>");
                }
                alert(newsvg1);
                document.getElementById("svgid").innerHTML = newsvg1;
            }, 'text');
        </script>
    <#else>
        <script type="text/javascript">
            $.get('${link}', function (svg) {
                document.getElementById("svgid").innerHTML = "<p>Unsupported image</p>";
            }, 'text');
        </script>
    </#if>
    <span>
        <div id="svgid">
        </div>
    </span>

</#macro>
