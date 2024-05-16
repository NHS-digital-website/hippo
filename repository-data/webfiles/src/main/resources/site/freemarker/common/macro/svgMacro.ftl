<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<#macro svgWithAltText svgString altText>
    <#if svgString?? && svgString?has_content && svgString?contains("svg")>
        ${svgString?replace("</svg>", "<title>${altText}</title></svg>")?no_esc}
    <#else>
        Unsupported image
    </#if>
</#macro>

<#macro svgWithoutAltText svgString>
    <#if svgString?? && svgString?has_content && svgString?contains("svg")>
        ${svgString?no_esc}
    <#else>
        Unsupported image
    </#if>
</#macro>