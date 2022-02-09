<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/childPageGrid.ftl">
<#include "macro/hubPageComponents.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/contentPixel.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article>
    <div class="nhsd-t-grid">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12">

                <h1 class="nhsd-t-heading-xxl" data-uipath="document.title">${document.title}</h1>

                <#if document.summary?? && document.summary?has_content>
                    <div data-uipath="website.hub.summary">
                        <@hst.html hippohtml=document.summary contentRewriter=brContentRewriter/>
                    </div>
                </#if>

                <#if document.body?? && document.body?has_content>
                    <#if document.summary?has_content>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>
                    <div>
                        <@hst.html hippohtml=document.body contentRewriter=brContentRewriter/>
                    </div>
                </#if>

                <#if childPages?? && childPages?size!=0>
                    <#if document.listtitle?? && document.listtitle?has_content>
                        <h2 class="nhsd-t-heading-xl">${document.listtitle}</h2>
                    </#if>
                    <@childPageGrid childPages></@childPageGrid>
                </#if>
            </div>
        </div>
    </div>
</article>
