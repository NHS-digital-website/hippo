<#ftl output_format="HTML">

<#include "../include/imports.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/sections/sections.ftl">

<#include "macro/metaTags.ftl">
<#-- Add meta tags -->
<@metaTags></@metaTags>

<#assign hasSectionIntroContent = document.introductionsections?has_content />
<#assign hasSectionContent = document.footersections?has_content />

<article class="article">

    <@documentHeader document '' document.pageIcon?has_content?then(document.pageIcon, '') '' '' '' false></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column page-block page-block--main">
                <#if hasSectionIntroContent>
                    <@sections document.introductionsections></@sections>
                </#if>

                <div class="article-section">
                    <@hst.include ref="form"/>
                </div>

                <#if hasSectionContent>
                    <@sections document.footersections></@sections>
                </#if>
            </div>
        </div>
    </div>

</article>
