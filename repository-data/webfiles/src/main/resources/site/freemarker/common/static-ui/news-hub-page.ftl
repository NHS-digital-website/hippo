<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#include "../macro/sectionNav.ftl">
<#include "../macro/tagNav.ftl">
<#include "../macro/hubBox.ftl">
<#include "../macro/stickyGroupBlockHeader.ftl">

<@hst.setBundle basename="rb.doctype.news-hub"/>

<#assign monthNames = monthsOfTheYear() />

<#--Group the news articles by earliest start month  -->
<#assign eventGroupHash = {} />
<#list pageable.items as item>
    <@fmt.formatDate value=item.publisheddatetime.time type="Date" pattern="MMMM" var="month" timeZone="${getTimeZone()}" />
    <p>month: ${month}, ${pageable.items?size}</p>
    <#-- <#if dateRangeData?size gt 0>
        <@fmt.formatDate value=dateRangeData.minStartTime type="Date" pattern="MMMM" timeZone="${getTimeZone()}" var="key" />
        <#assign eventGroupHash = eventGroupHash + {  key : (eventGroupHash[key]![]) + [ item ] } />
    </#if> -->
</#list>

<article class="article article--news-hub">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--one-third column--reset">
                            <img src="<@hst.webfile path="images/icon-news-paper.png" fullyQualified=true/>" alt="News article">
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
                    <#if eventGroupHash?has_content>
                        <#list monthsOfTheYear as month>
                            <#if eventGroupHash[month]??>
                                <div class="article-section article-section--letter-group">
                                    <@stickyGroupBlockHeader month></@stickyGroupBlockHeader>
                                </div>
                            </#if>
                        </#list>
                    </#if>

                    <#-- <div class="article-section article-section--letter-group">
                        <@stickyGroupBlockHeader "February"></@stickyGroupBlockHeader>

                        <div class="grid-row">
                            <div class="column column--reset">
                                <div class="hub-box-list">
                                    <@hubBox {"title": "Systems integration to boost NHS communication", "date": "21 February 2018", "text": "NHS staff will be able to communicate with each other through better integrated systems, thanks to new plans announced by NHS Digital.", "types": ["NHS Digital", "System & services"], "link": "#", "light": true } ></@hubBox>
                                </div>
                            </div>
                        </div>
                    </div> -->
                </div>
            </div>
        <#else>
            <div class="grid-row">
                <div class="column column--two-thirds page-block page-block--main">
                    <h2><@fmt.message key="texts.no-news" /></h2>
                </div>
            </div>
        </#if>
    </div>
</article>
