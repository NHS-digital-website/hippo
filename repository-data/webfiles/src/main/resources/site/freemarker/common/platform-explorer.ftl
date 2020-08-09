<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.OrgStructure" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>
<#assign headingText = "Capabilities" />
<article class="article">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <ul>
                <div class="article-section">
                    <h2>${headingText}</h2>
                </div>
                <#list capabilities as capability>
                    <li>${capability_index + 1}. ${capability.name}</li>
                </#list>
            </ul>
        </div>
    </div>
</article>
