<#ftl output_format="HTML">

<#include "../include/imports.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/stickyNavTags.ftl">
<#include "macro/hubBox.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/latestblogs.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#--Resource Bundle-->
<@hst.setBundle basename="rb.doctype.news-events-homepage"/>
<@fmt.message key="headers.news" var="latestNews"/>
<@fmt.message key="label.all-news" var="viewAllNews"/>
<@fmt.message key="link.all-news-link" var="viewAllNewsLink"/>
<@fmt.message key="number.news-articles" var="NumberNewsArticles"/>
<@fmt.message key="headers.blogs" var="latestBlogs"/>
<@fmt.message key="label.all-blogs" var="viewAllBlogs"/>
<@fmt.message key="link.all-blogs-link" var="viewAllBlogsLink"/>
<@fmt.message key="number.blog-articles" var="NumberBlogArticles"/>
<@fmt.message key="headers.contact" var="contactUs"/>
<@fmt.message key="headers.follow" var="followUs"/>


<#--Modifiers-->
<#assign noBorder = {"noBorder": true} />
<#assign noPadding = {"noPadding": true} />
<#assign noBackgroundCol = {"noBackgroundCol": true} />
<#assign sameStyle = {"sameStyle": true} />


<#if document.socialmedia.othersocialmedias?has_content>
    <#list document.socialmedia.othersocialmedias as other>
        <#assign facebookSocial = (other.title?lower_case == 'facebook')?then(other,{}) />
    </#list>
</#if>

<#assign hasSocialMedia = document.socialmedia.linkedinlink?has_content || document.socialmedia.twitteruser?has_content || facebookSocial?has_content/>
<#assign hasSignUpInfo = document.signupsummary?has_content && document.ctabutton?has_content />

