<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/stickyNavYears.ftl">
<#include "macro/stickyNavTags.ftl">
<#include "macro/hubBox.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/stickyGroupBlockHeader.ftl">
<#include "macro/stickyNavYears.ftl">

<@hst.setBundle basename="rb.generic.headers,rb.generic.labels,rb.doctype.supplementary-info-hub "/>

<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign monthNames = monthsOfTheYear()?reverse />

<#--Group the documents by month  -->
<#assign eventGroupHash = {} />
<#list pageable.items as item>
    <#if item.publishedDate?size gt 0>
        <@fmt.formatDate value=item.publishedDate.time type="Date" pattern="MMMM" timeZone="${getTimeZone()}" var="key" />
        <#assign eventGroupHash = eventGroupHash + {  key : (eventGroupHash[key]![]) + [ item ] } />
    </#if>
</#list>

<#-- Return the filter navigation links for the year -->
<#function getFilterYearLinks>
    <#assign links = [] />

    <#list years as key>
        <#assign typeCount = years?size />
        <#assign links += [{ "key" : key, "title": key }] />
    </#list>

    <#return links />
</#function>

<#assign hasIntroductionContent = document.introduction.content?has_content />

<article class="article article--supplementary-info-hub">
    <@documentHeader document 'supplementary-info-hub' '' '' '' '' false></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-quarter page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <div class="inner-wrapper-sticky">
                        <#-- Query search component -->
                        <div class="article-section-nav-wrapper" data-hub-filter-type="nhsd-hub-query-filter" data-hub-filter-key="query">
                            <div class="article-section-nav">
                                <h2 class="article-section-nav__title"><@fmt.message key="headers.refine" /></h2>
                                <#assign searchLink = "" />
                                <#assign searchId = "hub-search-input" />
                                <#assign searchFormId = "hub-search-form" />
                                <#assign buttonLabel><@fmt.message key="labels.filter" /></#assign>
                                <#include "../include/search-strip.ftl">
                            </div>
                        </div>

                        <#-- Year filter component -->
                        <#if getFilterYearLinks()?size gt 0>
                            <div class="article-section-nav-wrapper" data-hub-filter-type="nhsd-hub-tag-filter" data-hub-filter-key="year">
                                <@stickyNavYears getFilterYearLinks()></@stickyNavYears>
                            </div>
                        </#if>

                        <#-- Month anchor nav -->
                        <#if eventGroupHash?has_content>
                            <#assign links = [] />
                            <#list monthsOfTheYear() as month>
                                <#if eventGroupHash[month]??>
                                    <#assign links += [{ "url": "#" + slugify(month), "title": month, "aria-label": "Jump to events starting in ${month}" }] />
                                </#if>
                            </#list>
                            <div class="article-section-nav-wrapper" id="hub-search-page-contents">
                                <@stickyNavSections links></@stickyNavSections>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>

            <#-- Restore the bundle -->
            <@hst.setBundle basename="rb.generic.headers,rb.generic.labels,rb.doctype.supplementary-info-hub "/>

            <#-- @ftlvariable name="item" type="uk.nhs.digital.website.beans.SupplementaryInformation"-->
            <div class="column column--three-quarters page-block page-block--main">
                <#if hasIntroductionContent>
                <div class="article-section no-border article-section--introduction">
                    <div class="rich-text-content">
                        <@hst.html hippohtml=document.introduction contentRewriter=gaContentRewriter/>
                    </div>
                </div>
                </#if>

                <div class="hub-box-list article-section no-border no-top-margin" id="hub-search-results">
                <#if eventGroupHash?has_content>
                    <#list monthsOfTheYear() as month>
                        <#if eventGroupHash[month]??>
                            <div class="article-section article-section--letter-group" id="${slugify(month)}">
                                <@stickyGroupBlockHeader month></@stickyGroupBlockHeader>
                                <div class="grid-row">
                                    <div class="column column--reset">
                                        <#list eventGroupHash[month] as item>
                                            <@hst.link hippobean=item var="docLink" />
                                            <#assign docData = { "title": item.title, "text": item.shortsummary, "link": docLink } />

                                            <@fmt.formatDate value=item.publishedDate.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" var="date" />
                                            <#assign docData += { "date": date } />

                                            <#assign relatedDocuments = [] />
                                            <#list item.relateddocuments as doc>
                                                <@hst.link hippobean=doc var="relatedDocLink" />
                                                <#assign relatedDocuments += [{"title": doc.title, "url": relatedDocLink}] />
                                            </#list>
                                            <#assign docData += {"relatedLinks": relatedDocuments} />

                                            <@hubBox docData></@hubBox>
                                        </#list>
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </#list>
                <#else>
                    <div class="article-section">
                        <h2 class="article-header__title"><@fmt.message key="headers.no-results" /></h2>
                        <p>Would you like to <a href="${getDocumentUrl()}" aria-label="Clear filters" title="Clear filters">clear the filters</a>?</p>
                    </div>
                </#if>
                </div>

                <div class="article-section no-border no-top-margin" id="hub-search-pagination">
                    <#if pageable.totalPages gt 1>
                        <#include "../include/pagination.ftl">
                    </#if>
                </div>
            </div>
        </div>
    </div>

    <#include "scripts/hub-filter/hub-filter-controller.js.ftl"/>
</article>
