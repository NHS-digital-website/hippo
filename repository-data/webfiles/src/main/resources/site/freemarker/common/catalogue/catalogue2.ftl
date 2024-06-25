<#include "../../include/imports.ftl">
<#include "../macro/metaTags.ftl">
<#include "../macro/contentPixel.ftl">
<#include "scrollable-filter-nav.ftl">
<#include "catalogue-entries.ftl">
<#include "../macro/svgIcons.ftl">
<#include "../../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../../nhsd-common/macro/heroes/hero.ftl">
<#include "../../nhsd-common/macros/az-nav.ftl">
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
                <@azList alphabetical_hash "side-az-nav-heading"/>
            </div>

            <div class="nhsd-t-col-3 nhsd-!t-display-hide nhsd-!t-display-l-show">
                <@hst.include ref="filters" />
            </div>

            <div class="nhsd-t-col-l-9 nhsd-t-col-m-12">
                <div class="nhsd-!t-display-l-hide">
                    <@hst.include ref="filters" />
                </div>
                <div class="nhsd-t-row nhsd-!t-display-hide"
                     id="catalogue-search-bar">
                    <div class="nhsd-t-col-l-8 nhsd-!t-padding-left-0">
                        <div class="nhsd-t-float-left">
                            <h6 class="nhsd-t-heading-xs nhsd-!t-margin-bottom-2">
                                Search</h6>
                        </div>
                        <div class=" nhsd-!t-padding-0 nhsd-!t-margin-bottom-2">
                            <div class="nhsd-t-form-control">
                                <input
                                    class="nhsd-t-form-input"
                                    type="text"
                                    id="catalogue-search-bar-input"
                                    name="query"
                                    autocomplete="off"
                                    placeholder="What are you looking for today?"
                                    aria-label="Keywords"
                                />
                                <span class="nhsd-t-form-control__button">
                                    <button
                                        class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent nhsd-!t-display-hide"
                                        type="submit" data-clear-button
                                        aria-label="Clear">
                                        <span
                                            class="nhsd-a-icon nhsd-a-icon--size-s">
                                        <svg xmlns="http://www.w3.org/2000/svg"
                                             preserveAspectRatio="xMidYMid meet"
                                             focusable="false" viewBox="0 0 16 16"><polygon
                                                points="13.9,1 8,6.9 2.1,1 1,2.1 6.9,8 1,13.9 2.1,15 8,9.1 13.9,15 15,13.9 9.1,8 15,2.1 "/></svg>
                                        </span>
                                    </button>
                                </span>
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
                        results</h6>
                </div>
               <#-- <#if retiredFilterEnabled>-->
                    <div class="nhsd-t-row">
                        <div class="nhsd-t-col-6 nhsd-!t-padding-left-0">
                            <label aria-label="Include retired APIs and API standards"
                                   class="nhsd-m-selector-toggle-card nhsd-!t-padding-left-0">
                                <div class="nhsd-a-box nhsd-!t-padding-left-0">
                                    <span class="nhsd-m-selector-toggle-card__toggle">
                                        <div class="nhsd-a-selector-toggle">
                                            <#assign hstRequestContext = requestContext>
                                            <#assign contextPath = hstRequestContext.baseURL.contextPath>
                                            <#assign requestPath = hstRequestContext.baseURL.requestPath>
                                            <#assign fullURL = contextPath + requestPath +"?r120_r1:page=1"/>
                                            <#assign queryParams = showRetired?then("${fullURL}","${fullURL}&showRetired") />
                                            <input type="hidden" name="hiddenVar" value="${queryParams}">
                                        <@hst.link var="baseUrl"/>
                                            <a onclick=""
                                               href="${queryParams}"
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

                            <#--<#assign containsRetiredApi = false>
                            <#list document.keys as key>
                                <#if key == "retired-api">
                                    <#assign containsRetiredApi = true>
                                    <#break>
                                </#if>
                            </#list>

                        <#if !containsRetiredApi || showRetired>-->

                        <div class="nhsd-t-flex-item--grow">
                            <div data-api-catalogue-entry="">
                                <#list document.keys as key>
                                <a title="Filter by In production" href="/developer/api-catalogue?filter=access-to-records_1&amp;filter=community-health-care&amp;filter=in-production" style="line-height:1; text-decoration:none" class="nhsd-a-tag nhsd-a-tag--bg-light-green nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${key}</a>
                               <#-- <a title="Filter by Under review for deprecation" href="/developer/api-catalogue?filter=access-to-records_1&amp;filter=community-health-care&amp;filter=under-review-for-deprecation" style="line-height:1; text-decoration:none" class="nhsd-a-tag nhsd-a-tag--bg-light-red nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">Under review for deprecation</a>-->

                                </#list>
                                <h2 class="nhsd-t-heading-xs nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-1" id="access-control-service-hl7-v3-api">
                                    <a href="/developer/api-catalogue/access-control-service-hl7-v3" class="nhsd-a-link" data-filterable="">${document.title} </a>
                                </h2>

                                <p class="nhsd-t-body" data-filterable="">${document.shortsummary}</p>
                                <#--<a title="Remove Access to records filter" href="/developer/api-catalogue?filter=community-health-care" style="line-height:1; text-decoration:none" class="nhsd-a-tag filter-tag-yellow-highlight nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">Access to records</a>-->

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

