<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/stickyGroupBlockHeader.ftl">

<#include "macro/heroes/hero.ftl">
<#include "macro/hubArticle.ftl">
<#include "macro/pagination.ftl">
<#include "macro/toolkit/organisms/filterMenu.ftl">
<@hst.setBundle basename="rb.doctype.seriesfeed"/>

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
                    <!--  Call Macro -->
                    <@nhsdFilterMenu>
                        <div id="nhsd-filter-menu" class="js-filter-list">
                            <@hst.include ref="filters"/>
                        </div>
                    </@nhsdFilterMenu>
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
