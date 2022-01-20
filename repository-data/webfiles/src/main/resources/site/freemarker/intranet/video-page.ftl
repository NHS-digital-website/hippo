<#ftl output_format="HTML">

<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../nhsd-common/macro/heroes/hero.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Video" -->

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#assign options = getHeroOptionsWithMetaData(document) />
<#assign options += {
    "colour": "darkBlue"
} />
<@hero options />

<div class="nhsd-t-grid nhsd-!t-margin-top-8 nhsd-!t-margin-bottom-8">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-12 nhsd-!t-no-gutters">
            <#if document.introduction?has_content>
                <@hst.html hippohtml=document.introduction var="introduction" />
                <#if introduction?length &gt; 0>
                    ${introduction?no_esc}
                    <hr class="nhsd-a-horizontal-rule" />
                </#if>
            </#if>
            <div style="position: relative; padding-bottom: 56.25%; height: 0;">
                <iframe src="${document.videoUri}" style="position: absolute; top: 0; left: 0; width: 100%; height: 100%;"></iframe>
                <link itemprop="embedUrl" href="${document.videoUri}" />
            </div>
        </div>
    </div>
</div>