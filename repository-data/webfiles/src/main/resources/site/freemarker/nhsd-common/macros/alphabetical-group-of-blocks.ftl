<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#assign lettersOfTheAlphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]/>

<#assign matchesFound = 0/>

<#macro alphabeticalGroupOfBlocks blockGroups>
<#if blockGroups?has_content>
<div id="list-page-results-list" class="nhsd-!t-margin-bottom-9">
    <#list lettersOfTheAlphabet as letter>
        <#if blockGroups[letter]??>
            <#assign matchesFound++ />
            <div data-filter-results-item>
                <#if matchesFound gt 1>
                    <hr class="nhsd-a-horizontal-rule"/>
                </#if>
                <div  id="${letter?lower_case}" class="nhsd-t-flex" data-uipath="website.glossary.list">
                    <div class="nhsd-!t-margin-right-5">
                        <span class="nhsd-a-character-block nhsd-a-character-block--large nhsd-!t-display-sticky nhsd-!t-display-sticky--offset-2">${letter}</span>
                    </div>
                    <div class="nhsd-t-flex-item--grow" data-filter-results-item>
                        <#list blockGroups[letter] as block>
                            <#-- Glossery list links -->
                            <#assign link = ""/>
                            <#if block.external??>
                                <#if block.internal?has_content || block.external?has_content>
                                    <#if block.internal?has_content>
                                        <@hst.link var="link" hippobean=block.internal/>
                                    <#else>
                                        <#assign link=block.external/>
                                    </#if>
                                </#if>
                            <#-- Link list links -->
                            <#else>
                                <#if block.linkType??>
                                    <#if block.linkType == "external">
                                        <#assign link = block.link/>
                                    <#elseif block.linkType == "asset">
                                        <@hst.link hippobean=block.link var="assetLink"/>
                                        <#assign link = assetLink/>
                                    </#if>
                                <#else>
                                    <#if block.type?? && block.type?has_content && block.type == "alternativeTask">
                                        <@hst.link hippobean=block.task var="link"/>
                                    <#else>
                                        <@hst.link hippobean=block var="link1"/>
                                        <#assign link=link1>
                                    </#if>
                                </#if>
                            </#if>

                            <h2 class="nhsd-t-heading-xs nhsd-!t-margin-bottom-1" id="${block.title?lower_case?replace(" ", "-")}">
                                <#if link?has_content>
                                    <a href="${link}" class="nhsd-a-link" data-filterable ${block.link.archiveMaterial?then('rel=archived', '')}>${block.title} ${block.link.archiveMaterial?then("[Archive content]", "")}</a>
                                <#else>
                                    ${block.title}
                                </#if>
                            </h2>

                            <#if block.shortsummary??>
                                <p class="nhsd-t-body" data-filterable>${block.shortsummary}</p>
                            <#else>
                                <div class="nhsd-t-grid nhsd-!t-no-gutters">
                                    <@hst.html hippohtml=block.definition contentRewriter=brContentRewriter/>
                                </div>
                            </#if>

                            <#if block?index lt blockGroups[letter]?size - 1>
                                <hr class="nhsd-a-horizontal-rule"/>
                            </#if>
                        </#list>
                    </div>
                </div>
            </div>
        </#if>
    </#list>
</div>
</#if>
</#macro>
