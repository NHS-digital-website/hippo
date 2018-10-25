<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/alphabeticalFilterNav.ftl">
<#include "macro/alphabeticalGroupOfGDPRDocuments.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.doctype.gdpr-summary"/>
<#assign hasSummaryContent = document.summary?? && document.summary?has_content />
<#assign hasBodyContent = document.body.content?? && document.body.content?has_content />
<#assign hasContent = hasBodyContent || hasSummaryContent />
<#assign hasContactDetails = document.contactdetails?? && document.contactdetails.content?has_content />

<#assign alphabetical_hash = {} />
<#if facets??>
    <#if facets.folders?? && facets.folders?has_content>
        <#list facets.folders as facet>
            <#assign key = facet.displayName />
            <#if facet.resultSet?? && facet.resultSet.documents??>
                <#list facet.resultSet.documents as gdprDocument>
                    <#assign alphabetical_hash = alphabetical_hash + {  key : (alphabetical_hash[key]![]) + [ gdprDocument ] } />
                </#list>
            </#if>
        </#list>
    </#if>
</#if>

<article class="article article--gdpr-summary">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds column--reset">
                <div class="article-header article-header--secondary">
                    <h1 data-uipath="document.title">${document.title}</h1>
                </div>
            </div>
        </div>

        <#if hasContent || hasContactDetails>
        <div class="grid-row">
            <div class="column column--two-thirds column--reset">
                <#if hasContent>
                <div id="section-summary" class="article-section article-section--summary no-border">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="rich-text-content">
                                <#if hasSummaryContent>
                                    <div data-uipath="website.gdprsummary.summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                                </#if>

                                <#if hasBodyContent>
                                    <@hst.html hippohtml=document.body contentRewriter=gaContentRewriter/>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
                </#if>

                <#if hasContactDetails>
                <#assign contactSectionClassName = hasContent?then("article-section article-section--contact", "article-section article-section--contact no-border") />
                <div class="${contactSectionClassName}" id="contact-details">
                    <h2><@fmt.message key="headers.contact-details" /></h2>
                    <div class="rich-text-content">
                        <@hst.html hippohtml=document.contactdetails contentRewriter=gaContentRewriter/>
                    </div>
                </div>
                </#if>
            </div>
        </div>
        </#if>

        <#-- [FTL-BEGIN] 'Article groups' section -->
        <#if alphabetical_hash?? && alphabetical_hash?size gt 0>
            <div class="article-section">
                <div class="grid-row">
                    <div class="column column--two-thirds column--reset">
                        <h2><@fmt.message key="headers.register-of-assets" /></h2>
                    </div>
                </div>

                <div class="grid-row">
                    <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
                        <@alphabeticalFilterNav alphabetical_hash></@alphabeticalFilterNav>
                    </div>

                    <div class="column column--two-thirds page-block page-block--main">
                        <@alphabeticalGroupOfGDPRDocuments alphabetical_hash></@alphabeticalGroupOfGDPRDocuments>
                    </div>
                </div>
            </div>
        </#if>
        <#-- [FTL-END] 'Article groups' section -->
    </div>
</article>
