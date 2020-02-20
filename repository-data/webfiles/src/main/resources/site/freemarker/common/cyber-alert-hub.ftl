<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/stickyNavTags.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/cyberAlertBox.ftl">
<#include "macro/stickyGroupBlockHeader.ftl">
<#include "macro/stickyNavYears.ftl">

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
    <#assign year= dateString?keep_before("-") />
    <#assign month = monthNames[dateString?keep_after("-")?number-1] />
    <#assign monthYear = month + " " + year />
    <#return monthYear />
</#function>

<#--Group filter tags (severity and type)  -->
<#assign filterTypeGroupHash = {} />
<#assign filterSeverityGroupHash = {} />
<#list pageable.items as item>
    <#assign filterSeverityGroupHash = filterSeverityGroupHash + {  item.severity : (filterSeverityGroupHash[item.severity]![]) + [ item ] } />
    <#assign filterTypeGroupHash = filterTypeGroupHash + {  item.threatType  : (filterTypeGroupHash[item.threatType]![]) + [ item ] } />
</#list>

<#-- Return the type filter navigation links -->
<#function getFilterTypeGroupHash>
    <#assign links = [] />

    <#list filterTypeGroupHash?keys?sort as key>
        <#assign count = filterTypeGroupHash[key]?size />
        <#assign links += [{"key" : key, "title": key + " (${count})" }] />
    </#list>

    <#return links />
</#function>


<#-- Return the severity filter navigation links -->
<#function getFilterSeverityGroupHash>
    <#assign links = [] />

    <#list filterSeverityGroupHash?keys?sort as key>
        <#assign queryStringValue = key />

        <#assign count = filterSeverityGroupHash[key]?size />
        <#assign links += [{"key" : key, "title": key + " (${count})" }] />
    </#list>

    <#return links />
</#function>


<#-- Return the filter navigation links for the year -->
<#function getFilterYearLinks>
    <#assign links = [] />

    <#list years?keys as key>
    <#assign typeCount = years?size />
    <#assign links += [{ "key" : key, "title": key }] />
    </#list>

    <#return links />
</#function>

<article class="article article--news-hub">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--two-thirds column--reset">
                            <h1 class="local-header__title" data-uipath="document.title"><@fmt.message key="headers.page-title" /></h1>
                            <p class="article-header__subtitle"><@fmt.message key="texts.intro" /></p>
                        </div>
                        <div class="column--one-third column--reset">
                            <img src="<@hst.webfile path="images/icon-article.png" fullyQualified=true/>" alt="News article">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article">

            <div class="grid-row">
                <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                    <!-- start sticky-nav -->
                    <div id="sticky-nav">
                        <#assign affix = selectedThreattype?has_content?then("&threattype=" + selectedThreattype?join("&threattype="), "") />
                        <#assign affix += selectedSeverity?has_content?then("&severity=" + selectedSeverity?join("&severity="), "") />
                        <@stickyNavYears getFilterYearLinks() affix></@stickyNavYears>

                        <#assign links = [] />
                        <#list monthYearGroupHash?keys as k>
                            <#assign displayDate= getDisplayDate(k) />
                            <#assign links += [{ "url": "#" + slugify(displayDate), "title": displayDate, "aria-label": "Jump to items starting in ${displayDate}" }] />
                        </#list>
                        <@stickyNavSections getStickySectionNavLinks({"sections": links})></@stickyNavSections>

                        <#assign affix = "&year=" + selectedYear />
                        <#assign affix += selectedSeverity?has_content?then("&severity=" + selectedSeverity?join("&severity="), "") />
                        <@stickyNavTags getFilterTypeGroupHash() affix "Filter by type" "threattype" selectedThreattype></@stickyNavTags>

                        <#assign affix = "&year=" + selectedYear />
                        <#assign affix += selectedThreattype?has_content?then("&threattype=" + selectedThreattype?join("&threattype="), "") />
                        <@stickyNavTags getFilterSeverityGroupHash() affix "Filter by severity" "severity" selectedSeverity></@stickyNavTags>
                    </div>
                    <!-- end sticky-nav -->
                </div>

                <div class="column column--two-thirds page-block page-block--main">
                    <#if pageable?? && pageable.items?has_content>
                        <#list monthYearGroupHash?keys as key>
                            <div class="article-section article-section--letter-group">

                                <div class="grid-row" id="${slugify(getDisplayDate(key))}">
                                    <h2>${getDisplayDate(key)}</h2>
                                    <div class="column column--reset">
                                        <div class="hub-box-list">
                                            <#list monthYearGroupHash[key] as item>

                                                <#assign alertData = { "title": item.title, "text": item.shortsummary, "severity": item.severity, "threatType": item.threatType, "threatId": item.threatId } />
                                                <@hst.link hippobean=item var="itemLink" />
                                                <@fmt.formatDate value=item.publishedDate.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" var="publishedDate" />
                                                <@fmt.formatDate value=item.lastModified type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" var="lastModifiedDate" />
                                                <#assign alertData += { "link": itemLink, "publishedDate": publishedDate, "lastModifiedDate": lastModifiedDate } />

                                                <@cyberAlertBox alertData></@cyberAlertBox>
                                            </#list>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </#list>

                        <#if pageable.totalPages gt 1>
                        <div class="article-section no-border">
                            <#include "../include/pagination.ftl">
                        </div>
                        </#if>
                    <#else>
                        <div class="grid-row">
                            <div class="column column--two-thirds page-block page-block--main">
                                <p><@fmt.message key="texts.no-cyberalerts" /></p>
                            </div>
                        </div>
                    </#if>
                </div>
            </div>

    </div>
</article>
