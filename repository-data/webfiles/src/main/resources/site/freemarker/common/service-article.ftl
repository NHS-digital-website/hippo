<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/sectionNav.ftl">
<#include "macro/furtherInformationSection.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>

<#assign hasSectionContent = document.sections?has_content />
<#assign hasTopTasks = document.toptasks?has_content />
<#assign hasChildPages = childPages?has_content />
<#assign hasIntroductionContent = document.introduction?? />
<#assign hasContactDetailsContent = document.contactdetails?? && document.contactdetails.content?has_content />

<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = (sectionTitlesFound gte 1 || hasChildPages) || sectionTitlesFound gt 1 || hasContactDetailsContent />

<article class="article article--service">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--reset">
                <div class="local-header article-header">
                    <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>
                </div>
            </div>
        </div>

        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <@sectionNav getSectionNavLinks({ "document": document, "childPages": childPages })></@sectionNav>
                </div>
                <#-- Restore the bundle -->
                <@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <#if hasTopTasks>
                    <#assign summarySectionClassName = "article-section article-section--summary no-border">
                <#else>
                    <#assign summarySectionClassName = "article-section article-section--summary">
                </#if>

                <div id="${slugify('Summary')}" class="${summarySectionClassName}">
                    <h2><@fmt.message key="headers.summary" /></h2>
                    <div data-uipath="website.service.summary" class="article-section--summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                </div>

                <#if hasTopTasks>
                <div class="article-section article-section--highlighted">
                    <div class="callout callout--attention">
                        <h2><@fmt.message key="headers.top-tasks" /></h2>
                        <div class="rich-text-content">
                            <#list document.toptasks as toptask>
                            <@hst.html hippohtml=toptask contentRewriter=gaContentRewriter/>
                            </#list>
                        </div>
                    </div>
                </div>
                </#if>

                <#if hasIntroductionContent>
                <div class="article-section article-section--introduction">
                    <div class="rich-text-content">
                        <@hst.html hippohtml=document.introduction contentRewriter=gaContentRewriter/>
                    </div>
                </div>
                </#if>

                <#if hasSectionContent>
                <@sections document.sections></@sections>
                </#if>

                <#if hasContactDetailsContent>
                <div class="article-section article-section--contact" id="${slugify('Contact details')}">
                    <h2><@fmt.message key="headers.contact-details" /></h2>
                    <div class="rich-text-content">
                        <@hst.html hippohtml=document.contactdetails contentRewriter=gaContentRewriter/>
                    </div>
                </div>
                </#if>

                <#if hasChildPages>
                    <@furtherInformationSection childPages></@furtherInformationSection>
                </#if>
            </div>
        </div>
    </div>
</article>
