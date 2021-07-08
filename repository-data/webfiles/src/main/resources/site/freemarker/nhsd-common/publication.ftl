<#ftl output_format="HTML">

<#include "../include/imports.ftl">
<#include "./macro/metaTags.ftl">
<#include "./macro/component/lastModified.ftl">
<#include "./macro/details-banner.ftl">
<#include "./macro/publicationsystem/structured-text.ftl">
<#include "./macro/contentPixel.ftl">
<#include "./macro/stickyNavSections.ftl">
<#include "./macro/component/infoGraphic.ftl">
<#include "./macro/sections/sections.ftl">
<#include "./macro/card.ftl">
<#include '../common/macro/cardItem.ftl'>
<#include '../common/macro/gridColumnGenerator.ftl'>
<#include "./macro/component/downloadBlockAsset.ftl">
<#include "./macro/component/downloadBlockInternal.ftl">
<#include "./macro/component/downloadBlockExternal.ftl">
<#include '../common/macro/docGetImage.ftl'>
<#include "./macro/latestblogs.ftl">
<#include "./macro/component/pagination.ftl">
<#include "./macro/component/chapter-pagination.ftl">

<@hst.setBundle basename="publicationsystem.change,publicationsystem.survey,publicationsystem.interactive,publicationsystem.labels,publicationsystem.headers"/>

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#assign hasChapters = publication.publiclyAccessible && publication.pageIndex?has_content />

