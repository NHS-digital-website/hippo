<#ftl output_format="HTML">
<#setting url_escaping_charset="UTF-8">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.FeedHub" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/hubArticle.ftl">
<#include "macro/pagination.ftl">
<#include "macro/heroes/hero-options.ftl">
<#include "macro/heroes/hero.ftl">
<#include "macro/toolkit/organisms/filterMenu.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<article itemscope itemtype="http://schema.org/SearchResultsPage">
	<#assign heroOptions = getHeroOptions(document) />
	<#assign heroOptions += {"colour": "darkBlue"}/>
	<#assign heroType = heroOptions.image?has_content?then("image", "default")>
	<@hero heroOptions heroType />
	<br/>

    <#if document.title?has_content>
        <div class="nhsd-!t-display-hide" itemprop="publisher" itemscope itemtype="http://schema.org/Organization"><span itemprop="name">${document.title}</span></div>
    </#if>
    <#if document.seosummary?has_content><div class="nhsd-!t-display-hide" itemprop="description">${document.seosummary.content?replace('<[^>]+>','','r')}</div></#if>

    <#-- Content Page Pixel -->
    <@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

    <div id="feedhub-content" class="nhsd-t-grid nhsd-!t-margin-bottom-6">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-3 nhsd-!t-display-hide nhsd-!t-display-l-show">
                <div class="">
                    <!-- Call Macro-->
                    <@nhsdFilterMenu false filters/>
                </div>
            </div>

            <div class="nhsd-t-off-l-1 nhsd-t-col-l-8 nhsd-t-col-xs-12">
                <div class="nhsd-m-search-bar nhsd-m-search-bar__full-width nhsd-m-search-bar__small nhsd-!t-margin-bottom-4 nhsd-!t-display-l-hide">
                    <div class="nhsd-t-form-control">
                        <input
                            class="nhsd-t-form-input js-feedhub-query"
                            type="text"
                            value="${query}"
                            autocomplete="off"
                            placeholder="Filter search..."
                            aria-label="Filter search..."
                        />
                        <span class="nhsd-t-form-control__button">
                            <button class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent" type="submit" aria-label="Perform search">
                                <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                        <path d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/>
                                    </svg>
                                </span>
                            </button>
                        </span>
                    </div>
                </div>

                <div class="js-filter-tags">
                    <#if feed??>
                        <span class="nhsd-t-heading-s">${feed?size} result${(feed?size gt 1)?then('s', '')}</span>
                    </#if>
                    <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s" />

                    <#if filterCount gt 0>
                        <span class="nhsd-t-body nhsd-!t-col-dark-grey">Filters (${filterCount})</span>
                        <ul class="nhsd-m-tag-list nhsd-!t-margin-bottom-2 nhsd-!t-margin-top-2">
                            <#list filterValues as key, optionValues>
                                <#list optionValues?sort as optionValue>
                                    <#assign filterUrl = hstRequestContext.baseURL.contextPath + hstRequestContext.baseURL.requestPath + "?" />
                                    <#if query?has_content>
                                        <#assign filterUrl = filterUrl + "query=" + query />
                                    </#if>
                                    <#list filterValues as key2, optionValues2>
                                        <#list optionValues2 as optionValue2>
                                            <#if key != key2 || optionValue != optionValue2>
                                                <#assign filterUrl = filterUrl + key2?url + "=" + optionValue2 + "&" />
                                            </#if>
                                        </#list>
                                    </#list>
                                    <li>
                                        <a href="${filterUrl}" class="nhsd-a-tag nhsd-a-tag--closable js-filter-tag" data-filter-id="${key}-${optionValue}">
                                            <#if topicMap[optionValue]?has_content>
                                                ${topicMap[optionValue]}
                                            <#else>
                                                ${optionValue}
                                            </#if>
                                            <span class="nhsd-a-icon nhsd-a-icon--size-xxs nhsd-a-icon--col-blue">
                                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                                    <polygon points="13.9,1 8,6.9 2.1,1 1,2.1 6.9,8 1,13.9 2.1,15 8,9.1 13.9,15 15,13.9 9.1,8 15,2.1 "/>
                                                </svg>
                                            </span>
                                        </a>
                                    </li>
                                </#list>
                            </#list>
                        </ul>
                    </#if>
                </div>

                <span class="js-loading-text nhsd-t-heading-s nhsd-!t-col-dark-grey nhsd-!t-display-hide nhsd-!t-margin-bottom-2">Loading...</span>

                <div class="nhsd-t-grid nhsd-t-grid--nested">
                    <div class="nhsd-t-row">
                        <div class="nhsd-t-col-3">
                            <label class="nhsd-t-body nhsd-!t-col-dark-grey" for="sort-by-input">Sort by</label>
                            <div class="nhsd-t-form-group nhsd-!t-margin-top-2 nhsd-!t-margin-bottom-0">
                                <div class="nhsd-t-form-control">
                                <#-- TODO - file ${filterOption} <select form="feed-list-filter" name="sort" id="sort-by-input" data-input-id="${filterKey}-${filterOption}" class="nhsd-t-form-select-s"> -->
                                    <select form="feed-list-filter" name="sort" id="sort-by-input" class="nhsd-t-form-select-s">
                                        <option value="date-desc" ${(sort == "date-desc")?then("selected", "")}>Latest</option>
                                        <option value="date-asc" ${(sort == "date-asc")?then("selected", "")}>Oldest</option>
                                        <#if document.contentType == "website:feedhub" >
                                            <option value="title-desc" ${(sort == "title-desc")?then("selected", "")}>Z-A</option>
                                            <option value="title-asc" ${(sort == "title-asc")?then("selected", "")}>A-Z</option>
                                        </#if>
                                    </select>
                                    <span class="nhsd-t-form-control__icon">
                                        <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                                <path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"></path>
                                            </svg>
                                        </span>
                                      </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="js-feed-content">
                    <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s nhsd-!t-margin-bottom-6">
                    <#if feed?has_content>
                        <@hubArticles pageable.items true/>
                    <#else>
                        No results
                    </#if>

                    <#if pageable?? && pageable.total gt 0>
                        <hr class="nhsd-a-horizontal-rule">
                        <@pagination />
                    </#if>
                </div>
            </div>
        </div>
    </div>
</article>