<article class="article article--news-hub">
    <@documentHeader document 'news-events-homepage' '' '' '' '' false></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-quarter page-block page-block--sidebar">
                <div id="sticky-nav">
                    <#assign links = [] />

                    <#if document.newsData?has_content>
                        <#assign links += [{ "url": "#" + slugify(latestNews), "title": latestNews }] />
                    </#if>
                    <#if document.blogData?has_content>
                        <#assign links += [{ "url": "#" + slugify(latestBlogs), "title": latestBlogs }] />
                    </#if>
                    <#if document.sections?has_content>
                        <#assign links += getNavLinksInMultiple(document.sections) />
                    </#if>
                    <#if document.contactDetails?has_content>
                        <#assign links += [{ "url": "#" + slugify(contactUs), "title": contactUs }] />
                    </#if>
                    <#if hasSocialMedia>
                        <#assign links += [{ "url": "#" + slugify(followUs), "title": followUs }] />
                    </#if>
                    <@stickyNavSections getStickySectionNavLinks({"sections": links})></@stickyNavSections>
                </div>
            </div>

            <div class="column column--three-quarters page-block page-block--main">
                <#if document.newsData?has_content>
                    <div class="article-section" id="${slugify(latestNews)}">
                        <h2>${latestNews}</h2>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <div class="hub-box-list bottom-margin-20" id="${slugify(latestNews)}-list">
                                    <#list document.newsData as news>
                                        <#-- Limit articles displayed -->
                                        <#if news?index <= NumberNewsArticles?number-1 >
                                            <#assign item = news />
                                            <#assign item = { "title": item.title, "text": item.shortsummary} />

                                            <@hst.link hippobean=news var="newsLink" />
                                            <@fmt.formatDate value=news.publisheddatetime.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" var="date" />

                                            <#assign item += { "link": newsLink, "date": date } />
                                            <#if news.leadimagesection?has_content>
                                                <#assign item += { "imagesection": news.leadimagesection} />
                                            </#if>

                                            <#--<#assign item += noBorder + noBackgroundCol + noPadding />-->
                                            <@hubBox item ></@hubBox>
                                        </#if>
                                    </#list>
                                </div>
                            </div>
                        </div>

                        <#if viewAllNews?has_content>
                            <#assign onClickMethodCallNews = getOnClickMethodCall(document.class.name, viewAllNewsLink) />
                            <div class="grid-row">
                                <div class="column column--reset">
                                    <div class="ctabtn--div">
                                        <div class="ctabtn-left" aria-labelledby="ctabtn-${slugify(viewAllNews)}">
                                            <a href="${viewAllNewsLink}"
                                               class="ctabtn-left ctabtn--nhs-digital-button"
                                               id="ctabtn-${slugify(viewAllNews)}"
                                               onClick="${onClickMethodCallNews}"
                                               onKeyUp="return vjsu.onKeyUp(event)">${viewAllNews}</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </div>
                </#if>

                <#if document.ctabutton?has_content || document.signupsummary?has_content>
                    <div class="article-section" id="sign-up">
                        <div class="grid-row">
                            <div class="column column--reset">
                                <#if document.signupsummary?has_content>
                                    <p>${document.signupsummary}</p>
                                </#if>
                                <#if document.ctabutton?has_content>
                                    <div class="ctabtn--div">
                                        <div class="ctabtn-${slugify(document.ctabutton.alignment)}" aria-labelledby="ctabtn-${slugify(document.ctabutton.title)}">
                                            <#assign ctaItem = document.ctabutton.items[0] />
                                            <#if ctaItem.linkType == "internal">
                                                <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, ctaItem.link.title) />
                                                <@hst.link hippobean=ctaItem.link var="ctaLink" />
                                            <#elseif ctaItem.linkType == "external">
                                                <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, ctaItem.link) />
                                                <#assign ctaLink = ctaItem.link />
                                            </#if>

                                            <a href="${ctaLink}"
                                               class="ctabtn-${slugify(document.ctabutton.alignment)} ctabtn--${slugify(document.ctabutton.buttonType)}"
                                               id="ctabtn-${slugify(document.ctabutton.title)}"
                                               onClick="${onClickMethodCall}"
                                               onKeyUp="return vjsu.onKeyUp(event)">
                                                ${document.ctabutton.title}
                                            </a>
                                        </div>
                                    </div>
                                </#if>
                            </div>
                        </div>
                    </div>
                </#if>

                <#if document.blogData?has_content>
                    <div class="article-section" id="${slugify(latestBlogs)}">
                        <div class="grid-row">
                            <#-- Limit blogs displayed -->
                            <#assign blogsList = [] />
                            <#list document.blogData as blog>
                                <#if (NumberBlogArticles?has_content) && blog?index <= NumberBlogArticles?number-1 >
                                    <#assign blogsList += [blog] />
                                </#if>
                            </#list>
                            <@latestblogs blogsList "" "latest-blogs" latestBlogs></@latestblogs>
                        </div>

                        <#if viewAllBlogs?has_content>
                            <#assign onClickMethodCallBlogs = getOnClickMethodCall(document.class.name, viewAllNewsLink) />
                            <div class="grid-row">
                                <div class="column column--reset">
                                    <div class="ctabtn--div">
                                        <div class="ctabtn-left" aria-labelledby="ctabtn-${slugify(viewAllBlogs)}">
                                            <a href="${viewAllBlogsLink}"
                                               class="ctabtn-left ctabtn--nhs-digital-button"
                                               id="ctabtn-${slugify(viewAllBlogs)}"
                                               onClick="${onClickMethodCallBlogs}"
                                               onKeyUp="return vjsu.onKeyUp(event)">${viewAllBlogs}</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </div>
                </#if>

                <#if document.sections?has_content>
                    <div class="article-section">
                        <@sections document.sections></@sections>
                    </div>
                </#if>

                <#if document.contactDetails?has_content>
                    <div class="article-section" id="${slugify(contactUs)}">
                        <h2>${contactUs}</h2>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <@hst.html hippohtml=document.contactDetails contentRewriter=gaContentRewriter/>
                            </div>
                        </div>
                    </div>
                </#if>

                <#if hasSocialMedia>
                    <div class="article-section" id="${slugify(followUs)}">
                        <h2>${followUs}</h2>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <div class="blog-social">
                                    <#if facebookSocial?has_content >
                                        <div class="blog-social-icon like-first-child">
                                            <a target="_blank" href="${facebookSocial.link}">
                                                <img src="<@hst.webfile path="/images/icon/Facebook.svg"/>" alt="Follow on Facebook" class="blog-social-icon__img" />
                                            </a>
                                        </div>
                                    </#if>

                                    <#if document.socialmedia.twitteruser?has_content>
                                        <div class="blog-social-icon like-first-child">
                                            <a target="_blank" href="https://twitter.com/${document.socialmedia.twitteruser}">
                                                <img src="<@hst.webfile path="/images/icon/Twitter.svg"/>" alt="Follow on Twitter" class="blog-social-icon__img" />
                                            </a>
                                        </div>
                                    </#if>

                                    <#if document.socialmedia.linkedinlink?has_content>
                                        <div class="blog-social-icon like-first-child">
                                            <a target="_blank" href="${document.socialmedia.linkedinlink}">
                                                <img src="<@hst.webfile path="/images/icon/LinkedIn.svg"/>" alt="Follow on LinkedIn" class="blog-social-icon__img" />
                                            </a>
                                        </div>
                                    </#if>
                                </div>
                            </div>
                        </div>
                    </div>
                </#if>
            </div>
        </div>
    </div>
</article>
