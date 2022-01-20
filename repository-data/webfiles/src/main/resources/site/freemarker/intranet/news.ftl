<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "../nhsd-common/macro/heroes/hero.ftl">
<#include "../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../nhsd-common/macro/stickyNavSections.ftl">
<#include "../nhsd-common/macro/sections/sections.ftl">
<#include "../nhsd-common/macro/latestblogs.ftl">

<#assign latestNewsHeading = 'Latest news' />

<#-- Add meta tags -->
<@metaTags></@metaTags>

<article itemscope itemtype="https://schema.org/NewsArticle">
    <#assign heroOptions = getHeroOptionsWithMetaData(document) />
    <#assign heroOptions += {
        "colour": "darkBlue",
        "introText": "News"
    }/>
    <@hero heroOptions />

    <div class="nhsd-t-grid nhsd-!t-margin-top-8">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-3">
                <!-- start sticky-nav -->
                <#assign links = getStickySectionNavLinks({ "document": document, "includeTopLink": true }) />
                <#if document.latestNews?has_content>
                    <#assign links += [{ "url": "#${slugify(latestNewsHeading)}", "title": latestNewsHeading }] />
                </#if>
                <@stickyNavSections links></@stickyNavSections>
                <!-- end sticky-nav -->
            </div>

            <div class="nhsd-t-col-xs-12 nhsd-t-off-s-1 nhsd-t-col-s-8">
                <#if document.leadImageSection?has_content>
                    <div class="nhsd-o-gallery">
                        <div class="nhsd-o-gallery__card-container">
                            <div class="nhsd-m-card">
                                <div class="nhsd-a-box nhsd-a-box--border-grey">
                                    <figure class="nhsd-a-image nhsd-a-image--round-top-corners" itemprop="image" itemscope itemtype="http://schema.org/ImageObject">
                                        <@hst.link hippobean=document.leadImageSection.leadImage.newsPostImage2x fullyQualified=true var="leadImage" />
                                        <meta itemprop="url" content="${leadImage}" />
                                        <picture class="nhsd-a-image__picture ">
                                            <#assign leadImageAltText = document.leadImageSection.alttext?has_content?then(document.leadImageSection.alttext, "") />
                                            <img itemprop="contentUrl" src="${leadImage}" alt="${leadImageAltText}">
                                        </picture>
                                    </figure>
                                    <#if document.leadImageSection.imagecaption?has_content>
                                        <div class="nhsd-m-card__content_container">
                                            <div class="nhsd-m-card__content-box" data-uipath="website.blog.leadimagecaption">
                                                <div class="nhsd-!t-margin-bottom-3"><@hst.html hippohtml=document.leadImageSection.imagecaption contentRewriter=brContentRewriter/></div>
                                            </div>
                                        </div>
                                    </#if>
                                </div>
                            </div>
                        </div>
                    </div>

                    <hr class="nhsd-a-horizontal-rule" />
                </#if>

                <#if document.sections?has_content>
                    <@sections sections=document.sections orgPrompt=downloadPrompt></@sections>
                </#if>

                <#if document.latestNews?has_content>
                    <hr class="nhsd-a-horizontal-rule" />

                    <#assign idsuffix = slugify(document.title) />
                    <@latestblogs document.latestNews 'News' 'events-' + idsuffix latestNewsHeading false />
                </#if>
            </div>
        </div>
    </div>
</article>
