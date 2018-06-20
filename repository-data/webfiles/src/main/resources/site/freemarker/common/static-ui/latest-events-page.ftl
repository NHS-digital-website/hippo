<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#include "../macro/sectionNav.ftl">
<#include "../macro/hubBox.ftl">
<#include "../macro/stickyGroupBlockHeader.ftl">

<article class="article article--latest-events">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--one-third column--reset">
                            <img src="<@hst.webfile path="images/icon-calendar.png" fullyQualified=true/>" alt="Calendar">
                        </div>
                        <div class="column--two-thirds column--reset">
                            <h1 class="local-header__title" data-uipath="document.title">Events</h1>
                            <p class="article-header__subtitle">Welcome to our events area. Find out where you are likely to see us over the coming months and read about all our events.</p>  
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
                    <#assign links = [{ "url": "#january", "title": "January" }, { "url": "#october", "title": "October" }, { "url": "#past-events", "title": "Past events" } ] />
                    <@sectionNav links></@sectionNav>
                </div>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section article-section--letter-group no-border">
                    <@stickyGroupBlockHeader "January"></@stickyGroupBlockHeader>

                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="cta-list hub-box-list">
                                <@hst.webfile path="images/test-image.jpg" var="backgroundImage" />
                                <@hubBox {"title": "HSCN Summit 2018", "date": "18 January 2018", "text": "We're at the HSCN Summit 2018 on 18 January 2018 at the QEII Centre in London.", "tags": ["Conference", "Recruitment"], "background": backgroundImage, "url": "#" } ></@hubBox>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="article-section article-section--letter-group no-border">
                    <@stickyGroupBlockHeader "October"></@stickyGroupBlockHeader>

                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="cta-list hub-box-list">
                                <@hubBox {"title": "Lorem ipsum", "date": "18 October 2018", "text": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "tags": ["Conference"], "url": "#" } ></@hubBox>

                                <@hubBox {"title": "Lorem ipsum", "date": "18 October 2018", "text": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "tags": ["Conference"], "url": "#" } ></@hubBox>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="article-section article-section--letter-group no-border">
                    <@stickyGroupBlockHeader "Past events"></@stickyGroupBlockHeader>

                    <div class="grid-row">
                        <div class="column column--reset">
                            <p>View our <a href="#">events from previous years</a>.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</article>
