<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<@hst.setBundle basename="publicationsystem.headers,publicationsystem.labels"/>

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#include "../common/macro/pubsBreadcrumb.ftl">
<@pubsBreadcrumb "Publications"></@pubsBreadcrumb>

<article class="article article--legacy-series">
    <#if series??>
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--detailed" aria-label="Series Title">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <span class="article-header__label"><@fmt.message key="labels.series"/></span>
                    <h1 class="local-header__title" data-uipath="document.title">${series.title}</h1>
                    <#-- <hr class="hr hr--short hr--light"> -->
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article" aria-label="Series Content">
        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <#-- [FTL-BEGIN] mandatory 'Summary' section -->
                <div class="article-section article-section--summary" id="section-summary">
                    <h2><@fmt.message key="headers.summary"/></h2>
                    <div class="rich-text-content">
                        <@structuredText item=series.summary uipath="ps.series.summary" />
                    </div>
                </div>
                <#-- [FTL-END] mandatory 'Summary' section -->

                <#if publications?has_content>
                <div class="article-section">
                    <#if series.showLatest>
                        <h3 class="flush push--bottom"><@fmt.message key="headers.latest-version"/></h3>
                        <ul class="list list--reset cta-list" data-uipath="ps.series.publications-list.latest">
                            <@publicationItem publication=publications?first/>
                        </ul>

                        <#if publications?size gt 1>
                            <h3 class="flush push--bottom"><@fmt.message key="headers.previous-versions"/></h3>
                            <ul class="list list--reset cta-list" data-uipath="ps.series.publications-list.previous">
                                <#list publications[1..] as publication>
                                    <@publicationItem publication=publication/>
                                </#list>
                            </ul>
                        </#if>
                    <#else>
                        <ul class="list list--reset cta-list" data-uipath="ps.series.publications-list">
                            <#list publications as publication>
                                <@publicationItem publication=publication/>
                            </#list>
                        </ul>
                    </#if>
                </div>
                </#if>
            </div>
        </div>
    </div>
    <#else>
      <span>${error}</span>
    </#if>
</article>

<#macro publicationItem publication>
<li>
    <article class="cta">
        <a href="<@hst.link hippobean=publication.selfLinkBean/>" title="${publication.title}" class="cta__button">${publication.title}</a>
        <#if publication.class.name == "uk.nhs.digital.ps.beans.Publication">
            <p class="cta__text"><@truncate text=publication.summary.firstParagraph size="300"/></p>
        </#if>
    </article>
</li>
</#macro>
