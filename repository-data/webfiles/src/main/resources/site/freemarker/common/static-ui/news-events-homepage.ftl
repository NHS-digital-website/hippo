<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#include "../macro/stickyNavSections.ftl">
<#include "../macro/stickyNavTags.ftl">
<#include "../macro/hubBox.ftl">
<#include "../macro/stickyGroupBlockHeader.ftl">

<#assign latestNews="Latest news" />
<#assign latestBlogs="Latest blog" />
<#assign features="Features" />
<#assign resources="Resources" />
<#assign contactUs="Contact us" />
<#assign followSocial="Follow us on social media" />

<#assign noBorder = {"noBorder": true} />
<#assign noPadding = {"noPadding": true} />
<#assign noBackgroundCol = {"noBackgroundCol": true} />
<#assign sameStyle = {"sameStyle": true} />

<article class="article article--news-hub" aria-label="Document Header">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--two-thirds column--reset">
                            <h1 class="local-header__title" data-uipath="document.title">News and Events</h1>
                            <p class="article-header__subtitle" data-uipath="website.news-events-homepage.summary">Find all the latest news and events related content.</p>
                        </div>
                        <div class="column--one-third column--reset">
                            <img src="<@hst.webfile path="images/penpaper.svg" fullyQualified=true/>" alt="News and Events">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <div class="column column--one-quarter page-block page-block--sidebar">
                <div id="sticky-nav">
                    <#assign index = [latestNews, latestBlogs, features, resources, contactUs, followSocial] />

                    <#assign links = [] />

                    <#list index as i>
                        <#assign links += [{ "url": "#" + slugify(i), "title": i }] />
                    </#list>

                    <@stickyNavSections getStickySectionNavLinks({"sections": links})></@stickyNavSections>
                </div>
            </div>

            <div class="column column--three-quarters page-block page-block--main">
                <div class="article-section article-section--letter-group" id="${slugify(latestNews)}">
                    <h2>${latestNews}</h2>
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="hub-box-list bottom-margin-20" id="${slugify(latestNews)}-list">
                                <#assign newsData = [{}, {}] />
                                <#list newsData as news>
                                    <#assign data = {"title": "HSCN Summit 2018", "date": "18 January 2018", "text": "Conveniently orchestrate user friendly models without revolutionary.", "imagesection": "EMPTY", "link": "#" } />
                                    <#assign data += noBorder + noBackgroundCol + noPadding />
                                    <@hubBox data ></@hubBox>
                                </#list>
                            </div>
                        </div>
                    </div>
                    <div class="grid-row bottom-margin-20">
                        <a href="#">View all news</a>
                    </div>
                </div>

                <div class="article-section article-section--letter-group" id="${slugify(latestBlogs)}">
                    <h2>${latestBlogs}</h2>
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="hub-box-list bottom-margin-20" id="${slugify(latestBlogs)}-list">
                                <#assign blogData = [{}] />
                                <#list blogData as blog>
                                    <#assign data = {"title": "HSCN Summit 2018", "date": "18 January 2018", "text": "Conveniently orchestrate user friendly models without revolutionary.", "imagesection": "EMPTY", "link": "#" } />
                                    <#assign data += noBorder + noBackgroundCol + noPadding + sameStyle/>
                                    <@hubBox data ></@hubBox>
                                </#list>
                            </div>
                        </div>
                    </div>
                    <div class="grid-row bottom-margin-20">
                        <a href="#">View all blogs</a>
                    </div>
                </div>

                <div class="article-section article-section--letter-group" id="${slugify(features)}">
                    <h2>${features}</h2>
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="hub-box-list bottom-margin-20" id="${slugify(features)}-list">
                                <#assign featureData = [{}, {}] />
                                <#list featureData as feature>
                                    <#assign data = {"title": "HSCN Summit 2018", "date": "18 January 2018", "text": "Conveniently orchestrate user friendly models without revolutionary.", "imagesection": "EMPTY", "link": "#" } />
                                    <#assign data += noBorder + noBackgroundCol + noPadding + sameStyle/>
                                    <@hubBox data ></@hubBox>
                                </#list>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="article-section article-section--letter-group" id="${slugify(resources)}">
                    <h2>${resources}</h2>
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="hub-box-list bottom-margin-20" id="${slugify(resources)}-list">
                                <#assign resources = [
                                {"title": "Upcoming events", "text": "Conveniently orchestrate user friendly models without revolutionary.", "link": "#" },
                                {"title": "Journalist and media resources", "text": "Conveniently orchestrate user friendly models without revolutionary.", "link": "#" },
                                {"title": "Supplementary information", "text": "Conveniently orchestrate user friendly models without revolutionary.", "link": "#" },
                                {"title": "NHS Digital style guidelines", "text": "Conveniently orchestrate user friendly models without revolutionary.", "link": "#" }
                                ] />
                                <ul class="list list--reset">
                                    <#list resources as resource>
                                        <li>
                                            <a href="#">${resource.title}</a>
                                            <p>${resource.text}</p>
                                        </li>
                                    </#list>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="article-section article-section--letter-group" id="${slugify(contactUs)}">
                    <h2>${contactUs}</h2>
                    <div class="grid-row">
                        <div class="column column--reset bottom-margin-20">
                            <p>To report an urgent cyber security issue, call 0300 303 5222 or email: <a href="#">carecert@nhsdigital.nhs.uk</a></p>
                            <p>For general queries, to sign-up to any of our services or to give us feedback, telephone: 0300 303 5222 or email: <a href="#">carecert@nhsdigital.nhs.uk</a></p>
                        </div>
                    </div>
                </div>

                <div class="article-section article-section--letter-group" id="${slugify(followSocial)}">
                    <h2>${followSocial}</h2>
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="blog-social">
                                <div class="blog-social-icon like-first-child">
                                    <a target="_blank" href="https://www.facebook.com/NHSDigitalOfficial/">
                                        <img src="<@hst.webfile path="/images/icon/Facebook.svg"/>" alt="Follow on Facebook" class="blog-social-icon__img" />
                                    </a>
                                </div>

                                <div class="blog-social-icon like-first-child">
                                    <a target="_blank" href="https://twitter.com/NHSDigital">
                                        <img src="<@hst.webfile path="/images/icon/Twitter.svg"/>" alt="Follow on Twitter" class="blog-social-icon__img" />
                                    </a>
                                </div>

                                <div class="blog-social-icon like-first-child">
                                    <a target="_blank" href="https://www.linkedin.com/company/nhs-digital">
                                        <img src="<@hst.webfile path="/images/icon/LinkedIn.svg"/>" alt="Follow on LinkedIn" class="blog-social-icon__img" />
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</article>
