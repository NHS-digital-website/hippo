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

<#assign alphabeticalGroupHash = {} />
<#list document.glossaryItems as glossaryitem>
    <#assign key = glossaryitem.title?substring(0, 1)?cap_first>
    <#assign alphabeticalGroupHash = alphabeticalGroupHash + {  key : (alphabeticalGroupHash[key]![]) + [ glossaryitem ] } />
</#list>


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

                        <div data-uipath="website.glossary.summary" class="article-section--summary">
                            <@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/>
                        </div>
                    </div>
                </#if>
            </div>
        </div>

        <#assign alphabetical_hash = group_blocks(document.glossaryItems)/>

        <div class="grid-row article-section--padded">
            <div class="column column--one-third page-block page-block--sidebar">
                <!-- start sticky-nav -->
                <div id="sticky-nav">
                    <@alphabeticalFilterNav alphabetical_hash></@alphabeticalFilterNav>
                </div>
                <!-- end sticky-nav -->
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <div data-uipath="website.glossary.list">
                        <#list lettersOfTheAlphabet as letter>
                            <#if alphabeticalGroupHash[letter]??>
                                <div class="article-section article-section--letter-group no-border" id="${slugify(letter)}">
                                    <@stickyGroupBlockHeader letter></@stickyGroupBlockHeader>
                                    <dl>
                                        <#list alphabeticalGroupHash[letter] as glossaryitem>
                                            <#assign glossaryitemID = glossaryitem.title?lower_case?replace(' ', '-') />
                                            <dt class="glossaryItemHeader" id="${glossaryitemID}">
                                                <#if glossaryitem.internal?has_content || glossaryitem.external?has_content>
                                                    <#if glossaryitem.internal?has_content>
                                                        <@hst.link var="link" hippobean=glossaryitem.internal/>
                                                    <#else>
                                                        <#assign link=glossaryitem.external/>
                                                    </#if>
                                                    <a href="${link}">${glossaryitem.title}</a>
                                                <#else>
                                                    ${glossaryitem.title}
                                                </#if>
                                            </dt>
                                            <dd class="bottom-margin-20">
                                                <@hst.html hippohtml=glossaryitem.definition contentRewriter=gaContentRewriter/>
                                            </dd>
                                        </#list>
                                    </dl>
                                </div>
                            </#if>
                        </#list>
                    </div>
                </div>

                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
</article>