<@hst.setBundle basename="month-names"/>
<#if facets??>
    <@hst.link var="baseLink" hippobean=facets />
    <#list facets.folders as facet>
        <div class="article-section-nav-wrapper faceted-nav-facet">
            <div class="js-filter-list">
                <#assign filterKey = facet.name />
                <#assign filterName = facet.displayName?cap_first />
                <div class="nhsd-m-filter-menu-section ${facet.folders[0].leaf?then('nhsd-m-filter-menu-section--active', '')}" data-active-menu="${filterKey}">
                    <div class="nhsd-m-filter-menu-section__accordion-heading">
                        <button class="nhsd-m-filter-menu-section__menu-button" type="button">
                                <span class="nhsd-m-filter-menu-section__heading-text nhsd-t-body-s">
                                    <span>
                                        <span class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                                <path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"/>
                                            </svg>
                                        </span>
                                    </span>
                                    ${filterName}
                                </span>
                        </button>
                        <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xxs" />

                        <div class="nhsd-m-filter-menu-section__accordion-panel">
                            <#list facet.folders as value>
                                <#assign valueName = value.displayName />
                                <div class="nhsd-m-filter-menu-section__option-row">
                                    <span class="nhsd-a-checkbox">
                                        <#assign filterName = facet.name?replace(' ', '+')/>
                                        <#assign filterValue = valueName?replace(' ', '+')/>
                                        <#assign newLink = baseLink + "/" + filterName + "/" + filterValue />
                                        <#if value.leaf>
                                            <#assign newLink = newLink?replace('/${filterName}/${filterValue}', '', 'i')>
                                        </#if>
                                        <label>
                                            <#assign newLink = newLink + query?has_content?then("?query=" + query, "") />

                                            <#if filterKey="year">
                                                <#assign redirect = newLink?replace('month/(?:[0-9]|1[0-1])/?', '', 'ir')/>
                                            <#else>
                                                <#assign redirect = newLink />
                                            </#if>
                                            ${valueName} (${value.count})
                                            <#if facet.name="month">
                                                <@fmt.message key=value.name var="monthName"/>
                                                <#assign valueName=monthName/>
                                            </#if>
                                            <input name="${filterKey}" type="checkbox"
                                                   data-input-id="${filterKey}-${valueName}"
                                                   class="js-filter-checkbox"
                                                   value="${valueName}"
                                                ${value.leaf?then('checked=checked', '')}
                                                onClick="window.location='${redirect}'"
                                            />
                                            <span class="nhsd-a-checkbox__label nhsd-t-body-s">${valueName} (${value.count})</span>
                                            <span class="checkmark"></span>

                                        </label>
                                    </span>
                                </div>
                            </#list>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </#list>
</#if>
