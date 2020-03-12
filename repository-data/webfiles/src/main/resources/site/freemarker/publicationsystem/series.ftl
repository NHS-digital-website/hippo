<#ftl output_format="HTML">

<#-- @ftlvariable name="series" type="java.util.List<nhs.digital.ps.beans.Series>" -->
<#-- @ftlvariable name="publications" type="java.util.List<uk.nhs.digital.ps.beans.PublicationBase>" -->
<#-- @ftlvariable name="upcomingPublications" type="java.util.List<uk.nhs.digital.ps.beans.PublicationBase>" -->

<#include "../include/imports.ftl">
<#include "../common/macro/fileMetaAppendix.ftl">
<#include "../common/macro/updateGroup.ftl">
<#include "../common/macro/component/calloutBox.ftl">
<#include "../common/macro/stickyNavSections.ftl">
<#include "./macro/structured-text.ftl">
<#include "./macro/seriesDocumentHeader.ftl">

<@hst.setBundle basename="publicationsystem.headers, publicationsystem.labels" />

<#-- TODO - Use `document` instead of `series` -->
<#-- <#assign document = series /> -->

<#-- Cache section headers and section checkers -->
<@fmt.message key="headers.summary" var="summarySectionHeader" />
<@fmt.message key="headers.about-this-publication" var="aboutSectionHeader" />
<@fmt.message key="headers.methodology" var="methodologySectionHeader" />
<@fmt.message key="headers.metadata" var="metadataSectionHeader" />
<@fmt.message key="headers.latest-statistics" var="latestStatisticsSectionHeader" />
<@fmt.message key="headers.upcoming-publications" var="upcomingPublicationsSectionHeader" />
<@fmt.message key="headers.past-publications" var="pastPublicationsSectionHeader" />
<@fmt.message key="headers.resources" var="resourcesSectionHeader" />
<@fmt.message key="headers.pre-release-access" var="preReleaseAccessSectionHeader"/>

<#assign hasSummarySection = series.summary?? && series.summary?has_content />
<#assign hasLatestStatisticsSection = series.latestPublication?? && series.latestPublication?has_content />
<#assign hasAboutSection = series.about?? && series.about.content?has_content />
<#assign hasMethodologySection = series.methodology?? && series.methodology.content?has_content />
<#assign hasPastPublicationsSection = series.seriesReplaces?? />
<#assign hasUpcomingPublicationsSection = upcomingPublications?? && upcomingPublications?has_content />
<#assign hasResponsibleStatistician = series.statistician?? && series.statistician?has_content />
<#assign hasResponsibleTeam = series.team?? && series.team?has_content />
<#assign hasResourceLinks = series.resourceLinks?? && series.resourceLinks?has_content />
<#assign hasAttachments = series.attachments?? && series.attachments?has_content />
<#assign hasResourceSection = hasResourceLinks || hasAttachments />
<#assign hasPreReleaseAccessSection = series.releaseSubjects?? && series.releaseSubjects?has_content />
<#assign hasMetadataSection = (series.issn?? && series.issn?has_content) || (series.refNumber && series.refNumber?has_content) />


<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<@fmt.message key="headers.administrative-sources" var="administrativeResourcesHeader" />