<article class="article article--publication ${hasChapters?then('nhsd-!t-display-chapters', '')}" itemscope itemtype="http://schema.org/Dataset" aria-label="Document Header"
         data-related-doc-links="${(relatedDocumentLinks?has_content)?then(relatedDocumentLinks, '')}">
    <meta itemprop="license" content="https://digital.nhs.uk/about-nhs-digital/terms-and-conditions"/>

    <@detailsBanner publication publication.publiclyAccessible showDownload />
    <@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

    <#if publication.publiclyAccessible && publication.pageIndex?has_content>
        <@chapterNav document />
    </#if>

    <div class="nhsd-t-grid nhsd-!t-margin-top-8">
        <div class="nhsd-t-row">
            <#if publication.publiclyAccessible>
                <div class="nhsd-t-col-xs-12 nhsd-t-col-s-3">
                    <#assign links = [] />
                    <#list index as i>
                        <#assign links += [{ "url": "#" + slugify(i), "title": i }] />
                    </#list>
                    <@stickyNavSections getStickySectionNavLinks({
                        "document": document,
                        "sections": links,
                        "keepBundles": ["publicationsystem.change","publicationsystem.survey","publicationsystem.interactive","publicationsystem.labels","publicationsystem.headers"]
                    })/>
                </div>
                <div class="nhsd-t-col-xs-12 nhsd-t-off-s-1 nhsd-t-col-s-8 js-publication-body" data-uipath="ps.publication.body">
                    <#if document.publication??>
                        <h2 class="nhsd-t-heading-xl" data-uipath="ps.publication.page-title" title="${document.title}" itemprop="name">${document.title}</h2>
                        <#if document.sections?has_content>
                            <div data-uipath="ps.publication.sections">
                                <@sections sections=pageSections wrap=true/>
                            </div>
                            <hr class="nhsd-a-horizontal-rule"/>
                        </#if>
                    <#else>
                        <@fmt.message key="headers.summary" var="summaryHeader" />
                        <h2 id="${slugify(summaryHeader)}" class="nhsd-t-heading-m">${summaryHeader}</h2>
                        <div class="nhsd-!t-margin-bottom-6" itemprop="description"><@structuredText item=document.summary uipath="ps.publication.summary" /></div>

                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <#assign hasFactHead = (document.keyFactsHead?? && document.keyFactsHead.content?has_content) />
                    <#assign hasFactTail = (document.keyFactsTail?? && document.keyFactsTail.content?has_content) />
                    <#assign hasFactInfoGraphic = (document.keyFactInfographics?? && document.keyFactInfographics?size > 0) />
                    <#assign hasInteractiveTool = (document.interactivetool?? && document.interactivetool?has_content) />

                    <#assign hasOldKeyfacts = document.keyFacts.elements?has_content || keyFactImageSections?has_content />
                    <#assign hasNewKeyfacts = hasFactHead || hasFactTail || hasFactInfoGraphic || hasInteractiveTool />

                    <#if hasNewKeyfacts || hasOldKeyfacts>
                        <@fmt.message key="headers.key-facts" var="keyFactsHeader" />
                        <div id="${slugify(keyFactsHeader)}" class="nhsd-a-box nhsd-a-box--border-grey nhsd-!t-margin-bottom-6">
                            <h2 class="nhsd-t-heading-m">${keyFactsHeader}</h2>

                            <#if hasNewKeyfacts>
                                <#if hasFactHead>
                                    <@hst.html hippohtml=document.keyFactsHead contentRewriter=brContentRewriter />
                                </#if>

                                <#if hasFactInfoGraphic>
                                    <#list document.keyFactInfographics as graphic>
                                        <@infoGraphic graphic />
                                    </#list>
                                </#if>

                                <#if hasFactTail>
                                    <@hst.html hippohtml=document.keyFactsTail contentRewriter=brContentRewriter/>
                                </#if>
                            <#elseif hasOldKeyfacts>
                                <#if document.keyFacts.elements?has_content>
                                    <@structuredText item=publication.keyFacts uipath="ps.publication.key-facts" />
                                </#if>

                                <#if keyFactImageSections?has_content>
                                    <div data-uipath="ps.publication.key-fact-images">
                                        <@sections sections=keyFactImageSections />
                                    </div>
                                </#if>
                            </#if>

                            <#if document.interactivetool?has_content>
                                <@fmt.message key="interactive.header" var="interactiveHeader" />
                                <div id="${slugify(interactiveHeader)}" class="nhsd-!t-margin-top-6">
                                    <h2 class="nhsd-t-heading-m">${interactiveHeader}</h2>
                                    <#list document.interactivetool as interactiveToolData>
                                        <#assign cardProps = {} />

                                        <#assign cardProps += interactiveToolData>

                                        <@fmt.message key="interactive.date-label" var="interactiveDateLabel" />

                                        <@fmt.formatDate value=interactiveToolData.date.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" var="expiryDate" />
                                        <#assign cardProps += {
                                            "date": interactiveDateLabel + " " + expiryDate,
                                            "icon": "link"
                                        } />

                                        <div class="nhsd-!t-margin-top-4"><@card cardProps /></div>
                                    </#list>
                                </div>
                            </#if>
                        </div>

                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <#if document.survey?has_content>
                        <div class="nhsd-!t-margin-bottom-6">
                            <#assign cardProps = {
                                "background": "light-yellow"
                            } />

                            <#assign cardProps += document.survey>

                            <@fmt.message key="survey.lead-line" var="surveyTitle" />
                            <@fmt.message key="survey.second-line" var="surveyContent" />
                            <@fmt.message key="survey.hyperlink-text" var="surveyLinkText" />
                            <@fmt.message key="survey.date-label" var="surveyDateLabel" />

                            <@fmt.formatDate value=document.survey.date.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" var="expiryDate" />
                            <#assign cardProps += {
                                "title": surveyTitle,
                                "content": surveyContent,
                                "button": surveyLinkText,
                                "date": surveyDateLabel + " " + expiryDate
                            } />

                            <@card cardProps />
                        </div>

                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <#if document.parentDocument?? && document.parentDocument?is_hash && document.parentDocument.administrativeSources?has_content>
                        <#assign administrativeSources = document.parentDocument.administrativeSources />
                    <#elseif document.administrativeSources?has_content>
                        <#assign administrativeSources = document.administrativeSources />
                    </#if>

                    <#if administrativeSources??>
                        <@fmt.message key="headers.administrative-sources" var="administrativeResourcesHeader" />
                        <div id="${slugify(administrativeResourcesHeader)}" class="nhsd-!t-margin-bottom-6">
                            <h2 class="nhsd-t-heading-m">${administrativeResourcesHeader}</h2>
                            <p class="nhsd-t-body" itemprop="isBasedOn" data-uipath="ps.publication.administrative-sources">${administrativeSources}</p>
                        </div>

                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <#if document.datasets?has_content>
                        <@fmt.message key="headers.datasets" var="datasetsHeader" />
                        <div id="${slugify(datasetsHeader)}" class="nhsd-!t-margin-bottom-6">
                            <h2 class="nhsd-t-heading-m">${datasetsHeader}</h2>
                            <ul data-uipath="ps.publication.datasets" class="nhsd-t-list nhsd-t-list--bullet">
                                <#list document.datasets as dataset>
                                    <li itemprop="hasPart" itemscope itemtype="http://schema.org/Dataset">
                                        <span itemprop="name"><a class="nhsd-a-link" itemprop="url" href="<@hst.link hippobean=dataset.selfLinkBean/>">${dataset.title}</a></span>
                                        <#list dataset.summary.elements as element>
                                            <meta itemprop="description" content="${element}"/>
                                        </#list>
                                        <meta itemprop="license" content="https://digital.nhs.uk/about-nhs-digital/terms-and-conditions"/>
                                    </li>
                                </#list>
                            </ul>
                        </div>

                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <#if document.attachments?has_content || document.resourceLinks?has_content>
                        <@fmt.message key="headers.resources" var="resourcesHeader" />
                        <div id="${slugify(resourcesHeader)}" class="nhsd-!t-margin-bottom-6">
                            <h2 class="nhsd-t-heading-m">${resourcesHeader}</h2>
                            <#if document.attachments?has_content>
                                <div data-uipath="ps.publication.resources-attachments">
                                    <#list document.attachments as attachment>
                                        <div class="nhsd-!t-margin-top-4" data-uipath="ps.publication.resources-attachment">
                                            <@externalstorageLink attachment.resource; url>
                                                <@downloadBlockAsset attachment.text attachment.resource "${attachment.text}" "" attachment.resource.mimeType attachment.resource.length false true />
                                            </@externalstorageLink>
                                        </div>
                                    </#list>
                                </div>
                            </#if>
                            <#if document.resourceLinks?has_content>
                                <#list document.resourceLinks as link>
                                    <div class="nhsd-!t-margin-top-4" data-uipath="ps.publication.resources-link">
                                        <@downloadBlockExternal document.class.name link "${link.linkText}" "" />
                                    </div>
                                </#list>
                            </#if>
                        </div>

                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <#if document.supplementaryInformation?has_content>
                        <@fmt.message key="headers.supplementary-information-requests" var="supplementaryHeader" />
                        <div id="${slugify(supplementaryHeader)}" class="nhsd-!t-margin-bottom-6">
                            <h2 class="nhsd-t-heading-m">${supplementaryHeader}</h2>
                            <div data-uipath="ps.publication.supplementary-information-requests">
                                <#list document.supplementaryInformation as supData>
                                    <div class="nhsd-!t-margin-bottom-4">
                                        <@downloadBlockInternal document.class.name supData "${supData.title}" "${supData.shortsummary}" />
                                    </div>
                                </#list>
                            </div>
                        </div>

                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <#if document.relatedNews?has_content>
                        <@fmt.message key="headers.related-news" var="relatedNewsHeader" />
                        <div id="${slugify(relatedNewsHeader)}" class="nhsd-!t-margin-bottom-2">
                            <h2 class="nhsd-t-heading-m">${relatedNewsHeader}</h2>
                            <div class="nhsd-t-grid nhsd-t-grid--nested">
                                <div class="nhsd-t-row">
                                    <#list document.relatedNews as card>
                                        <#assign imageData = getImageData(card) />

                                        <#assign itemShortSummary = "" />
                                        <#if card.shortsummary?has_content>
                                            <#assign itemShortSummary = card.shortsummary />
                                        <#elseif card.content?has_content>
                                            <#assign itemShortSummary = card.content />
                                        </#if>

                                        <#assign linkDestination = "#" />
                                        <#if card.internal?has_content>
                                            <@hst.link hippobean=card.internal var="itemLinkDestination"/>
                                            <#assign linkDestination = itemLinkDestination />
                                        <#elseif card.external?has_content>
                                            <#assign linkDestination = card.external/>
                                        <#else>
                                            <@hst.link hippobean=card var="itemLinkDestination"/>
                                            <#assign linkDestination = itemLinkDestination />
                                        </#if>

                                        <#assign cardProps = card/>
                                        <#assign cardProps += {
                                            "image": imageData[0],
                                            "alttext": imageData[1],
                                            "shortSummary": itemShortSummary,
                                            "linkDestination": linkDestination,
                                            "background": "pale-grey"
                                        }/>
                                        <div class="${getGridCol(document.relatedNews?size)} nhsd-!t-margin-bottom-4">
                                            <@cardItem cardProps />
                                        </div>
                                    </#list>
                                </div>
                            </div>
                        </div>

                        <hr class="nhsd-a-horizontal-rule nhsd-!t-margin-top-4"/>
                    </#if>

                    <#if publication.relatedLinks?has_content>
                        <@fmt.message key="headers.related-links" var="relatedLinksHeader" />
                        <div class="nhsd-!t-margin-bottom-6" id="${slugify(relatedLinksHeader)}">
                            <h2 class="nhsd-t-heading-m">${relatedLinksHeader}</h2>
                            <ul data-uipath="ps.publication.related-links" class="nhsd-t-list nhsd-t-list--bullet">
                                <#list publication.relatedLinks as link>
                                    <li><a href="${link.linkUrl}"
                                       onClick="logGoogleAnalyticsEvent('Link click','Publication','${link.linkUrl}');"
                                       onKeyUp="return vjsu.onKeyUp(event)"
                                       title="${link.linkText}">${link.linkText}</a></li>
                                </#list>
                            </ul>
                        </div>

                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <div class="nhsd-!t-margin-bottom-6">
                        <@lastModified publication.lastModified false />
                    </div>

                    <div class="nhsd-!t-margin-bottom-6">
                        <@pagination document/>
                    </div>
                </div>
            <#else>
                <div class="nhsd-t-col-12 nhsd-!t-margin-bottom-6">
                    <p class="nhsd-t-heading-xs">(Upcoming, not yet published)</p>
                </div>
            </#if>
        </div>
    </div>

    <#if hasChapters>
        <div id="chapter-index" class="nhsd-t-grid nhsd-!t-margin-bottom-6">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col-12">
                    <hr class="nhsd-a-horizontal-rule"/>
                    <@fmt.message key="headers.publication-chapters" var="publicationChapters" />
                    <h2 class="nhsd-t-heading-m">${publicationChapters}</h2>
                    <div class="nhsd-m-publication-chapter-navigation nhsd-m-publication-chapter-navigation--split" data-uipath="ps.publication.pages">
                        <ol class="nhsd-t-list nhsd-t-list--number nhsd-t-list--loose">
                            <#list publication.pageIndex as page>
                                <#assign isActive = document.getCanonicalUUID() == page.linkedBean.getCanonicalUUID()/>
                                <li ${isActive?then('class=nhsd-m-publication-chapter-navigation--active', '')} itemprop="hasPart" itemscope itemtype="http://schema.org/WebPage">
                                    <span itemprop="name"><a itemprop="url" href="<@hst.link hippobean=page.linkedBean/>" ${isActive?then('', 'data-print-article')}>${page.title}</a></span>
                                </li>
                            </#list>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
    </#if>
</article>