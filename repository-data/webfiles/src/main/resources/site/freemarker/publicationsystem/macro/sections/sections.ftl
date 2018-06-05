<#ftl output_format="HTML">
<#include "textSection.ftl">
<#include "imageSection.ftl">
<#include "chartSection.ftl">
<#include "related-linkSection.ftl">

<!-- This is a load of global setup for the highcharts config -->
<#include "highChartsSetup.ftl">

<!-- Set up equation support from mathjax -->
<script type="text/javascript" async src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/latest.js?config=TeX-AMS_HTML"></script>

<#macro sections sections>
    <div data-uipath="ps.publication.body">
        <#list sections as section>
            <#if section.sectionType == 'text'>
                <@textSection section=section />
            <#elseif section.sectionType == 'image'>
                <@imageSection section=section />
            <#elseif section.sectionType == 'relatedLink'>
                <@relatedLinkSection section=section />
            <#elseif section.sectionType == 'chart'>
                <@chartSection section=section />
            </#if>
        </#list>
    </div>
</#macro>
