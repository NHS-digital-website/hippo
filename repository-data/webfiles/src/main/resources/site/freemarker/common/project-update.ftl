<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/headerMetadata.ftl">
<#include "../common/macro/stickyNavSections.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "../publicationsystem/macro/structured-text.ftl">
<#include "../common/macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#macro restrictedContentOfUpcomingProjectUpdates>
<#--    <@publicationHeader publication=publication restricted=true downloadPDF=false />-->

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <p data-uipath="ps.project-update.upcoming-disclaimer"
                       class="strong" itemprop="description">(Upcoming, not yet
                        published)</p>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#--Resource Bundle-->
<@hst.setBundle basename="rb.doctype.projectupdate"/>
<@fmt.message key="headers.summary" var="summaryHeader" />
<@fmt.message key="headers.relatedInfo" var="relatedInfoHeader" />
<@fmt.message key="headers.sharePage" var="sharePageHeader" />

<#--hasContent-->
<#assign hasSummary = document.shortSummaryHtml?? && document.shortSummaryHtml?has_content/>
<#assign hasRelatedInfo = document.serviceAffected?? && document.serviceAffected?has_content/>
<#assign hasSections = document.sections?? && document.sections?has_content/>
<#assign hasKeys = document.keys?? && document.keys?has_content/>
<#assign hasTwitterHashtag = document.twitterHashtag?? && document.twitterHashtag?has_content />
<#assign hasFullAccess = document.publiclyAccessible />


<#--Build metaData-->
<#assign metaData = [] />
<#if document.typeOfUpdate?has_content && hasFullAccess>
    <@fmt.message key="texts.${document.typeOfUpdate}" var="updateType"/>
    <#assign metaData += [{"key":"Type of update", "value": updateType, "uipath": "type", "schemaOrgTag": "", "type":""}] />
</#if>

<#if document.organisation?has_content && document.organisation.title?has_content && hasFullAccess>
    <#assign metaData += [{"key":"Organisation issuing this update", "value": document.organisation.title, "uipath": "organisation", "schemaOrgTag": "sourceorganization", "type": ""}] />
</#if>

<#if document.updateTimestamp?has_content>
    <#assign metaData += [{"key":"Update issued", "value": document.updateTimestamp.time, "uipath": "updateTimestamp", "schemaOrgTag": "datepublished", "type": "date"}] />
</#if>

<#if document.expiryDate?has_content && hasFullAccess>
    <#assign metaData += [{"key":"Update expires", "value": document.expiryDate.time, "uipath": "expiryDate", "schemaOrgTag": "expires", "type": "date"}] />
</#if>


