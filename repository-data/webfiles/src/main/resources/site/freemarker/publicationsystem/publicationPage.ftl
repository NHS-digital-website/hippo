<#ftl output_format="HTML">

<#-- @ftlvariable name="page" type="uk.nhs.digital.ps.beans.PublicationPage" -->

<#include "./macro/publicationHeader.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "../include/imports.ftl">
<#include "../common/macro/stickyNavSections.ftl">
<#include "../common/macro/component/lastModified.ftl">
<#include "../common/macro/component/pagination.ftl">

<#assign overridePageTitle>${page.title}</#assign>
<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>

<article class="article article--chaptered-publication" itemscope itemtype="http://schema.org/WebPage">
    <#if page.publication??>
        <div itemprop="isPartOf" itemscope itemtype="http://schema.org/PublicationIssue">
            <@publicationHeader publication=page.publication />
        </div>
    </#if>

    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <#assign links=getStickySectionNavLinks({ "document" : page })/>
            <#if links?size != 0>
                <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                    <!-- start sticky-nav -->
                    <div id="sticky-nav">
                        <@stickyNavSections links></@stickyNavSections>
                    </div>
                    <!-- end sticky-nav -->
                </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <h1 data-uipath="ps.publication.page-title" title="${page.title}" itemprop="name">${page.title}</h1>

                <#if page.sections?has_content>
                    <div data-uipath="ps.publication.body">
                        <div class="article-section"></div>
                        <@sections sections=pageSections wrap=true/>
                    </div>
                </#if>

                <@lastModified page.lastModified></@lastModified>

                <div class="article-section no-border no-top-margin">
                    <@pagination page/>
                </div>
            </div>
        </div>
    </div>
</article>