<article class="article article--legacy-series" itemscope itemtype="http://schema.org/Series">
    <#-- [FTL-BEGIN] Big Blue Header -->
    <@seriesDocumentHeader document=series />
    <#-- [FTL-END] Big Blue Header -->

    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        
        <#-- [FTL-BEGIN] Update group -->
        <@updateGroup document=series />
        <#-- [FTL-END] Update group -->

        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <!-- start sticky-nav -->
                <div id="sticky-nav">
                    <#assign links = [] />
                    <#if hasSummarySection>
                        <#assign links += [{ "url": "#" + slugify(summarySectionHeader), "title": summarySectionHeader }] />
                    </#if>
                    <#if hasLatestStatisticsSection>
                        <#assign links += [{ "url": "#" + slugify(latestStatisticsSectionHeader), "title": latestStatisticsSectionHeader }] />
                    </#if>
                    <#if hasAboutSection || hasResponsibleStatistician || hasResponsibleTeam>
                        <#assign links += [{ "url": "#" + slugify(aboutSectionHeader), "title": aboutSectionHeader }] />
                    </#if>
                    <#if hasMethodologySection>
                        <#assign links += [{ "url": "#" + slugify(methodologySectionHeader), "title": methodologySectionHeader }] />
                    </#if>
                    <#if hasUpcomingPublicationsSection>
                        <#assign links += [{ "url": "#" + slugify(upcomingPublicationsSectionHeader), "title": upcomingPublicationsSectionHeader }] />
                    </#if>
                    <#if hasPastPublicationsSection>
                        <#assign links += [{ "url": "#" + slugify(pastPublicationsSectionHeader), "title": pastPublicationsSectionHeader }] />
                    </#if>
                    <#if hasResourceSection>
                        <#assign links += [{ "url": "#" + slugify(resourcesSectionHeader), "title": resourcesSectionHeader }] />
                    </#if>
                    <#if hasPreReleaseAccessSection>
                        <#assign links += [{ "url": "#" + slugify(preReleaseAccessSectionHeader), "title": preReleaseAccessSectionHeader }] />
                    </#if>
                    <#if hasMetadataSection>
                        <#assign links += [{ "url": "#" + slugify(metadataSectionHeader), "title": metadataSectionHeader }] />
                    </#if>

                    <@stickyNavSections getStickySectionNavLinks({"document": series, "sections": links})></@stickyNavSections>
                </div>
                <!-- end sticky-nav -->
            </div>
            <#-- Restore the bundle -->
            <@hst.setBundle basename="publicationsystem.headers, publicationsystem.labels" />

            <div class="column column--two-thirds page-block page-block--main">
                <div class="grid-row">
                    <#-- [FTL-BEGIN] mandatory 'Summary' section -->
                    <div class="article-section article-section--summary" id="${slugify(summarySectionHeader)}">
                        <h2>${summarySectionHeader}</h2>
                        <div class="rich-text-content" itemprop="description">
                            <@structuredText item=series.summary uipath="ps.series.summary" />
                        </div>
                    </div>
                    <#-- [FTL-END] mandatory 'Summary' section -->

                    <#-- [FTL-BEGIN] 'Latest statistics' section -->
                    <#if hasLatestStatisticsSection>
                    <div class="article-section" id="${slugify(latestStatisticsSectionHeader)}">
                        <h2>${latestStatisticsSectionHeader}</h2>

                        <@hst.link hippobean=series.latestPublication var="latestPublicationLink" />
                        <#assign publishDate = series.latestPublication.nominalPublicationDate.dayOfMonth + " " + 
                        series.latestPublication.nominalPublicationDate.month?capitalize + " " + series.latestPublication.nominalPublicationDate.year?c />

                        <#assign latestPublicationData = {
                            "title": series.latestPublication.title, 
                            "link": latestPublicationLink,
                            "calloutType": "interactive",
                            "date": publishDate,
                            "dateLabel": "Published: ",
                            "accessible": true,
                            "severity": "grey",
                            "narrow": true,
                            "index": "latest-publication"
                        } />
                        
                        <@calloutBox latestPublicationData />
                    </div>
                    </#if>
                    <#-- [FTL-END] 'Latest statistics' section -->

                    <#-- [FTL-BEGIN] 'About this publication' section -->
                    <#if hasAboutSection || hasResponsibleStatistician || hasResponsibleTeam>
                    <div class="article-section" id="${slugify(aboutSectionHeader)}">
                        <h2>${aboutSectionHeader}</h2>
                        <#if series.about.content?has_content>
                        <div class="rich-text-content" itemprop="description">
                            <@hst.html hippohtml=series.about contentRewriter=gaContentRewriter />
                        </div>
                        </#if>

                        <#if hasResponsibleStatistician || hasResponsibleTeam>
                        <h3><@fmt.message key="headers.responsible-parties"/></h3>
                        <div class="detail-list-grid detail-list-grid--regular">
                            <#if hasResponsibleStatistician>
                            <dl class="detail-list">
                                <dt class="detail-list__key"><@fmt.message key="labels.responsible-statistician"/></dt>
                                <dd class="detail-list__value"><a href="<@hst.link hippobean=series.statistician />">${series.statistician.title}</a>    </dd>
                            </dl>
                            </#if>
                            <#if hasResponsibleTeam>
                            <dl class="detail-list">
                                <dt class="detail-list__key"><@fmt.message key="labels.responsible-team"/></dt>
                                <dd class="detail-list__value"><a href="<@hst.link hippobean=series.team />">${series.team.title}</a></dd>
                            </dl>
                            </#if>
                        </div>
                        </#if>
                    </div>
                    </#if>
                    <#-- [FTL-END] 'About this publication' section -->

                    <#-- [FTL-BEGIN] 'Methodology' section -->
                    <#if hasMethodologySection>
                    <div class="article-section ${(hasResponsibleStatistician || hasResponsibleTeam)?then('no-border', '')}" id="${slugify(methodologySectionHeader)}">
                        <h2>${methodologySectionHeader}</h2>
                        <div class="rich-text-content" itemprop="description">
                            <@hst.html hippohtml=series.methodology contentRewriter=gaContentRewriter />
                        </div>
                    </div>
                    </#if>
                    <#-- [FTL-END] 'Methodology' section -->





                    <#-- [FTL-BEGIN] 'Upcoming publications' section -->
                    <#if hasUpcomingPublicationsSection>
                    <div class="article-section" id="${slugify(upcomingPublicationsSectionHeader)}">
                        <h2>${upcomingPublicationsSectionHeader}</h2>
                        <@upcomingPublicationList/>
                    </div>
                    </#if>
                    <#-- [FTL-END] 'Upcoming publications' section -->




                    <#-- [FTL-BEGIN] 'Past publications' section -->
                    <#if hasPastPublicationsSection>
                        <div class="article-section" id="${slugify(pastPublicationsSectionHeader)}">
                            <h2>${pastPublicationsSectionHeader}</h2>

                            <@fmt.formatDate var="changeDate" value=series.seriesReplaces.changeDate.time?date type="date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" />

                            <#assign updateData = {
                                "title": series.seriesReplaces.replacementSeries.title,
                                "content": series.seriesReplaces.whyReplaced,
                                "severity": "information",
                                "calloutType": "change",
                                "date": changeDate,
                                "narrow": true,
                                "index": "series-replaced"
                            } />
                        
                            <@calloutBox updateData />

                            <#if publications?size gt 1>
                                <h3 class="flush push--bottom"><@fmt.message key="headers.previous-versions"/></h3>
                                <ul class="list list--reset cta-list" data-uipath="ps.series.publications-list.previous">
                                    <#list publications[1..] as publication>

                                        <#assign publishDate = publication.nominalPublicationDate.dayOfMonth + " " + 
                                        publication.nominalPublicationDate.month?capitalize + " " + publication.nominalPublicationDate.year?c />

                                        <#assign publicationData = {
                                            "title": publication.title,
                                            "severity": "grey",
                                            "calloutType": "interactive",
                                            "accessible": true,
                                            "date": publishDate,
                                            "narrow": true,
                                            "index": publication?index
                                        } />
                            
                                        <@calloutBox publicationData />
                                    </#list>
                                </ul>
                            </#if>
                        </div>
                    </#if>
                    <#-- [FTL-END] 'Past publications' section -->







                    <#-- [FTL-BEGIN] 'Attachments and Resource links' section -->
                    <#if hasResourceSection>
                    <div class="article-section" id="${slugify(resourcesSectionHeader)}">
                        <h2>${resourcesSectionHeader}</h2>
                        <ul data-uipath="ps.series.resources">
                        <#list series.attachments as attachment>
                            <li class="attachment" itemprop="hasPart" itemscope itemtype="http://schema.org/MediaObject">
                                <@externalstorageLink attachment.resource; url>
                                <a itemprop="contentUrl" title="${attachment.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','Series','${attachment.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)"><span itemprop="name">${attachment.text}</span></a>
                                </@externalstorageLink>
                                <@fileMetaAppendix attachment.resource.length, attachment.resource.mimeType></@fileMetaAppendix>
                            </li>
                        </#list>
                        <#list series.resourceLinks as link>
                            <li>
                                <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Series','${link.linkUrl}');" onKeyUp="return vjsu.onKeyUp(event)" title="${link.linkText}">${link.linkText}</a>
                            </li>
                        </#list>
                        </ul>
                    </div>
                    </#if>
                    <#-- [FTL-END] 'Attachments and Resource links' section -->

                    <#-- [FTL-BEGIN] 'Pre-release access' section -->
                    <#if hasPreReleaseAccessSection>
                    <div class="article-section" id="${slugify(preReleaseAccessSectionHeader)}">
                        <h2>${preReleaseAccessSectionHeader}</h2>

                        <ul class="list--reset">
                        <#list series.releaseSubjects as item>
                            <li>
                                <#if item.organisation?has_content>
                                    <h3>${item.organisation.title}</h3>
                                </#if>

                                <#if item.additionalDetail.content?has_content>
                                <div class="rich-text-content">
                                    <@hst.html hippohtml=item.additionalDetail contentRewriter=gaContentRewriter />
                                </div>
                                </#if>

                                <#if item.recipients?has_content>
                                <ul>
                                <#list item.recipients as jobRole>
                                    <li>${jobRole.title}</li>
                                </#list>
                                </ul>
                            </li>
                            </#if>
                        </#list>
                        </ul>
                    </div>
                    </#if>
                    <#-- [FTL-END] 'Pre-release access' section -->

                    <#-- [FTL-BEGIN] 'Metadata' section -->
                    <#if hasMetadataSection>
                    <div class="article-section" id="${slugify(metadataSectionHeader)}">
                        <h2>${metadataSectionHeader}</h2>

                        <div class="detail-list-grid detail-list-grid--regular">
                            <#if series.issn?has_content>
                            <dl class="detail-list">
                                <dt class="detail-list__key"><@fmt.message key="labels.issn"/></dt>
                                <dd class="detail-list__value">${series.issn}</dd>
                            </dl>
                            </#if>
                            <#if series.refNumber?has_content>
                            <dl class="detail-list">
                                <dt class="detail-list__key"><@fmt.message key="labels.usrn"/></dt>
                                <dd class="detail-list__value">${series.refNumber?c}</dd>
                            </dl>
                            </#if>
                        </div>
                    </div>
                    </#if>
                    <#-- [FTL-END] 'Metadata' section -->
                </div>
            </div>
        </div>
    </div>
</article>

<#macro upcomingPublicationList>
<ul class="list list--reset cta-list" data-uipath="ps.series.publications-list.upcoming">
    <!--Only next 4 upcoming publications-->
    <#local count = (upcomingPublications?size < 4)?then(upcomingPublications?size, 4)/>
    <#list upcomingPublications[0..count-1] as publication>
    <li itemprop="hasPart" itemscope itemtype="http://schema.org/PublicationIssue">
        <article class="cta">
            <h3 itemprop="name"><a href="<@hst.link hippobean=publication.selfLinkBean/>" title="${publication.title}" class="cta__button" itemprop="url">${publication.title}</a></h3>
            <p class="cta__text"><@formatRestrictableDate value=publication.nominalPublicationDate/></p>
        </article>
    </li>
    </#list>
</ul>
</#macro>
