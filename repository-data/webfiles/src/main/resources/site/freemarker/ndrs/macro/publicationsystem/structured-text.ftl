<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">

<#macro structuredText item uipath>
    <#list item.elements as element>
        <#if element.type == "Paragraph">
            <p class="nhsd-t-body" data-uipath="${uipath}">${element}</p>
        </#if>
        <#if element.type == "BulletList">
            <ul>
                <#list element.items as item>
                    <li class="nhsd-t-body" data-uipath="${uipath}">${item}</li>
                </#list>
            </ul>
        </#if>
    </#list>
</#macro>
