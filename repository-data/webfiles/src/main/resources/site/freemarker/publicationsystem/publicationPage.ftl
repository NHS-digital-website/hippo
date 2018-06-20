<#ftl output_format="HTML">
<#include "./macro/publicationHeader.ftl">
<#include "./publication.ftl">

<#include "../include/imports.ftl">
<#include "../common/macro/sectionNav.ftl">

<#function getSectionNavLinks index>
    <#assign links = [] />

    <#if index?has_content>
        <#list index as i>
            <#assign links += [{ "url": "#" + slugify(i), "title": i }] />
        </#list>
    </#if>

    <#return links />
</#function>

<article class="article article--chaptered-publication" itemscope itemtype="http://schema.org/WebPage">
    <#if page.publication??>
    <div itemprop="isPartOf" itemscope itemtype="http://schema.org/PublicationIssue">
        <@publicationHeader publication=page.publication />
    </div>
    </#if>

    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <#if index?has_content>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <@sectionNav getSectionNavLinks(index)></@sectionNav>
                </div>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <h1 data-uipath="ps.publication.page-title" title="${page.title}" itemprop="name">${page.title}</h1>
                </div>

                <div class="article-section no-border">
                    <#if page.bodySections?has_content>
                        <div data-uipath="ps.publication.body">
                            <@sections sections=pageSections />
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</article>
