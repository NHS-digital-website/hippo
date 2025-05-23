<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.PolicyPage" -->

<#include "../include/imports.ftl">
<#include "../nhsd-common/macro/sections/sections.ftl">
<#include "../nhsd-common/macro/component/lastModified.ftl">
<#include "../nhsd-common/macro/component/pagination.ftl">
<#include "../nhsd-common/macro/component/chapter-pagination.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "./macro/heroes/hero.ftl">
<#include "./macro/heroes/hero-options.ftl">
<#include "macro/contentPixel.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.doctype.published-work,rb.generic.headers,publicationsystem.headers"/>

<#-- Define variables to prevent null values  -->
<#assign parentWork = document.policy />
<#assign parentText = "" />
<#assign parentLink = "" />

<#assign hasSectionContent = document.sections?has_content />
<#assign hasChapters = linkeddocuments?? && linkeddocuments.hippoBeans?has_content />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = sectionTitlesFound gt 1 />

<#if hasChapters>
    <#assign documents = [] />

<#-- this loop only runs once -->
    <#list linkeddocuments.hippoBeans as item>

    <#-- Cache the parent document's details -->
        <@hst.link hippobean=item var="link" />
        <#assign documents = [{ "index": 0, "id": item.identifier, "title": item.title, "link": link }] />

    <#-- Cache the chapter details -->
        <#list item.links as chapter>
            <@hst.link hippobean=chapter var="link" />
            <#assign documents += [{ "index": chapter?counter, "id": chapter.identifier, "title": chapter.title, "link": link }] />
        </#list>
    </#list>
</#if>
<#if documents[0].title??>
    <#assign parentText = documents[0].title />
</#if>
<@hst.link hippobean=parentWork var="parentLinkBean" />
<#assign parentLink = parentLinkBean />


<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>


<div class="nhsd-t-grid nhsd-t-grid--full-width nhsd-!t-display-chapters"
     aria-label="Document Header">
    <#assign heroOptions = getHeroOptionsWithMetaData(document,true)/>

    <#assign publicationStyle = document.publicationStyle.publicationStyle?has_content?then(document.publicationStyle.publicationStyle, 'bluebanner')>

    <#if document.publicationStyle.publicationStyle == 'parent'>
        <#assign parentHeroOptions = getHeroOptionsWithMetaData(parentWork,true)/>
        <#assign publicationStyle = parentWork.publicationStyle.publicationStyle/>
        <#assign heroOptions += {
        "image": parentHeroOptions.image
        }/>
    </#if>

    <#if parentText?has_content>
        <#assign introText>
            Part of ${parentLink?has_content?then("<a class=\"nhsd-a-link nhsd-a-link--col-white\" href=\"" + parentLink + "\">" + parentText + "</a>", parentText)?no_esc}
        </#assign>
    </#if>

    <#if parentWork.publicationStyle.button??>
        <#assign button = parentWork.publicationStyle.button />
    </#if>
    <#if button?? && button == "jump">
        <@fmt.message key="labels.download-pdf" var="downloadPdf"/>
        <#assign heroButtons = [{
        "text": "Jump to overview",
        "src": "#document-content",
        "type": "invert"
        },{
        "text": downloadPdf,
        "src": "#",
        "type": "invert",
        "classes": "js-print-pdf-button"
        }
        ]/>

        <#assign heroOptions += {
        "buttons": heroButtons
        }/>
    </#if>
    <#if publicationStyle == 'bluebanner' || !heroOptions.image?has_content>
        <#assign heroOptions += {
        "colour": "darkBlue",
        "introText": introText
        }/>
        <@hero heroOptions />
    <#elseif publicationStyle == 'heromodule'>
        <#assign heroOptions += {
        "introText": introText
        }/>
        <@hero heroOptions "image" />
    <#elseif publicationStyle == 'slimpicture'>
        <@hero getHeroOptions(document,true) "image" />
    </#if>


    <#if hasChapters>
        <@chapterNav document "Current chapter – " />
    </#if>

    <#-- Don't use the summary in the content when the hero module is active -->
    <#if publicationStyle = 'heromodule'>
        <#assign hasSummaryContent = false />
    <#else>
        <#assign hasSummaryContent = document.summary?? && document.summary.content?has_content />
    </#if>

    <div class="nhsd-t-grid nhsd-!t-margin-top-8">
        <div class="nhsd-t-row" id="document-content">
            <#if renderNav>
                <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4">
                    <!-- start sticky-nav -->
                    <@stickyNavSections getStickySectionNavLinks({ "document": document, "includeSummary": hasSummaryContent })></@stickyNavSections>

                    <!-- end sticky-nav -->
                    <#-- Restore the bundle -->
                    <@hst.setBundle basename="rb.doctype.published-work,rb.generic.headers,publicationsystem.headers"/>
                </div>
            </#if>

            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">
                <#if hasSummaryContent>
                    <div id="${slugify('Summary')}">
                        <h2 class="nhsd-t-heading-xl"><@fmt.message key="headers.summary"/></h2>
                        <div data-uipath="website.publishedworkchapter.summary"><@hst.html hippohtml=document.summary contentRewriter=brContentRewriter/></div>
                    </div>
                </#if>

                <#if hasSectionContent>
                    <#if hasSummaryContent>
                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <@sections document.sections></@sections>
                </#if>

                <div class="nhsd-!t-margin-bottom-6">
                    <@lastModified document.lastModified false></@lastModified>
                </div>

                <div class="nhsd-!t-margin-bottom-8">
                    <@pagination document />
                </div>

                <#if hasChapters>
                    <#assign splitChapters = splitHash(documents) />

                    <div class="nhsd-m-publication-chapter-navigation nhsd-m-publication-chapter-navigation--split nhsd-!t-margin-1"
                         id="chapter-index"
                    >
                        <ol class="nhsd-t-list nhsd-t-list--number nhsd-t-list--loose">
                            <#list splitChapters.left as chapter>
                                <#if chapter.id == document.identifier>
                                    <li class="nhsd-m-publication-chapter-navigation--active">
                                <#else>
                                    <li class="">
                                </#if>
                                <a class="nhsd-a-link"
                                   href="${chapter.link}"
                                   onClick="${getOnClickMethodCall(document.class.name, chapter.link)}"
                                   title="${chapter.title}"
                                >
                                    ${chapter.title}
                                    <span class="nhsd-t-sr-only"></span>
                                </a>
                                </li>
                            </#list>

                            <#if splitChapters.right?size gte 1>
                                <#list splitChapters.right as chapter>
                                    <#if chapter.id == document.identifier>
                                        <li class="nhsd-m-publication-chapter-navigation--active">
                                    <#else>
                                        <li class="">
                                    </#if>
                                    <a class="nhsd-a-link"
                                       href="${chapter.link}"
                                       onClick="${getOnClickMethodCall(document.class.name, chapter.link)}"
                                       title="${chapter.title}"
                                    >
                                        ${chapter.title}
                                        <span class="nhsd-t-sr-only"></span>
                                    </a>
                                    </li>
                                </#list>
                            </#if>
                        </ol>
                    </div>
                </#if>
            </div>
        </div>
    </div>
</div>
