<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/stickyNavTags.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/heroes/hero.ftl">
<#include "macro/heroes/hero-options.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.doctype.roadmap,publicationsystem.headers"/>

<@fmt.message key="headers.summary" var="summaryHeader" />
<@fmt.message key="headers.standards" var="standardsHeader" />
<@fmt.message key="headers.effective-date" var="effectiveDateHeader" />
<@fmt.message key="headers.impacted-services" var="impactedServicesHeader" />
<@fmt.message key="headers.resources" var="resourcesHeader" />

<#assign hasSummaryContent = document.summary?? && document.summary?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasStandards = document.standards?? && document.standards?has_content />
<#assign hasImpactedServices = document.impactedServices?? && document.impactedServices?has_content />
<#assign hasBlocks = document.blocks?? && document.blocks?size!=0 />

<#assign links = [] />
<#if hasImpactedServices><#assign links += [{ "url": "#" + slugify(impactedServicesHeader), "title": impactedServicesHeader }] /></#if>
<#if hasBlocks><#assign links += [{ "url": "#" + slugify(resourcesHeader), "title": resourcesHeader }] /></#if>

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<#assign heroType = "default" />
<#assign heroOptions = getHeroOptions(document) />
<#assign heroOptions += {
    "colour": "darkBlue"
}/>
<@hero heroOptions heroType>
    <dl class="nhsd-o-hero__meta-data">
        <#if document.effectiveDate.dateScale?lower_case != 'future'>
            <div class="nhsd-o-hero__meta-data-item">
                <dt class="nhsd-o-hero__meta-data-item-title">${effectiveDateHeader}</dt>
                <dd class="nhsd-o-hero__meta-data-item-description" data-uipath="roadmapitem.effectiveDateValue">
                    <@fmt.formatDate value=document.effectiveDate.startDate.time type="Date" pattern="dd MMMM yyyy" var="startdate" timeZone="${getTimeZone()}" />
                    <@fmt.formatDate value=document.effectiveDate.endDate.time type="Date" pattern="dd MMMM yyyy" var="enddate" timeZone="${getTimeZone()}" />
                    ${startdate} to ${enddate} (${effectivedatestatus[document.effectiveDate.status]})
                </dd>
            </div>
        </#if>

        <div class="nhsd-o-hero__meta-data-item">
            <dt class="nhsd-o-hero__meta-data-item-title">${standardsHeader}</dt>
            <dd class="nhsd-o-hero__meta-data-item-description" data-uipath="roadmapitem.standardValue">
                <#if hasStandards>
                    <#list document.standards as standard>
                        <span>
                        <a class="nhsd-a-link" href="${standard.webLink}">${standard.referenceNumber}</a>,
                        ${standard.body}, ${standard.name}
                        </span><#sep><br />
                    </#list>
                </#if>
            </dd>
        </div>
    </dl>
</@hero>


<article class="nhsd-t-grid nhsd-!t-margin-top-8 nhsd-!t-margin-bottom-6">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-m-3 nhsd-t-col-xs-12 nhsd-!t-margin-bottom-2">
            <!-- start sticky-nav -->
            <div id="sticky-nav">
                <@stickyNavSections getStickySectionNavLinks({ "document": document, "sectons": links, "includeSummary": true })></@stickyNavSections>
            </div>
            <!-- end sticky-nav -->
        </div>

        <div class="nhsd-t-col-m-8 nhsd-t-off-m-1 nhsd-t-col-xs-12">
            <#if hasSummaryContent>
                <div id="${slugify(summaryHeader)}" class="nhsd-!t-margin-bottom-6">
                    <h2 class="nhsd-t-heading-xl">${summaryHeader}</h2>
                    <div data-uipath="roadmapitem.summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                </div>
            </#if>

            <#if hasSectionContent>
                <div id="${slugify('Sections')}" class="article-section">
                    <@sections document.sections></@sections>
                </div>
            </#if>

            <#if hasImpactedServices>
                <div id="${slugify(impactedServicesHeader)}">
                    <h2 class="nhsd-t-heading-l">${impactedServicesHeader}</h2>
                    <#list document.impactedServices as service>
                        <span>
                            <a class="nhsd-a-link" href="<@hst.link hippobean=service/>" data-uipath="ps.search-results.result.title">
                                ${service.title}
                            </a>
                        </span><#sep><br />
                    </#list>
                </div>
            </#if>

            <#if hasBlocks>
                <div id="${slugify(resourcesHeader)}">
                    <h2 class="nhsd-t-heading-m">${resourcesHeader}</h2>
                    <ul class="nhsd-t-list--bullet">
                        <#list document.blocks as block>
                            <#if block?has_content && block.link?has_content>
                                <li>
                                    <#if block.linkType == "internal">
                                        <a class="nhsd-a-link" href="<@hst.link hippobean=block.link />">${block.link.title}</a>
                                    <#else>
                                        <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, block.link) />
                                        <a class="nhsd-a-link" href="${block.link}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${block.title}</a>
                                    </#if>
                                </li>
                            </#if>
                        </#list>
                    </ul>
                </div>
            </#if>
        </div>
    </div>
</article>
