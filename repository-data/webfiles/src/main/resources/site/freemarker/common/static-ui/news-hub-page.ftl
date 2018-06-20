<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#include "../macro/sectionNav.ftl">
<#include "../macro/tagNav.ftl">
<#include "../macro/hubBox.ftl">
<#include "../macro/stickyGroupBlockHeader.ftl">

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
                            <h1 class="local-header__title" data-uipath="document.title">News</h1>
                            <p class="article-header__subtitle">The latest news and events from NHS Digital.</p>  
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <#assign filterNavLinks = [{ "url": "#", "title": "NHS Digital (4)" }, { "url": "#", "title": "Programme (16)" }, { "url": "#", "title": "NHS Digital (32)" }, { "url": "#", "title": "System and services (22)" }, { "url": "#", "title": "2018 (25)" }, { "url": "#", "title": "2017 (150)" }, { "url": "#", "title": "2016 (123)" }, { "url": "#", "title": "2015 (85)" }]/>
                    <@tagNav filterNavLinks></@tagNav>
                </div>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section article-section--letter-group">
                    <@stickyGroupBlockHeader "February"></@stickyGroupBlockHeader>

                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="hub-box-list">
                                <@hubBox {"title": "Systems integration to boost NHS communication", "date": "21 February 2018", "text": "NHS staff will be able to communicate with each other through better integrated systems, thanks to new plans announced by NHS Digital.", "types": ["NHS Digital", "System & services"], "link": "#", "light": true } ></@hubBox>

                                <@hubBox {"title": "NHS Digital apps and wearables team hosts Swedish delegation", "date": "16 February 2018", "text": "Representatives from Sweden's key health organisations visited NHS Digital this week to share knowledge and experience of the apps and digital healthcare landscape.", "types": ["NHS Digital"], "link": "#", "light": true } ></@hubBox>

                                <@hubBox {"title": "Adult social care workforce statistics released by NHS Digital today", "date": "7 February 2018", "text": "NHS Digital has today published the latest information on the number of adult social care staff who work for local authorities in England.", "link": "#", "light": true } ></@hubBox>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="article-section article-section--letter-group">
                    <@stickyGroupBlockHeader "January"></@stickyGroupBlockHeader>

                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="hub-box-list">
                                <@hubBox {"title": "Uptake for routine breast screening falls", "date": "31 January 2018", "text": "The proportion of women aged 50-70 taking up routine breast screening invitations fell to 71.1 per cent in 2016-17 down from 72.1 per cent in 2015-16 and 73.6 percent in 2006-07. It is at the lowest rate over the 10 year period.", "link": "#", "light": true } ></@hubBox>

                                <@hubBox {"title": "Information sharing presents major barrier to social workers", "date": "21 January 2018", "text": "98 per cent of social workers cited at least one difficulty with sharing information digitally, in research commissioned by NHS Digital.", "link": "#", "light": true } ></@hubBox>

                                <@hubBox {"title": "A Statement from NHS Digital on the misrepresentation of our vacancy statistics in some media reports", "date": "24 January 2018", "text": "A number of media reports on our vacancy statistics data published on 23 January have unintentionally misrepresented our data. It is not possible to use our data to draw any conclusions on the number of appointments as a proportion of advertisements raised. This is because our information is based on the secondary use of administrative data related to job advertisements within NHS Jobs. The appointments data is often not completed, as individual organisations will have alternative means of managing appointments information locally.", "link": "#", "light": true } ></@hubBox>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</article>
