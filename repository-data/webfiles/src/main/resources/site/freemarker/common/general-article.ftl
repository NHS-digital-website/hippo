<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/articleSections.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="site.website.labels"/>
<@fmt.message key="child-pages-section.title" var="childPagesSectionTitle"/>

<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
                <div class="article-section-nav">
                    <h2 class="article-section-nav__title">Page contents</h2>
                    <hr>
                    <nav role="navigation">
                        <ol class="article-section-nav__list">
                            <#if document.summary?has_content>
                            <li><a href="#section-summary">Summary</a></li>
                            </#if>

                            <#if document.sections?has_content>
                            <#list document.sections as section>
                            <#if section.title?has_content>
                            <li><a href="#section-${section?index+1}">${section.title}</a></li>
                            </#if>
                            </#list>
                            </#if>

                            <#if childPages?has_content>
                            <li><a href="#section-child-pages">${childPagesSectionTitle}</a></li>
                            </#if>
                        </ol>
                    </nav>
                </div>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-header article-header--secondary">
                    <h1 data-uipath="document.title">${document.title}</h1>
                </div>

                <#-- BEGIN optional 'summary section' -->
                <#if document.summary?has_content>
                <div id="section-summary" class="article-section article-section--summary article-section--reset-top">
                    <p>${document.summary}</p>
                </div>
                </#if>
                <#-- END optional 'summary section' -->

                <#-- BEGIN optional 'Sections' -->
                <#if document.sections?has_content>
                <@articleSections document.sections></@articleSections>
                </#if>
                <#-- END optional 'Sections' -->

                <#-- BEGIN optional 'Further information section' -->
                <#if childPages?has_content>
                <div class="article-section article-section--child-pages article-section--last-one" id="section-child-pages">
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
