<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "fileMetaAppendix.ftl">

<#assign lettersOfTheAlphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]/>

<#assign matchesFound = 0/>

<#macro alphabeticalGroupOfBlocks blockGroups>
<#if blockGroups?has_content>
<#list lettersOfTheAlphabet as letter>
    <#if blockGroups[letter]??>
    <#assign matchesFound++ />

    <div class="article-section article-section--letter-group" id="section-${letter}">
        <div class="grid-row sticky sticky--top">
            <div class="column column--reset">
                <div class="article-header article-header--tertiary">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <h2 class="article-header__title">${letter}</h2>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="grid-row">
            <div class="column column--reset">
                <div class="list list--reset cta-list cta-list--sections">
                    <#list blockGroups[letter] as block>
                    <div class="cta">
                        <#if block.type??>
                            <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, block.link) />

                            <#if block.type == "external">
                            <h2 class="cta__title"><a href="${block.link}" onClick="${onClickMethodCall}">${block.title}</a></h2>
                            <p class="cta__text">${block.shortsummary}</p>
                            <#elseif block.type == "asset">
                            <h2 class="cta__title"><a href="<@hst.link hippobean=block.link />" onClick="${onClickMethodCall}">${block.title}</a><@fileMetaAppendix block.link.asset.getLength()></@fileMetaAppendix></h2>
                            </#if>
                        <#else>
                            <h2 class="cta__title"><a href="<@hst.link hippobean=block />">${block.title}</a></h2>
                            <p class="cta__text">${block.shortsummary}</p>
                        </#if>
                        
                    </div>
                    </#list>
                </div>
            </div>
        </div>
    </div>
    </#if>
</#list>
</#if>
</#macro>
