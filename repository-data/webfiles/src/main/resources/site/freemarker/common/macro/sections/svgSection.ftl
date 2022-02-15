<#ftl output_format="HTML">

<#include "../../../include/imports.ftl">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.SvgSection" -->

<#macro svgSection section index>

    <#assign svgData = section.svgXmlFromRepository>

    <#if svgData?? && svgData?has_content && svgData?contains("svg")>
        ${svgData?replace("</svg>", "<title>${section.altText}</title></svg>")?no_esc}
    <#else>
        Unsupported image
    </#if>
</#macro>

