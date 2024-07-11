<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/metaTags.ftl">
<#include "../macro/contentPixel.ftl">
<#include "scrollable-filter-nav.ftl">
<#include "catalogue-entries.ftl">
<#include "../macro/svgIcons.ftl">
<#include "../../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../../nhsd-common/macro/heroes/hero.ftl">
<#include "../../nhsd-common/macro/updateSection.ftl">
<#include "../../nhsd-common/macro/pagination.ftl">

<#assign renderUrl = "uk.nhs.digital.common.components.catalogue.UrlGeneratorDirective"?new() />

<#--

&lt;#&ndash; Add meta tags &ndash;&gt;
<@metaTags></@metaTags>
&lt;#&ndash; Content Page Pixel &ndash;&gt;
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<#assign alphabetical_hash = group_blocks(flat_blocks(catalogueLinks true))/>
-->

<@hero getHeroOptions(document) />

<div
    class="nhsd-t-grid nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-7 nhsd-api-catalogue">
    <#if document.updates?has_content>
        <div class="nhsd-t-row">
            <div style="margin:0 auto;" class="nhsd-t-col-8">
                <div>
                    <@updateSection document.updates />
                </div>
            </div>
        </div>
    </#if>
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-12">
            <#if document.body?has_content??>
                <@hst.html hippohtml=document.body contentRewriter=brContentRewriter/>
            </#if>
        </div>
    </div>
   <#-- <#if alphabetical_hash??>-->
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12 nhsd-!t-margin-bottom-5">
                <h2 id="side-az-nav-heading" class="nhsd-t-heading-xs">Quick
                    navigation</h2>
               <#-- <@azList alphabetical_hash "side-az-nav-heading"/>-->
            </div>

            <div class="nhsd-t-col-3 nhsd-!t-display-hide nhsd-!t-display-l-show">
                <@hst.include ref="filters" />
            </div>

            <div class="nhsd-t-col-l-9 nhsd-t-col-m-12">
                <div class="nhsd-!t-display-l-hide">
                    <@hst.include ref="filters" />
                </div>
                <div class="nhsd-t-row <#--nhsd-!t-display-hide-->"
                     id="catalogue-search-bar">
                    <div class="nhsd-t-col-l-8 nhsd-!t-padding-left-0">
                        <div class="nhsd-t-float-left">
                            <h6 class="nhsd-t-heading-xs nhsd-!t-margin-bottom-2">
                                Search</h6>
                        </div>
                        <div class=" nhsd-!t-padding-0 nhsd-!t-margin-bottom-2">
                            <div class="nhsd-t-form-control">
                                <@hst.renderURL fullyQualified=true var="searchUrlLink" />
                                <form action="${searchUrlLink}" method="get">
                                <input
                                    class="nhsd-t-form-input"
                                    type="text"
                                    id="catalogue-search-bar-input"
                                    name="query"
                                    autocomplete="off"
                                    placeholder="What are you looking for today?"
                                    aria-label="Keywords"
                                    value="${currentQuery}"
                                />
                                <span class="nhsd-t-form-control__button">
                                    <button  class="nhsd-a-button nhsd-a-button--circle" type="submit" aria-label="Search">
                                      <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                      <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet"
                                           focusable="false" viewBox="0 0 16 16"><path
                                              d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/></svg>
                                      </span>
                                    </button>
                                </span>
                                </form>
                                <script
                                    src="<@hst.webfile path="/apicatalogue/apicatalogue.js"/>"></script>
                            </div>
                        </div>
                    </div>
                    <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xs"/>
                </div>
                <div class="nhsd-t-row nhsd-!t-padding-top-1">
                    <h6 class="nhsd-t-heading-xs nhsd-!t-margin-bottom-0"
                        id="search-results-count">${totalAvailable}
                        results </h6>
                </div>
               <#-- <#if retiredFilterEnabled>-->
                    <div class="nhsd-t-row">
                        <div class="nhsd-t-col-6 nhsd-!t-padding-left-0">
                            <label aria-label="Include retired APIs and API standards"
                                   class="nhsd-m-selector-toggle-card nhsd-!t-padding-left-0">
                                <div class="nhsd-a-box nhsd-!t-padding-left-0">
                                    <span class="nhsd-m-selector-toggle-card__toggle">
                                        <div class="nhsd-a-selector-toggle">
                                            <#assign currentQuery1 = "">
                                            <#assign currentQuery1 = currentQuery>
                                            <@hst.renderURL fullyQualified=true var="link" />

                                            <#assign fullURL = updateQueryParamsAndUrl(currentQuery1 showRetired) />

                                            <a onclick=""
                                               href="${fullURL}"
                                               class="nhsd-a-checkbox__label nhsd-t-body-s">
                                                <input type="checkbox"
                                                       <#if showRetired>checked</#if> />
                                                <span class="slider"></span>
                                            </a>
                                        </div>
                                    </span>

                                    <p class="nhsd-t-body-s">Include retired APIs and
                                        API standards</p>
                                </div>
                            </label>
                        </div>
                    </div>
                <#--<#else>-->
                    <div class="nhsd-!t-padding-top-4"/>
                <#--</#if>-->

               <#-- <@apiCatalogueEntries alphabetical_hash filtersModel></@apiCatalogueEntries>-->

                    <div id="list-page-results-list" class="nhsd-!t-margin-bottom-9">
                    <#list pageable.items as document>
                       <#-- ${document.shortsummary}-->
                        <div class="nhsd-t-flex-item--grow">
                            <div data-api-catalogue-entry="">
                                <#list document.keys as key>
                                    <#if apiStatusEntries[key]?exists>
                                        <@hst.renderURL fullyQualified=true var="link" />
                                        <#assign taxonomyPath = "/Taxonomies/${key}">
                                        <#assign updatedLink = updateOrRemoveLinkWithTaxonomyPath(link, taxonomyPath) />
                                        <#if link?contains(taxonomyPath)>
                                            <a title="Remove ${apiStatusEntries[key].displayName} filter" href="${updatedLink}" style="line-height:1; text-decoration:none" class="nhsd-a-tag nhsd-a-tag--bg-${apiStatusEntries[key].highlight} nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${apiStatusEntries[key].displayName}</a>
                                        <#else>
                                            <a title="Filter by ${apiStatusEntries[key].displayName}" href="${updatedLink}" style="line-height:1; text-decoration:none" class="nhsd-a-tag nhsd-a-tag--bg-${apiStatusEntries[key].highlight} nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${apiStatusEntries[key].displayName}</a>
                                        </#if>
                                    </#if>
                                </#list>

                                <@hst.link hippobean=document var="link1"/>

                                <h2 class="nhsd-t-heading-xs nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-1" id="${document.title?lower_case?replace(" ", "-")}">
                                    <#if link1?has_content>
                                        <a href="${link1}" class="nhsd-a-link" data-filterable>${document.title}</a>
                                    <#else>
                                        ${document.title}
                                    </#if>
                                </h2>

                                <p class="nhsd-t-body" data-filterable="">${document.shortsummary}</p>
                                <#list document.keys as key>
                                    <#if !(apiStatusEntries[key]?exists)>
                                        <@hst.renderURL fullyQualified=true var="link" />
                                        <#assign taxonomyPath = "/Taxonomies/${key}">
                                        <#assign updatedLink = updateOrRemoveLinkWithTaxonomyPath(link, taxonomyPath) />
                                        <#if link?contains(taxonomyPath)>
                                            <a title="Remove ${key} filter" href="${updatedLink}" style="line-height:1; text-decoration:none" class="nhsd-a-tag filter-tag-yellow-highlight nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${key}</a>
                                        <#else >
                                            <a title="Filter by key ${key}" href="${updatedLink}" style="line-height:1; text-decoration:none" class="nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${key}</a>
                                        </#if>
                                    </#if>
                                </#list>
                            </div>
                        </div>

                        <hr class="nhsd-a-horizontal-rule">
                        <#--</#if>-->
                    </#list>
                    </div>

                <#if document.entriesFooterContentTitle?has_content?? && document.entriesFooterContentBody?has_content??>
                    <@entriesFooter document.entriesFooterContentTitle document.entriesFooterContentBody/>
                </#if>
            </div>
          <#if pageable?? && pageable.total gt 0>
               <#-- <hr class="nhsd-a-horizontal-rule">-->
                <@pagination />
            </#if>
        </div>
    <#--</#if>-->

