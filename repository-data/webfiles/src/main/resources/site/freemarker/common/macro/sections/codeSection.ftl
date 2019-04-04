<#ftl output_format="HTML">

<#include "../fileIcon.ftl">

<#macro codeSection section>
    <div class="article-section">

        <#if section.headingLevel == 'Main heading'>
            <h2 data-uipath="website.contentblock.codesection.title">${section.heading}</h2>
        <#else>
            <h3 data-uipath="website.contentblock.codesection.title">${section.heading}</h3>
        </#if>









    </div>
</#macro>
