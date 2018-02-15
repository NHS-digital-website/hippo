<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/articleSections.ftl">

<@hst.setBundle basename="site.website.labels"/>
<@fmt.message key="child-pages-section.title" var="childPagesSectionTitle"/>

<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">
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
                <div class="article-header article-header--secondary">
                    <h1>${document.title}</h1>
                </div>
                
                <#-- BEGIN mandatory 'summary section' -->
                <section id="section-summary" class="article-section article-section--summary article-section--summary-with-border">
                    <p>${document.summary}</p>
                </section>
                <#-- END mandatory 'summary section' -->

                <#-- BEGIN optional 'Sections' -->
                <@articleSections document.sections></@articleSections>
                <#-- END optional 'Sections' -->

                <#-- BEGIN optional 'Further information section' -->
                <#if childPages?has_content>
                <section class="article-section article-section--child-pages" id="section-child-pages">
                    <h2>${childPagesSectionTitle}</h2>
                    <ol class="list list-reset list-of-articles">
                        <#list childPages as childPage>
                        <@hst.link var="link" hippobean=childPage />
                        <li>
                            <article>
                                <h2><a href="${link}">${childPage.title}</a></h2>
                                <p>${childPage.shortsummary}</p>
                            </article>
                        </li>
                        </#list>
                    </ol>
                </section>
                </#if>
                <#-- END optional 'Further information section' -->
            </div>
        </div>
    </div>
</article>
