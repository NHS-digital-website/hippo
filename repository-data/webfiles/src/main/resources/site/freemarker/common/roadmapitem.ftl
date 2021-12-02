<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/stickyNavTags.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/contentPixel.ftl">

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

<div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
    <div class="local-header article-header article-header--detailed">
        <div class="grid-wrapper">
            <div class="article-header__inner">

                <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>

                <span data-uipath="ps.publication.information-types">
                    <#if document.markers??>
                        <#list document.markers as marker>${roadmapcategories[marker]}<#sep>, </#list>
                    </#if>
                </span>

                <hr class="hr hr--short hr--light">

                <div class="detail-list-grid">
                    <#if document.effectiveDate.dateScale?lower_case != 'future'>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key">${effectiveDateHeader}</dt>
                                    <dd class="detail-list__value"  data-uipath="roadmapitem.effectiveDateValue">
                                        <@fmt.formatDate value=document.effectiveDate.startDate.time type="Date" pattern="dd MMMM yyyy" var="startdate" timeZone="${getTimeZone()}" />
                                        <@fmt.formatDate value=document.effectiveDate.endDate.time type="Date" pattern="dd MMMM yyyy" var="enddate" timeZone="${getTimeZone()}" />
                                        ${startdate} to ${enddate} (${effectivedatestatus[document.effectiveDate.status]})
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </#if>

                    <div class="grid-row">
                        <div class="column column--reset">
                            <dl class="detail-list">
                                <dt class="detail-list__key">${standardsHeader}</dt>
                                <dd class="detail-list__value" data-uipath="roadmapitem.standardValue">

                                    <#if hasStandards>
                                        <#list document.standards as standard>
                                            <span>
                                                <a href="${standard.webLink}">${standard.referenceNumber}</a>,
                                                ${standard.body}, ${standard.name}
                                            </span><#sep><br />
                                        </#list>
                                    </#if>
                                </dd>
                            </dl>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>


<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">

        <div class="grid-row">

            <div class="column column--one-third page-block--sticky-nav page-block--sidebar article-section-nav-outer-wrapper">
                <!-- start sticky-nav -->
                <div id="sticky-nav">
                    <@stickyNavSections getStickySectionNavLinks({ "document": document, "sectons": links, "includeSummary": true })></@stickyNavSections>
                </div>
                <!-- end sticky-nav -->
            </div>

            <div class="column column--two-thirds page-block page-block--main">

                <#if hasSummaryContent>
                    <div id="${slugify(summaryHeader)}" class="article-section article-section--summary article-section--reset-top">
                        <h2>${summaryHeader}</h2>
                        <div data-uipath="roadmapitem.summary" class="article-section--summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                    </div>
                </#if>

                <#if hasSectionContent>
                    <div id="${slugify('Sections')}" class="article-section">
                        <@sections document.sections></@sections>
                    </div>
                </#if>

                <#if hasImpactedServices>
                    <div id="${slugify(impactedServicesHeader)}" class="article-section">
                        <h2>${impactedServicesHeader}</h2>
                        <#list document.impactedServices as service>
                            <span>
                                <a class="cta__title cta__button" href="<@hst.link hippobean=service/>" title="${service.title}" data-uipath="ps.search-results.result.title">
                                    ${service.title}
                                </a>
                            </span><#sep><br />
                        </#list>
                    </div>
                </#if>

                <#if hasBlocks>
                    <div id="${slugify(resourcesHeader)}" class="article-section">
                        <h2 class="cta__subtitle">${resourcesHeader}</h2>
                        <ul class="list cta-list">
                            <#list document.blocks as block>
                                <li>
                                    <article class="cta">
                                        <#if block.linkType == "internal">
                                            <h3 class="cta__meta cta__meta--reset-bottom"><a href="<@hst.link hippobean=block.link />">${block.link.title}</a></h3>
                                        <#else>
                                            <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, block.link) />
                                            <h3 class="cta__meta cta__meta--reset-bottom"><a href="${block.link}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${block.title}</a></h3>
                                        </#if>
                                    </article>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>

            </div>

        </div>
    </div>

</article>
