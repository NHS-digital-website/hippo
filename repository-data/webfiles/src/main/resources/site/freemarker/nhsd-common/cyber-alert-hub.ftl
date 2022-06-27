<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/cyberAlertBox.ftl">
<#include "macro/stickyGroupBlockHeader.ftl">

<#include "macro/heroes/hero.ftl">
<#include "macro/hubArticle.ftl">
<#include "macro/pagination.ftl">

<@hst.setBundle basename="rb.doctype.cyberalerts"/>

<#assign overridePageTitle><@fmt.message key="headers.page-title" /></#assign>
<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign monthNames = monthsOfTheYear() />

<#--Group the cyber alerts by published date (month/year)  -->
<#--Date pattern used for sorting, with getDisplayDate() used for display  -->

<#assign monthYearGroupHash = {} />
<#list pageable.items as item>
    <@fmt.formatDate value=item.publishedDate.time type="Date" pattern="yyyy-MM" var="monthYear" timeZone="${getTimeZone()}" />
    <#assign monthYearGroupHash = monthYearGroupHash + {  monthYear : (monthYearGroupHash[monthYear]![]) + [ item ] } />
</#list>

<#function getDisplayDate dateString>
    <#assign year = dateString?keep_before("-") />
    <#assign month = monthNames[dateString?keep_after("-")?number-1] />
    <#assign monthYear = month + " " + year />
    <#return monthYear />
</#function>

<#assign heroOptions = {"colour": "darkBlue",
                        "title": overridePageTitle
                        }/>
                        
<#if !query??>
    <#assign query = "" />
</#if>

<article class="article article--news-hub">
    <@hero heroOptions "default" />
    <br/>
    
    <div id="feedhub-content" class="nhsd-t-grid nhsd-!t-margin-bottom-6">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-3 nhsd-!t-display-hide nhsd-!t-display-l-show">
                <div class="">
                    <form id="feed-list-filter" method="get">
                        <div class="nhsd-o-filter-menu__search-bar">
                            <div class="nhsd-m-search-bar nhsd-m-search-bar__full-width nhsd-m-search-bar__small">
                                <div class="nhsd-t-form-control">
                                    <input
                                        class="nhsd-t-form-input js-feedhub-query"
                                        type="text"
                                        name="query"
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
                        </div>
                        <div id="nhsd-filter-menu" class="js-filter-list">
                            <@hst.include ref="filters"/>
                        </div>
                        <div class="nhsd-o-filter-menu__filter-button">
                            <button class="nhsd-a-button" type="submit"><span class="nhsd-a-button__label">Filter results</span></button>
                        </div>
                    </form>
                </div>
            </div>
            <div class="nhsd-t-off-l-1 nhsd-t-col-l-8 nhsd-t-col-xs-12">
                <div class="js-filter-tags">
                    <span class="nhsd-t-heading-s">${pageable.total} result${(pageable.total gt 1)?then('s', '')}</span>
                </div>

                <span class="js-loading-text nhsd-t-heading-s nhsd-!t-col-dark-grey nhsd-!t-display-hide nhsd-!t-margin-bottom-2">Loading...</span>

                <div class="js-feed-content">
                    <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s nhsd-!t-margin-bottom-6">

                    <#if monthYearGroupHash?has_content>

                        <#list monthYearGroupHash?keys as key>
                            <#assign alertData = [] />
                            <#list monthYearGroupHash[key] as item>
                                <#assign alertData = alertData + [item]>
                            </#list>
                            <h2>${getDisplayDate(key)}</h2>
                            <@hubArticles alertData true/>
                        </#list>

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
