<#include "../../include/imports.ftl">

<#macro structuredText item uipath>
    <#list item.elements as element>
        <#if element.type == "Paragraph">
            <p data-uipath="${uipath}">${element?html}</p>
        </#if>

        <#if element.type == "BulletList">
            <ul><#list element.items as item>
                <li data-uipath="${uipath}">${item?html}</li>
            </#list></ul>
        </#if>
    </#list>
</#macro>
