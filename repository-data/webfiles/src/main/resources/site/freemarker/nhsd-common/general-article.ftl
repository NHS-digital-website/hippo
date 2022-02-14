<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.General" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/furtherInformationSection.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/latestblogs.ftl">
<#include "macro/component/calloutBox.ftl">
<#include "macro/contentPixel.ftl">
<#import "app-layout-head.ftl" as alh>
<#include "macro/heroes/hero.ftl">
<#include "macro/heroes/hero-options.ftl">
<#include "macro/dialogs/modal.ftl">

<@hst.headContribution category="metadata">
    <meta name="robots" content="${document.noIndexControl?then("noindex","index")}"/>
</@hst.headContribution>

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#if document.metadata?has_content>
    <#list document.metadata as md>
        <@hst.headContribution category="metadata">
            ${md?no_esc}
        </@hst.headContribution>
    </#list>
</#if>

<@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>

<#assign hasSummaryContent = document.summary.content?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasChildPages = childPages?has_content />
<#assign hasHtmlCode = document.htmlCode?has_content />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = ((hasSummaryContent || hasChildPages) && sectionTitlesFound gte 1) || sectionTitlesFound gt 1 || (hasSummaryContent && hasChildPages) />
<#assign idsuffix = slugify(document.title) />
<#assign navStatus = document.navigationController />
<#assign hasBannerImage = document.image?has_content />
<#assign custom_summary = document.summary />
<#assign promptValue =document.propmtUserOrg[0] />
<#assign downloadPrompt = (promptValue == 'Prompt on Download' || promptValue == 'Prompt all users') />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article>
    <#assign heroType = "default" />
    <#if hasBannerImage>
        <#assign heroType = "image" />
    </#if>
    <@hero getHeroOptions(document) heroType />

    <div class="nhsd-t-grid nhsd-!t-margin-top-8">
        <#if document.updates?has_content>
            <div class="nhsd-t-row">
                <div class="nhsd-t-col-12">
                    <#assign item = {} />
                    <#list document.updates as update>
                        <#assign item += update />
                        <#assign item += {"calloutType":"update", "index":update?index} />
                        <@calloutBox item document.class.name />
                    </#list>
                </div>
            </div>
        </#if>
        <div class="nhsd-t-row">
            <#if navStatus == "withNav" && renderNav>
                <div class="nhsd-t-col-xs-12 nhsd-t-col-s-3">
                    <#assign links = [{ "url": "#top", "title": "Top of page" }] />
                    <#if document.latestNews?has_content >
                        <#assign links += [{ "url": "#related-articles-latest-news-${idsuffix}", "title": 'Latest news' }] />
                    </#if>
                    <#assign links += getStickySectionNavLinks({ "document": document, "childPages": childPages, "includeTopLink": false }) />
                    <#if document.relatedEvents?has_content >
                        <#assign links += [{ "url": "#related-articles-events-${idsuffix}", "title": 'Forthcoming events' }] />
                    </#if>
                    <@stickyNavSections links></@stickyNavSections>
                    <!-- end sticky-nav -->
                    <#-- Restore the bundle -->
                    <@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>
                </div>
            </#if>

            <div class="${(navStatus == "withNav" || navStatus == "withoutNav")?then("nhsd-t-col-xs-12 nhsd-t-off-s-1 nhsd-t-col-s-8", "nhsd-t-col-12")}">

                <@latestblogs document.latestNews 'General' 'latest-news-' + idsuffix 'Latest news' />

                <#if hasSectionContent>
                    <@sections sections=document.sections orgPrompt=downloadPrompt></@sections>
                </#if>

                <#if !document.latestNews?has_content && document.relatedNews?has_content>
                    <@fmt.message key="headers.related-news" var="relatedNewsHeader" />
                    <div id="${slugify(relatedNewsHeader)}">
                        <hr class="nhsd-a-horizontal-rule">
                        <h2 class="nhsd-t-heading-xl"
                            data-uipath="website.contentblock.section.title">${relatedNewsHeader}</h2>
                        <@latestblogs document.relatedNews 'General' 'related-news-' + idsuffix "" />
                    </div>
                </#if>

                <#if hasChildPages>
                    <#if (hasSectionContent && (document.latestNews?has_content || !document.relatedNews?has_content) ) >
                        <hr class="nhsd-a-horizontal-rule nhsd-!t-margin-bottom-6"/>
                    </#if>
                    <@furtherInformationSection childPages downloadPrompt></@furtherInformationSection>
                </#if>

                <@latestblogs document.relatedEvents 'General' 'events-' + idsuffix 'Forthcoming events' />

                <div class="nhsd-t-body nhsd-!t-margin-bottom-8">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>

    <#if downloadPrompt>
        <@hst.setBundle basename="rb.modal.download"/>
        <@fmt.message key="modal.download.download.header" var="modalDownloadHeader" />
        <@fmt.message key="modal.download.all.users.header" var="modalAllUsersHeader" />
        <@fmt.message key="modal.download.intro" var="modalIntro" />
        <@fmt.message key="modal.download.placeholder.text" var="modalPlaceholderText" />
        <@fmt.message key="modal.download.confirm.button" var="modalConfirmButton" />
        <@fmt.message key="modal.download.decline.text" var="modalDeclineText" />
        <@fmt.message key="modal.download.org.not.listed.text" var="modalOrgNotListed" />
        <@fmt.message key="modal.download.org.not.selected.error" var="modalOrgNotSelectedError" />

        <@modal 'track-download-modal' { "mandatory": true }>
        <#if promptValue == 'Prompt all users'>
            <h1 class="nhsd-t-heading-m">${modalAllUsersHeader}</h1>
        <#else>
            <h1 class="nhsd-t-heading-m">${modalDownloadHeader}</h1>
        </#if>
            <p class="nhsd-t-body">${modalIntro}</p>

            <p id="org-not-selected" class="nhsd-t-body nhsd-!t-col-red" hidden>${modalOrgNotSelectedError}</p>
            <div class="nhsd-o-dropdown nhsd-o-dropdown--full-width nhsd-!t-margin-bottom-2" id="autocomplete-default">
                <div class="nhsd-o-dropdown__input">
                    <div class="nhsd-m-search-bar nhsd-m-search-bar__small nhsd-m-search-bar__full-width">
                        <label class="control-label" for="org-search">Search for an organisation</label>
                        <div class="nhsd-t-form-control">
                            <input
                                    class="nhsd-t-form-input"
                                    type="text"
                                    id="org-search"
                                    name="query"
                                    autocomplete="off"
                                    placeholder="${modalPlaceholderText}"
                                    aria-label="Keywords"
                                    role="combobox"
                                    aria-expanded="false"
                                    aria-autocomplete="list"
                                    aria-owns="autocomplete-default-dropdown-list"
                                    data-api-url="<@hst.link path= "/" mount="restapi"/>/orgname"
                            />
                            <span class="nhsd-t-form-control__button">
                                <button class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent" type="submit" aria-label="Perform search">
                                  <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                      <path d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/>
                                    </svg>
                                  </span>
                                </button>
                              </span>
                        </div>
                    </div>
                </div>
                <div class="nhsd-o-dropdown__dropdown">
                    <ul role="listbox" id="autocomplete-default-dropdown-list"></ul>
                </div>
            </div>
            <nav class="nhsd-m-button-nav nhsd-m-button-nav--condensed nhsd-!t-text-align-left">
                <button id="track-download-confirm-org" class="nhsd-a-button" type="button">
                    <span class="nhsd-a-button__label">${modalConfirmButton}</span>
                </button>
            </nav>

            <div class="nhsd-!t-margin-top-6">
                <div><a data-organisation="DECLINE" href="#" class="nhsd-a-link js-track-org-button">${modalDeclineText}</a></div>
                <div class="nhsd-!t-margin-top-3"><a data-organisation="NOT FOUND" href="#" class="nhsd-a-link js-track-org-button">${modalOrgNotListed}</a></div>
            </div>
        </@modal>
        <#if promptValue == 'Prompt all users'>
            <script>window.openOrgTrackingModal = true;</script>
        </#if>
    </#if>
</article>

<#if hasHtmlCode>
    ${document.htmlCode?no_esc}
</#if>
