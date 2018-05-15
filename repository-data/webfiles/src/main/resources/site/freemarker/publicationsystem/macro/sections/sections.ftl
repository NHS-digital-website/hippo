<#ftl output_format="HTML">
<#include "textSection.ftl">
<#include "imageSection.ftl">
<#include "chartSection.ftl">
<#include "related-linkSection.ftl">

<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

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
