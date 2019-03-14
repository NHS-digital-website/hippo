<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.GlossaryList" -->

<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/stickyGroupBlockHeader.ftl">
<#include "macro/alphabeticalFilterNav.ftl">
<#include "macro/component/lastModified.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>

<#assign hasSummaryContent = document.summary.content?has_content />

<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--reset">
                <div class="local-header article-header">
                    <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>
                </div>

                <#if hasSummaryContent>
                    <div id="${slugify('Summary')}" class="article-section article-section--summary article-section--reset-top">
                        <h2><@fmt.message key="headers.summary" /></h2>

                        <div data-uipath="website.general.summary" class="article-section--summary">
                            <@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/>
                        </div>
                    </div>
                </#if>
            </div>
        </div>
        
        <#assign alphabetical_hash = group_blocks(document.glossaryItems)/>

        <div class="grid-row article-section--padded">
            <div class="column column--one-third page-block page-block--sidebar">
                <div id="sticky-nav">
                    <@alphabeticalFilterNav alphabetical_hash></@alphabeticalFilterNav>
                </div>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <dl>
                        <#list lettersOfTheAlphabet as letter>

                            <#list document.glossaryItems as glossaryitem>
                                <#if glossaryitem.title?substring(0, 1) == letter>
                                    <div class="article-section article-section--letter-group no-border" id="${slugify(letter)}">
                                        <@stickyGroupBlockHeader letter></@stickyGroupBlockHeader>

                                        <#assign glossaryitemID = glossaryitem.title?lower_case?replace(' ', '-') />

                                        <dt>
                                             <h4 id="${glossaryitemID}">
                                                <#if glossaryitem.link??>
                                                    <#if glossaryitem.link.linkType == "external">
                                                        <a href="${glossaryitem.link.link}">${glossaryitem.title}</a>
                                                    <#elseif glossaryitem.link.linkType == "internal">
                                                        <a href="<@hst.link hippobean=glossaryitem.link.link />">${glossaryitem.title}</a>
                                                    </#if>
                                                <#else>
                                                    ${glossaryitem.title}
                                                </#if>
                                            </h4>
                                        </dt>

                                        <dd><@hst.html hippohtml=glossaryitem.definition contentRewriter=gaContentRewriter/></dd>
                                    </div>
                                </#if>
                            </#list>
                        </#list>
                    </dl>
                </div>

                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
</article>
