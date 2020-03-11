<#ftl output_format="HTML">

<#-- @ftlvariable name="series" type="java.util.List<nhs.digital.ps.beans.Series>" -->
<#-- @ftlvariable name="publications" type="java.util.List<uk.nhs.digital.ps.beans.PublicationBase>" -->
<#-- @ftlvariable name="upcomingPublications" type="java.util.List<uk.nhs.digital.ps.beans.PublicationBase>" -->
<#include "../include/imports.ftl">
<#include "../common/macro/fileMetaAppendix.ftl">
<#include "../common/macro/updateGroup.ftl">
<#include "../common/macro/calloutBox.ftl">
<#include "./macro/structured-text.ftl">
<#include "./macro/seriesDocumentHeader.ftl">

<@hst.setBundle basename="publicationsystem.headers,publicationsystem.labels"/>


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
            <#if index?has_content && index?size gt 1>
                <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                    <!-- start sticky-nav -->
                    <div id="sticky-nav">
                        <#assign links = [] />
                        <#list index as i>
                            <#assign links += [{ "url": "#" + slugify(i), "title": i }] />
                        </#list>
                        <@stickyNavSections getStickySectionNavLinks({"document": publication, "sections": links})></@stickyNavSections>
                    </div>
                    <!-- end sticky-nav -->
                </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="grid-row">
                    <div class="column column--two-thirds page-block page-block--main">
                        <#-- [FTL-BEGIN] 'Latest statistics' section -->
                        <div class="article-section" id="section-latest-statistics">
                            <h2><@fmt.message key="headers.latest-statistics"/></h2>
                            
                            <@hst.link hippobean=series.latestPublication var="latestPublicationLink" />
                            <#assign latestPublicationData = {
                                "title": series.latestPublication.title, 
                                "link": latestPublicationLink,
                                "calloutType": "interactive",
                                "severity": "grey",
                                "accessible": true
                            } />
                            <@calloutBox latestPublicationData />

                            <p style="color: red;"><b>Notes:</b> Date (${series.latestPublication.nominalPublicationDate}) and summary isn't accessible to FE</p>
                        </div>
                        <#-- [FTL-END] 'Latest statistics' section -->

                        <#-- [FTL-BEGIN] mandatory 'Summary' section -->
                        <#-- <div class="article-section article-section--summary" id="section-summary">
                            <h2><@fmt.message key="headers.summary"/></h2>
                            <div class="rich-text-content" itemprop="description">
                                <@structuredText item=series.summary uipath="ps.series.summary" />
                            </div>
                        </div> -->
                        <#-- [FTL-END] mandatory 'Summary' section -->

                        <#-- [FTL-BEGIN] 'About this publication' section -->
                        <div class="article-section no-border" id="section-about">
                            <h2><@fmt.message key="headers.about-this-publication"/></h2>
                            <div class="rich-text-content" itemprop="description">
                                <@hst.html hippohtml=series.about contentRewriter=gaContentRewriter />
                            </div>

                            <h3><@fmt.message key="headers.responsible-parties"/></h3>
                            <div class="detail-list-grid detail-list-grid--regular">
                                <dl class="detail-list">
                                    <dt class="detail-list__key"><@fmt.message key="labels.responsible-statistician"/></dt>
                                    <dd class="detail-list__value"><a href="<@hst.link hippobean=series.statistician />">${series.statistician.title}</a>    </dd>
                                </dl>
                                <dl class="detail-list">
                                    <dt class="detail-list__key"><@fmt.message key="labels.responsible-team"/></dt>
                                    <dd class="detail-list__value"><a href="<@hst.link hippobean=series.team />">${series.team.title}</a></dd>
                                </dl>
                            </div>
                        </div>
                        <#-- [FTL-END] 'About this publication' section -->

                        <#-- [FTL-BEGIN] 'Methodology' section -->
                        <div class="article-section no-border" id="section-methodology">
                            <h2><@fmt.message key="headers.methodology"/></h2>
                            <div class="rich-text-content" itemprop="description">
                                <@hst.html hippohtml=series.methodology contentRewriter=gaContentRewriter />
                            </div>
                        </div>
                        <#-- [FTL-END] 'Methodology' section -->

                        <#-- [FTL-BEGIN] 'Pre-release access' section -->
                        <div class="article-section no-border" id="section-pre-release-access">
                            <h2><@fmt.message key="headers.pre-release-access"/></h2>

                            <ul>
                            <#list series.accessList.releaseSubjects as item>
                                <#if item.organisation>
                                    <h3>Found an organisation: ${item.organisation.title}</h3>
                                </#if>
                                <ul>
                                <#list item.recipients as recipient>
                                    <li>${recipient.jobRole.title}</li>
                                </#list>
                                </ul>
                                <li><@hst.html hippohtml=item.additionalDetail contentRewriter=gaContentRewriter /></li>
                            </#list>
                            </ul>
                        </div>
                        <#-- [FTL-END] 'Pre-release access' section -->

                        <#-- [FTL-BEGIN] 'Metadata' section -->
                        <div class="article-section no-border" id="section-metadata">
                            <h2><@fmt.message key="headers.metadata"/></h2>

                            <div class="detail-list-grid detail-list-grid--regular">
                                <dl class="detail-list">
                                    <dt class="detail-list__key"><@fmt.message key="labels.issn"/></dt>
                                    <dd class="detail-list__value">${series.issn}</dd>
                                </dl>
                                <dl class="detail-list">
                                    <dt class="detail-list__key"><@fmt.message key="labels.usrn"/></dt>
                                    <dd class="detail-list__value">${series.refNumber?c}</dd>
                                </dl>
                            </div>
                        </div>
                        <#-- [FTL-END] 'Metadata' section -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</article>
