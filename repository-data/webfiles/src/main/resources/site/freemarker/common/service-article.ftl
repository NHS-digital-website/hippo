<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/articleSections.ftl">

<@hst.setBundle basename="site.website.labels"/>
<@fmt.message key="child-pages-section.title" var="childPagesSectionTitle"/>

<article class="article article--service">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="local-header article-header">
            <h1 class="local-header__title">${document.title}</h1>
        </div>

        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar page-block--sticky-top">
                <div class="article-section-nav">
                    <h2 class="article-section-nav__title">Page contents</h2>
                    <hr>
                    <nav role="navigation">
                        <ol class="article-section-nav__list">
                            <li><a href="#section-summary">Summary</a></li>
                            <#if document.sections?has_content>
                            <#list document.sections as section>
                            <li><a href="#section-${section?index+1}">${section.title}</a></li>
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
                <#-- [FTL-BEGIN] mandatory 'summary section' -->
                <#if document.toptasks?has_content>
                    <#assign summarySectionClassName = "article-section article-section--summary article-section--no-border">
                <#else>
                    <#assign summarySectionClassName = "article-section article-section--summary">
                </#if>
                <#-- [FTL-END] mandatory 'Summary' section -->

                <section id="section-summary" class="${summarySectionClassName}">
                    <h2>Summary</h2>
                    <p>${document.summary}</p>
                </section>
                <#-- [FTL-END] mandatory 'Summary' section -->

                <#-- [FTL-BEGIN] optional list of 'Top tasks' section -->
                <#if document.toptasks?has_content>
                <section class="article-section article-section--top-tasks">
                    <div class="callout callout--attention">
                        <h2>Top Tasks</h2>
                        <div class="rich-text-content">
                            <#list document.toptasks as toptask>
                            <@hst.html hippohtml=toptask contentRewriter=gaContentRewriter/>
                            </#list>
                        </div>
                    </div>
                </section>
                </#if>
                <#-- [FTL-END] optional list of 'Top tasks' section -->

                <#-- [FTL-BEGIN] 'Introduction' section -->
                <#if document.introduction??>
                <div class="article-section article-section--introduction">
                    <div class="rich-text-content">
                        <@hst.html hippohtml=document.introduction contentRewriter=gaContentRewriter/>
                    </div>
                </div>
                </#if>
                <#-- [FTL-END] 'Introduction' section -->

                <@articleSections document.sections></@articleSections>

                <#-- [FTL-BEGIN] 'Further information' section -->
                <#if childPages?has_content>
                <section class="article-section article-section--child-pages article-section--last-one" id="section-child-pages">
                    <h2>Further information</h2>
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
                </section>
                </#if>
                <#-- [FTL-END] 'Further information' section -->
            </div>
        </div>
    </div>
</article>
