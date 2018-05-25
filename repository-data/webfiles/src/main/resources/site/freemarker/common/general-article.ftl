<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/articleSections.ftl">
<#include "macro/sectionNav.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="site.website.labels"/>
<@fmt.message key="child-pages-section.title" var="childPagesSectionTitle"/>

<#assign hasSummaryContent = document.summary?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasChildPages = childPages?has_content />

<#assign sectionTitlesFound = countSectionTitles(document.sections) />

<#assign renderNav = (hasSummaryContent && (hasSectionContent && (sectionTitlesFound gte 1))) || (hasSectionContent && (sectionTitlesFound gt 1)) || (hasSectionContent && (sectionTitlesFound gte 1) && hasChildPages) />

<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="local-header article-header">
            <h1 class="local-header__title">${document.title}</h1>
        </div>

        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
                <@sectionNav getSectionNavLinks({ "document": document, "childPages": childPages, "ignoreSummary": hasSummaryContent })></@sectionNav>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <#-- BEGIN optional 'summary section' -->
                <#if hasSummaryContent>
                <div id="${slugify('Summary')}" class="article-section article-section--summary article-section--reset-top">
                    <h2>Summary</h2>
                    <p>${document.summary}</p>
                </div>
                </#if>
                <#-- END optional 'summary section' -->

                <#-- BEGIN optional 'Sections' -->
                <#if hasSectionContent>
                <@articleSections document.sections></@articleSections>
                </#if>
                <#-- END optional 'Sections' -->

                <#-- BEGIN optional 'Further information section' -->
                <#if hasChildPages>
                <div class="article-section article-section--child-pages" id="section-child-pages">
                    <h2>${childPagesSectionTitle}</h2>
                    <ol class="list list--reset cta-list">
                        <#list childPages as childPage>
                            <li>
                                <article class="cta">
                                    <#if childPage.type?? && childPage.type == "external">
                                    <#-- Assign the link property of the externallink compound -->
                                    <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, childPage.link) />
                                    <h2 class="cta__title"><a href="${childPage.link}" onClick="${onClickMethodCall}">${childPage.title}</a></h2>
                                    <#elseif hst.isBeanType(childPage, 'org.hippoecm.hst.content.beans.standard.HippoBean')>
                                    <#-- In case the childPage is not a compound but still a document in the cms, then create a link to it-->
                                    <h2 class="cta__title"><a href="<@hst.link var=link hippobean=childPage />">${childPage.title}</a></h2>
                                    </#if>
                                    <p class="cta__text">${childPage.shortsummary}</p>
                                </article>
                            </li>
                        </#list>
                    </ol>
                </div>
                </#if>
                <#-- END optional 'Further information section' -->
            </div>
        </div>
    </div>
</article>
