<#ftl output_format="HTML">

<#include "../../../include/imports.ftl">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.SvgSection" -->

<#macro svgSection section index>

    <@hst.link hippobean=section.link.original fullyQualified=true var="imageLink" />
    <#local getData="uk.nhs.digital.freemarker.svg.SvgFromUrl"?new() />
    <#if imageLink?contains("http")>
        <#local chartData =  (getData(imageLink))! />
    <#else>
        <#local chartData =  (getData("https:"+imageLink))! />
    </#if>

    <#if imageLink?? && imageLink?has_content && imageLink?contains("svg")>
        ${chartData.data?replace("</svg>", "<title>${section.altText}</title></svg>")?no_esc}
    <#else>
        Unsupported image
    </#if>
</#macro>
