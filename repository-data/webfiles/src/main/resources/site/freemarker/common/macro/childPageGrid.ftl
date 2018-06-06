<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "fileMetaAppendix.ftl">

<#macro childPageGrid childPages>
<#if childPages?has_content>
<#list childPages as childPage>
    <#if childPage?is_odd_item>
    <div class="grid-row">
    </#if>
        <div class="column column--one-half ${childPage?is_odd_item?then("column--left", "column--right")}">
            <article class="cta cta--hf">
                <#if childPage.type?? && (childPage.type == "external" || childPage.type == "asset")>
                    <#-- Assign the link property of the externallink compound -->
                    <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, childPage.link) />
                    <#if childPage.type == "external">
                        <span data-type="external" class="hidden"></span>
                        <h2 class="cta__title"><a href="${childPage.link}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${childPage.title}</a></h2>
                    <#elseif childPage.type == "asset">
                        <span data-type="asset" class="hidden"></span>
                        <h2 class="cta__title"><a href="<@hst.link hippobean=childPage.link />" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${childPage.title}</a><@fileMetaAppendix childPage.link.asset.getLength()></@fileMetaAppendix></h2>
                    </#if>
                <#elseif hst.isBeanType(childPage, 'org.hippoecm.hst.content.beans.standard.HippoBean')>
                    <#-- In case the childPage is not a compound but still a document in the cms, then create a link to it-->
                    <span data-type="internal" class="hidden"></span>
                    <h2 class="cta__title"><a href="<@hst.link var=link hippobean=childPage />">${childPage.title}</a></h2>
                </#if>
                
                <#if childPage.shortsummary?? && childPage.shortsummary?has_content>
                    <p class="cta__text">${childPage.shortsummary}</p>
                </#if>
            </article>
        </div>
    <#if childPage?is_even_item || childPage?counter==childPages?size>
    </div>
    </#if>
</#list>
</#if>
</#macro>
