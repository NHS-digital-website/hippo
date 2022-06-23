<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.VisualHub" -->

<#include "../include/imports.ftl">
<#include "macro/heroes/hero.ftl">
<#include "macro/heroes/hero-options.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/visualhubBox.ftl">
<#include "macro/tabTileHeadings.ftl">
<#include "macro/tabTiles.ftl">
<#include "macro/contentPixel.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>
<@hst.setBundle basename="site.website.labels"/>

<#assign bannerImage = "" />

<#assign hasSummaryContent = document.summary.content?has_content />
<#assign hasBannerImage = document.image?has_content />
<#assign hasTopicIcon = document.pageIcon?has_content />
<#assign hasAdditionalInformation = document.additionalInformation.content?has_content />
<#assign hasPrimaryLinks = document.primarySections?? && document.primarySections?size gt 0 />
<#assign hasTabTileLinks = document.tileSections?? && document.tileSections?size gt 0 && !hasPrimaryLinks/>

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article>
    <#assign heroOptions = getHeroOptions(document) />
    <#if heroOptions.image?has_content>
        <@hero getHeroOptions(document) "image" />
    <#else>
        <@hero getHeroOptions(document) />
    </#if>

    <div class="nhsd-t-grid">
    <#if document.introduction?has_content>
            <div class="nhsd-t-row nhsd-!t-margin-top-8 nhsd-!t-margin-bottom-4">
                <div class="nhsd-t-col-12">
                    <@hst.html hippohtml=document.introduction contentRewriter=brContentRewriter />
                </div>
            </div>
    </#if>

    <#if hasTabTileLinks>
        <#-- get param 'area' -->
        <#assign param = hstRequest.request.getParameter("area") />
        <#-- assign area the param value else, the first section in the list -->
        <#assign area = param???then(param, slugify(document.tileSections[0].tileSectionHeading)) />

        <div class="nhsd-t-row">
            <@tabTileHeadings document.tileSections "service" area />
        </div>

        <#list document.tileSections as tileSection>
            <#if slugify(tileSection.tileSectionHeading) == area>
                <div class="nhsd-o-card-list">
                    <div class="nhsd-t-grid nhsd-t-grid--nested">
                        <div class="nhsd-t-row nhsd-o-card-list__items" role="tabpanel">
                            <#list tileSection.tileSectionLinks as links>
                                <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6">
                                    <@tabTiles links/>
                                </div>
                            </#list>
                        </div>
                    </div>
                </div>
            </#if>
        </#list>
    </#if>

    <#if hasPrimaryLinks>
        <div class="nhsd-o-card-list">
            <div class="nhsd-t-grid nhsd-!t-no-gutters">
                <div class="nhsd-t-row nhsd-o-card-list__items nhsd-t-row--centred">
                    <#list document.primarySections as primarySection>
                        <#list primarySection.primarySectionsTiles as link>
                            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6">
                                <@visualhubBox link />
                            </div>
                        </#list>
                    </#list>
                </div>
            </div>
        </div>
    </#if>

    <#if !hasTabTileLinks>
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12">
                <@hst.html hippohtml=document.additionalInformation contentRewriter=brContentRewriter />
            </div>
        </div>
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12">
                <@lastModified document.lastModified false></@lastModified>
            </div>
        </div>
    </#if>
    </div >
</article>
