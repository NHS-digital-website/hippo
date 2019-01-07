<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "macro/sectionNav.ftl">
<#include "macro/hubBox.ftl">
<#include "macro/stickyGroupBlockHeader.ftl">

<@hst.setBundle basename="rb.doctype.news-hub"/>

<#assign overridePageTitle><@fmt.message key="headers.page-title" /></#assign>
<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign monthNames = monthsOfTheYear()?reverse />

<#--Group the news articles by earliest start month  -->
<#assign newsGroupHash = {} />
<#list pageable.items as item>
    <@fmt.formatDate value=item.publisheddatetime.time type="Date" pattern="MMMM" timeZone="${getTimeZone()}" var="key" />
    <#assign newsGroupHash = newsGroupHash + {  key : (newsGroupHash[key]![]) + [ item ] } />
</#list>

<#-- Return the section navigation links for the months -->
<#function getSectionNavLinks>
    <#assign links = [] />
    
    <#list monthNames as month>
        <#if newsGroupHash[month]??>
            <#assign links += [{ "url": "#" + slugify(month), "title": month, "aria-label": "Jump to events starting in ${month}" }] />
        </#if>
    </#list>  
    
    <#return links />
</#function>

<article class="article article--news-hub">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--one-third column--reset">
                            <img src="<@hst.webfile path="images/icon-article.png" fullyQualified=true/>" alt="News article">
                        </div>
                        <div class="column--two-thirds column--reset">
                            <h1 class="local-header__title" data-uipath="document.title"><@fmt.message key="headers.page-title" /></h1>
                            <p class="article-header__subtitle"><@fmt.message key="texts.intro" /></p>  
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article">
        <#if pageable?? && pageable.items?has_content>
            <div class="grid-row">

                <div class="column column--two-thirds page-block page-block--main">

                    <div class="article-section article-section--letter-group">

                        <div class="grid-row">
                            <div class="column column--reset">
                                <div class="hub-box-list">
                                    <#list pageable.items as item>
                                        <#assign newsData = { "title": item.title, "text": item.shortsummary, "light": true } />

                                        <@hst.link hippobean=item var="newsLink" />
                                        <@fmt.formatDate value=item.publisheddatetime.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" var="date" />

                                        <#assign newsData += { "link": newsLink, "date": date } />

                                        <@hubBox newsData></@hubBox>
                                    </#list>
                                </div>
                            </div>
                        </div>
                    </div>


                    <#if pageable.totalPages gt 1>
                        <div class="article-section no-border">
                            <#include "../include/pagination.ftl">
                        </div>
                    </#if>
                </div>
            </div>
        <#else>
            <div class="grid-row">
                <div class="column column--two-thirds page-block page-block--main">
                    <p><@fmt.message key="texts.no-news" /></p>
                </div>
            </div>
        </#if>
    </div>
</article>
