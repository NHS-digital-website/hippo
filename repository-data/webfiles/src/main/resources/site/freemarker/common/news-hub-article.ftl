<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "macro/sectionNav.ftl">
<#include "macro/hubBox.ftl">
<#include "macro/stickyGroupBlockHeader.ftl">
<#include "macro/documentHeader.ftl">

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

    <#assign header_title><@fmt.message key="headers.page-title" /></#assign>
    <#assign header_summary><@fmt.message key="texts.intro" /></#assign>
    <#assign header_icon = 'images/icon-article.png' />
    <#assign document = "simulating_doc" />

    <@documentHeader document 'news' header_icon header_title header_summary></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">
        <#if pageable?? && pageable.items?has_content>
            <div class="grid-row">

                <div class="column column--two-thirds page-block page-block--main">

                    <div class="article-section article-section--letter-group">

                        <div class="grid-row">
                            <div class="column column--reset">
                                <div class="hub-box-list">
                                    <#list pageable.items as item>
                                        <#assign newsData = { "title": item.title, "text": item.shortsummary} />

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

                <div class="column column--one-third">
                    <div class="article-section">
                        <div class="grid-row">
                            <div class="column column--reset">
                                <@hst.include ref="component-rightpane"/>
                            </div>
                        </div>
                    </div>
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
