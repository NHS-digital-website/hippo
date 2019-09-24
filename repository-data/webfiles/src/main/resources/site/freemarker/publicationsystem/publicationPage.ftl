<#ftl output_format="HTML">

<#-- @ftlvariable name="page" type="uk.nhs.digital.ps.beans.PublicationPage" -->

<#include "./macro/publicationHeader.ftl">
<#include "./publication.ftl">
<#include "../include/imports.ftl">
<#include "../common/macro/sectionNavTemp.ftl">
<#include "../common/macro/component/lastModified.ftl">
<#include "../common/macro/component/pagination.ftl">

<article class="article article--chaptered-publication" itemscope itemtype="http://schema.org/WebPage">
    <#if page.publication??>
        <div itemprop="isPartOf" itemscope itemtype="http://schema.org/PublicationIssue">
            <@publicationHeader publication=page.publication />
        </div>
    </#if>

    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">

            <#if pageIndex?has_content>
                <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                    <!-- start sticky-nav -->
                    <div id="sticky-nav">
                        <@sectionNav pageIndex></@sectionNav>
                    </div>
                    <!-- end sticky-nav -->
                </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <h1 data-uipath="ps.publication.page-title" title="${page.title}" itemprop="name">${page.title}</h1>
                </div>

                <#if page.bodySections?has_content>
                    <div class="article-section no-border">
                        <div data-uipath="ps.publication.body">
                            <@sections sections=pageSections />
                        </div>
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
