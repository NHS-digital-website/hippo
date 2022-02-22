<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/publicationsystem/structured-text.ftl">
<#include "./macro/fileMetaAppendix.ftl">
<#include "./macro/contentPixel.ftl">
<#include "./macro/heroes/hero-options.ftl">
<#include "./macro/heroes/hero.ftl">

<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<@contentPixel dataset.getCanonicalUUID() dataset.title></@contentPixel>

<#assign hasFiles = dataset.files?has_content />
<#assign hasResourceLinks = dataset.resourceLinks?has_content />
<#assign hasResources = hasFiles || hasResourceLinks />

<#assign earlyAccessKey=hstRequest.request.getParameter("key") />

<article aria-label="Document Content" itemscope itemtype="http://schema.org/Dataset">
    <#if dataset??>
    <meta itemprop="keywords" content="${dataset.fullTaxonomyList?join(",")}"/>
    <meta itemprop="url" content="<@hst.link hippobean=dataset.selfLinkBean/>"/>
    <meta itemprop="license" content="https://digital.nhs.uk/about-nhs-digital/terms-and-conditions" />

    <#assign options = getHeroOptions(document) />

    <#assign introText>
        <#if dataset.parentPublication??>
            <span><@fmt.message key="labels.dataset"/>,
                Part of <a itemprop="url" class="nhsd-a-link" href="<@hst.link hippobean=dataset.parentPublication.selfLinkBean><#if earlyAccessKey?has_content><@hst.param name="key" value="${earlyAccessKey}"/></#if></@hst.link>">
                    <span itemprop="name">${dataset.parentPublication.title}</span>
                </a>
            </span>
        <#else>
            <span><@fmt.message key="labels.dataset"/></span>
        </#if>
    </#assign>
    <#assign options += {
        "introText": introText,
        "summary": "",
        "colour": "darkBlue"
    }/>

    <@hero options>
        <dl class="nhsd-o-hero__meta-data nhsd-!t-margin-top-4">
            <div class="nhsd-o-hero__meta-data-item">
                <dt class="nhsd-o-hero__meta-data-item-title"><@fmt.message key="headers.date-range"/></dt>
                <dd class="nhsd-o-hero__meta-data-item-description" data-uipath="ps.dataset.date-range">
                    <#if dataset.coverageStart?? && dataset.coverageEnd??>
                        <@formatCoverageDates start=dataset.coverageStart.time end=dataset.coverageEnd.time/>
                        <meta itemprop="temporalCoverage" content="<@formatCoverageDates start=dataset.coverageStart.time end=dataset.coverageEnd.time schemaFormat=true/>" />
                    <#else>
                        (Not specified)
                    </#if>
                </dd>
            </div>

            <#if dataset.nominalDate?has_content>
                <div class="nhsd-o-hero__meta-data-item">
                    <dt class="nhsd-o-hero__meta-data-item-title"><@fmt.message key="headers.publication-date"/></dt>
                    <dd class="nhsd-o-hero__meta-data-item-description" data-uipath="ps.dataset.nominal-date" itemprop="datePublished">
                        <@formatRestrictableDate value=dataset.nominalDate/>
                    </dd>
                </div>

                <#if dataset.nextPublicationDate?has_content>
                    <div class="nhsd-o-hero__meta-data-item">
                        <dt class="nhsd-o-hero__meta-data-item-title"><@fmt.message key="headers.next-publication-date"/></dt>
                        <dd class="nhsd-o-hero__meta-data-item-description" data-uipath="ps.dataset.next-publication-date">
                            <@formatDate date=dataset.nextPublicationDate.time/>
                        </dd>
                    </div>
                </#if>
            </#if>

            <#if dataset.geographicCoverage?has_content>
                <div class="nhsd-o-hero__meta-data-item">
                    <dt class="nhsd-o-hero__meta-data-item-title" id="geographic-coverage" data-uipath="geographic-coverage"><@fmt.message key="headers.geographical-coverage"/></dt>
                    <dd class="nhsd-o-hero__meta-data-item-description" data-uipath="ps.dataset.geographic-coverage">
                        <#list dataset.geographicCoverage as geographicCoverageItem>
                            <span itemprop="spatialCoverage" itemscope itemtype="http://schema.org/Place"><span itemprop="name">${geographicCoverageItem}</span></span><#sep>,
                        </#list>
                    </dd>
                </div>
            </#if>

            <#if dataset.granularity?has_content>
                <div class="nhsd-o-hero__meta-data-item">
                    <dt class="nhsd-o-hero__meta-data-item-title"><@fmt.message key="headers.geographical-granularity"/></dt>
                    <dd class="nhsd-o-hero__meta-data-item-description" data-uipath="ps.dataset.granularity">
                        <#list dataset.granularity as granularityItem>${granularityItem}<#sep>, </#list>
                    </dd>
                </div>
            </#if>
        </dl>
    </@hero>

    <div class="nhsd-t-grid nhsd-!t-margin-top-8">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-8">
                <div class="nhsd-!t-margin-bottom-6">
                    <h2 class="nhsd-t-heading-xl"><@fmt.message key="headers.summary"/></h2>
                    <div class="rich-text-content" itemprop="description">
                        <@structuredText item=dataset.summary uipath="ps.dataset.summary" />
                    </div>
                </div>

                <#if hasResources>
                    <div class="nhsd-!t-margin-bottom-6">
                        <h2 class="nhsd-t-heading-xl"><@fmt.message key="headers.resources"/></h2>
                        <ul data-uipath="ps.dataset.resources" class="nhsd-t-list nhsd-t-list--bullet">
                            <#list dataset.files as attachment>
                                <li itemprop="distribution" itemscope itemtype="http://schema.org/DataDownload">
                                    <@externalstorageLink item=attachment.resource earlyAccessKey=earlyAccessKey; url>
                                      <a class="nhsd-a-link" itemprop="contentUrl" href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','Data set','${attachment.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)"><span itemprop="name">${attachment.text}</span></a>
                                      <meta itemprop="license" content="https://digital.nhs.uk/about-nhs-digital/terms-and-conditions" />
                                      <meta itemprop="encodingFormat" content="${attachment.resource.mimeType}" />
                                      <meta itemprop="name" content="${attachment.text}" />
                                    </@externalstorageLink>
                                    <div><@fileMetaAppendix attachment.resource.length, attachment.resource.mimeType /></div>
                                </li>
                            </#list>
                            <#list dataset.resourceLinks as link>
                                <li>
                                    <a class="nhsd-a-link" href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Download attachment','Data set','${link.linkUrl}');" onKeyUp="return vjsu.onKeyUp(event)">${link.linkText}</a>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>
            </div>
        </div>
    </div>

    <#else>
        <div class="nhsd-t-grid">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col nhsd-!t-margin-bottom-6">
                    <span>${error}</span>
                </div>
            </div>
        </div>
    </#if>
</article>