<#macro fullContentOfPubliclyAvailableProjectUpdates>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">

            <div class="column column--one-third page-block--sticky-nav page-block--sidebar article-section-nav-outer-wrapper">
                <!-- start sticky-nav -->
                <div id="sticky-nav">
                    <#assign links = [] />

                    <#if hasSummary>
                        <#assign links += [{ "url": "#" + slugify(summaryHeader), "title": summaryHeader }] />
                    </#if>

                    <#if hasSections>
                        <#assign links += getNavLinksInMultiple(document.sections) />
                    </#if>

                    <#if hasRelatedInfo>
                        <#assign links += [{ "url": "#" + slugify(relatedInfoHeader), "title": relatedInfoHeader }] />
                    </#if>

                    <#if sharePageHeader?has_content>
                        <#assign links += [{ "url": "#" + slugify(sharePageHeader), "title": sharePageHeader }] />
                    </#if>

                    <@stickyNavSections getStickySectionNavLinks({"sections": links})></@stickyNavSections>
                </div>
                <!-- end sticky-nav -->
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <#if hasKeys>
                    <#--schema:keyword-->
                    <meta itemprop="keywords" content="${document.keys?join(",")}"/>
                </#if>

                <#if hasSummary>
                    <div id="${slugify(summaryHeader)}" class="article-section article-section--summary no-border">
                        <h2>${summaryHeader}</h2>
                        <@hst.html hippohtml=document.shortSummaryHtml contentRewriter=gaContentRewriter/>
                    </div>
                </#if>

                <#if hasSections>
                    <#--schema:text-->
                    <div class="article-section" itemprop="text">
                        <@sections document.sections></@sections>
                    </div>
                </#if>

                <#if hasRelatedInfo>
                    <div id="${slugify(relatedInfoHeader)}" class="article-section">
                        <h2>${relatedInfoHeader}</h2>

                        <div class="article-section--list">
                            <div class="grid-row">
                                <div class="column column--reset">
                                    <ul class="list list--reset cta-list cta-list--sections">
                                        <#list document.serviceAffected as link>

                                            <#-- apply (schema:about) prop here if related info url exists and first item -->
                                            <#assign firstUrlItemProp = (link?index==0 && document.typeOfUpdate?has_content)?then("itemprop=about", "") />
                                            <li>
                                                <#if link.linkType??>
                                                    <@typeSpan link.linkType />

                                                    <#if link.linkType == "internal">
                                                        <#--schema:about-->
                                                        <@downloadBlock link.link firstUrlItemProp/>

                                                    <#elseif link.linkType == "external">
                                                        <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, link.title) />

                                                        <#--schema:about-->
                                                        <a href="${link.link}"
                                                           class="block-link"
                                                           onClick="${onClickMethodCall}"
                                                           onKeyUp="return vjsu.onKeyUp(event)" ${firstUrlItemProp}>
                                                            <div class="block-link__header">
                                                                <@fileIconByFileType link.link />
                                                            </div>
                                                            <div class="block-link__body">
                                                                <span class="block-link__title">${link.title}</span>
                                                                <p class="cta__text">${link.shortsummary}</p>
                                                            </div>
                                                        </a>
                                                    </#if>
                                                </#if>
                                            </li>
                                        </#list>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </#if>

                <div id="${slugify(sharePageHeader)}" class="article-section">

                    <h2>${sharePageHeader}</h2>
                    <@hst.link var="currentUrl" hippobean=document fullyQualified=true/>

                    <div class="blog-social">
                        <#-- Use UTF-8 charset for URL escaping from now: -->
                        <#setting url_escaping_charset="UTF-8">

                        <div class="blog-social-icon like-first-child">
                            <a target="_blank" href="http://www.facebook.com/sharer.php?u=${currentUrl?url}">
                                <img src="<@hst.webfile path="/images/icon/Facebook.svg"/>" alt="Share on Facebook" class="blog-social-icon__img" />
                            </a>
                        </div>

                        <div class="blog-social-icon like-first-child">
                            <#assign hashtags ='' />
                            <#if hasTwitterHashtag>
                                <#list document.twitterHashtag as tag>
                                    <#if tag?starts_with("#")>
                                        <#assign hashtags = hashtags + tag?keep_after('#') + ','>
                                    <#else>
                                        <#assign hashtags = hashtags + tag + ','>
                                    </#if>
                                </#list>
                            </#if>
                            <a target="_blank" href="https://twitter.com/intent/tweet?via=nhsdigital&url=${currentUrl?url}&text=${document.title?url}&hashtags=${hashtags?url}">
                                <img src="<@hst.webfile path="/images/icon/Twitter.svg"/>" alt="Share on Twitter" class="blog-social-icon__img" />
                            </a>
                        </div>

                        <div class="blog-social-icon like-first-child">
                            <@hst.html hippohtml=document.shortSummaryHtml contentRewriter=gaContentRewriter var="shortSummaryHtml"/>
                            <!-- strip HTML tags -->
                            <#assign plainShortSummary = shortSummaryHtml?replace('<[^>]+>','','r') />
                            <a target="_blank" href="http://www.linkedin.com/shareArticle?mini=true&url=${currentUrl?url}&title=${document.title?url}&summary=${plainShortSummary?url}">
                                <img src="<@hst.webfile path="/images/icon/LinkedIn.svg"/>" alt="Share on LinkedIn" class="blog-social-icon__img" />
                            </a>
                        </div>
                    </div>

                </div>

            </div>
        </div>
    </div>
</#macro>


<#if document??>
    <article class="article" itemscope itemtype="http://schema.org/SpecialAnnouncement">
        <#--schema:description-->
        <#if document.seosummary?has_content>
            <#assign description = document.seosummary.content?replace('<[^>]+>','','r') />
            <span itemprop="description" class="is-hidden">${description}</span>
        </#if>

        <#--schema:{typeOfUpdate}-->
        <#if document.typeOfUpdate?has_content>
            <@hst.link var="currentUrl" hippobean=document fullyQualified=true/>
            <span itemprop="${typeOfUpdateTag(updateType)}" class="is-hidden">${currentUrl}</span>
        </#if>

        <#--schema:datePosted-->
        <#if document.updateTimestamp?has_content>
            <@fmt.formatDate value=document.updateTimestamp.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" var="datePosted"/>
            <span itemprop="datePosted" class="is-hidden">${datePosted}</span>
        </#if>

        <#--schema:spatialCoverage-->
        <span itemprop="spatialCoverage" class="is-hidden">England</span>

        <#--schema:category-->
        <#if document.wikiLink?has_content>
            <span itemprop="category" class="is-hidden">${document.wikiLink}</span>
        </#if>

        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
            <div class="local-header article-header article-header--with-icon">
                <div class="grid-wrapper">
                    <div class="article-header__inner">
                        <div class="grid-row">
                            <div class="column--two-thirds column--reset">
                                <#--schema:name-->
                                <h1 id="top" class="local-header__title" data-uipath="document.title" itemprop="name">${document.title}</h1>
                            </div>
                        </div>
                    </div>

                    <#if metaData?has_content && metaData?size gt 0>
                        <@headerMetadata metaData />
                    </#if>
                </div>
            </div>
        </div>
        <#if hasFullAccess>
            <@fullContentOfPubliclyAvailableProjectUpdates/>
        <#else>
            <@restrictedContentOfUpcomingProjectUpdates/>
        </#if>
    </article>
</#if>