</div>

<#macro entriesFooter title body>
    <div class="nhsd-m-emphasis-box" id="entriesFooter">
        <div class="nhsd-a-box nhsd-a-box--border-grey">
            <div class="nhsd-m-emphasis-box__content-box">
                <h1 class="nhsd-t-heading-s nhsd-t-word-break">${title}</h1>
                <p class="nhsd-t-body-s nhsd-t-word-break"><@hst.html hippohtml=body contentRewriter=brContentRewriter/></p>
            </div>
        </div>
    </div>
</#macro>

<#function updateOrRemoveLinkWithTaxonomyPath link taxonomyPath>
    <#-- Split the link into base URL and query parameters -->
    <#assign parts = link?split("?", 2)>
    <#assign baseURL = parts[0]>
    <#assign queryParams = parts[1]?if_exists>

    <#-- Check if the base URL contains the taxonomy path -->
    <#if baseURL?contains(taxonomyPath)>
    <#-- Remove the taxonomy path from the base URL -->
        <#assign updatedBaseURL = baseURL?replace(taxonomyPath, "")>
    <#else>
    <#-- Append the taxonomy path to the base URL -->
        <#assign updatedBaseURL = baseURL + taxonomyPath>
    </#if>

    <#-- Reassemble the updated link with query parameters if they exist -->
    <#assign updatedLink = updatedBaseURL + (queryParams?has_content?then("?", "") + queryParams)>

    <#-- Output the updated link without escaping HTML special characters -->
    <#return updatedLink?no_esc>
</#function>


<#function updateQueryParamsAndUrl currentQuery1 showRetired>
<#-- Check if the link contains '?query=' and remove it if it does -->
    <#assign queryParams = "?query=">
    <#assign query = "?">
    <#if link?contains(queryParams)>
    <#-- Extract the part before '?query=' -->
        <#assign link = link?substring(0, link?index_of(queryParams))>
        <#assign query = "?query=">
        <#else>
    </#if>

<#-- Construct the URL with the new query parameters -->
    <#assign urlWithQueryParams = link + query + currentQuery1/>
    <#assign fullURL = showRetired?then(urlWithQueryParams, urlWithQueryParams + "&showRetired") />

<#-- Output the updated URL -->
    <#return fullURL?no_esc>
</#function>

<@hst.setBundle basename="month-names"/>

