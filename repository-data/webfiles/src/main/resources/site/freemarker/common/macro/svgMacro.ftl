<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<#macro svgWithoutAltText svgString>
    <#if svgString?? && svgString?has_content && svgString?contains("svg")>
        ${svgString?no_esc}
    <#else>
        Unsupported image
    </#if>
</#macro>