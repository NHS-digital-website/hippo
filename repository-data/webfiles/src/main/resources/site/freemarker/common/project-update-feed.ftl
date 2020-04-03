<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/stickyNavYears.ftl">
<#include "macro/stickyNavTags.ftl">
<#include "macro/hubBox.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/stickyGroupBlockHeader.ftl">
<#include "macro/stickyNavYears.ftl">
<#include "macro/sections/sections.ftl">

<@hst.setBundle basename="rb.generic.headers,rb.generic.labels,rb.doctype.projectupdate "/>

<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign monthNames = monthsOfTheYear()?reverse />

<#--Group the documents by month  -->
<#assign eventGroupHash = {} />
<#list pageable.items as item>
    <#if item.publiclyAccessible && item.updateTimestamp?size gt 0>
        <@fmt.formatDate value=item.updateTimestamp.time type="Date" pattern="MMMM" timeZone="${getTimeZone()}" var="key" />
        <#assign eventGroupHash = eventGroupHash + {  key : (eventGroupHash[key]![]) + [ item ] } />
    </#if>
</#list>

<#assign earlyAccessKey = hstRequest.request.getParameter("key")!''>
<#assign hasIntroductionContent = document.optionalIntroductoryText?has_content />

<article class="article">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--with-icon" >
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--two-thirds column--reset">
                            <h1 id="top" class="local-header__title" data-uipath="document.title">${document.title}</h1>
                            <div class="article-header__subtitle" data-uipath="website.project-update-feed.summary">
                                <@hst.html hippohtml=document.summary />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

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

                        <#-- Month anchor nav -->
                        <#if eventGroupHash?has_content>
                            <#assign links = [] />
                            <#list monthNames as month>
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
            <@hst.setBundle basename="rb.generic.headers,rb.generic.labels,rb.doctype.projectupdate "/>

            <#-- @ftlvariable name="item" type="uk.nhs.digital.website.beans.SupplementaryInformation"-->
            <div class="column column--three-quarters page-block page-block--main">
                <#if hasIntroductionContent>
                <div class="article-section no-border article-section--introduction">
                    <div class="rich-text-content">
                        <@hst.html hippohtml=document.optionalIntroductoryText contentRewriter=gaContentRewriter/>
                    </div>
                </div>
                </#if>

                <div class="hub-box-list article-section no-border no-top-margin" id="hub-search-results">
                <#if eventGroupHash?has_content>
                    <#list monthNames as month>
                        <#if eventGroupHash[month]??>
                            <div class="article-section article-section--letter-group" id="${slugify(month)}">
                                <@stickyGroupBlockHeader month></@stickyGroupBlockHeader>
                                <div class="grid-row">
                                    <div class="column column--reset">
                                        <#list eventGroupHash[month] as item>
                                            <@hst.link hippobean=item var="docLink" >
                                                <#if earlyAccessKey?has_content><@hst.param name="key" value="${earlyAccessKey}"/></#if>
                                            </@hst.link>
                                            <#assign docData = { "title": item.title, "htmlText": item.shortSummaryHtml, "link": docLink } />

                                            <@fmt.formatDate value=item.updateTimestamp.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" var="date" />
                                            <#assign docData += { "date": date } />

                                            <#assign metaData = {}/>
                                            <#assign metaData += {"itemscope":"itemscope itemtype=http://schema.org/SpecialAnnouncement"}/>
                                            <#assign metaData += {"name":"itemProp=name"}/>
                                            <#assign metaData += {"description":"itemProp=description", "descriptionValue":item.seosummary}/>
                                            <#assign metaData += {"datePublished":"itemProp=datePublished"}/>
                                            <#assign metaData += {"category":"itemProp=category"}/>
                                            <#assign metaData += {"text":"itemProp=text"}/>
                                            <#assign metaData += {"spatialCoverage":"itemProp=spatialCoverage"}/>

                                            <#if item.typeOfUpdate?has_content>
                                                <#assign docData += {"types": [item.typeOfUpdate]} />
                                                <#assign metaData += {"about":"itemProp=about"}/>
                                            </#if>

                                            <#assign hiddenSchemaList = []/>
                                            <#--schema:description-->
                                            <#if item.seosummary?has_content>
                                                <#assign hiddenSchemaList += [{"prop":"description", "value":item.seosummary.content?replace('<[^>]+>','','r')}]/>
                                            </#if>
                                            <#--schema:category-->
                                            <#if item.wikiLink?has_content>
                                                <#assign hiddenSchemaList += [{"prop":"category", "value":item.wikiLink}]/>
                                            </#if>
                                            <#--schema:about-->
                                            <#if item.serviceAffected?has_content>
                                                <#if item.serviceAffected[0].linkType == "internal">
                                                    <@hst.link hippobean=item.serviceAffected[0].link fullyQualified=true var="link"/>
                                                    <#assign about = link />
                                                <#else>
                                                    <#assign about = item.serviceAffected[0].link />
                                                </#if>
                                                <#assign hiddenSchemaList += [{"prop":"about", "value":about}]/>
                                            </#if>
                                            <#--schema:{typeOfUpdate}-->
                                            <#if item.typeOfUpdate?has_content>
                                                <@hst.link var="itemUrl" hippobean=item fullyQualified=true/>
                                                <#assign hiddenSchemaList += [{"prop": typeOfUpdateTag(item.typeOfUpdate), "value":itemUrl}]/>
                                            </#if>
                                            <#--schema:datePosted-->
                                            <#assign hiddenSchemaList += [{"prop":"datePosted", "value":date}]/>
                                            <#--schema:spatialCoverage-->
                                            <#assign hiddenSchemaList += [{"prop":"spatialCoverage", "value":"England"}]/>
                                            <#--schema:keyword-->
                                            <#assign hiddenSchemaList += [{"prop":"keyword", "value":item.keys?join(",")}]/>

                                            <@hubBox docData metaData hiddenSchemaList></@hubBox>
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
