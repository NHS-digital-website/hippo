<#ftl output_format="HTML">
<#include "textSection.ftl">
<#include "imageSection.ftl">
<#include "related-linkSection.ftl">

<#macro sections sections>
    <div data-uipath="ps.publication.body">
        <#list sections as section>
            <#if section.sectionType == 'text'>
                <@textSection section=section />
            <#elseif section.sectionType == 'image'>
                <@imageSection section=section />
            <#elseif section.sectionType == 'relatedLink'>
                <@relatedLinkSection section=section />
            </#if>
        </#list>
    </div>
</#macro>
