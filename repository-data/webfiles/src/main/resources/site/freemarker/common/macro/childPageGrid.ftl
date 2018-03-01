<#ftl output_format="HTML">
<#macro childPageGrid childPages>
<#if childPages?has_content>
<#list childPages as childPage>
    <#if childPage?is_odd_item>
    <div class="grid-row">
    </#if>
        <div class="column column--one-half ${childPage?is_odd_item?then("column--left", "column--right")}">
            <article class="cta">
                <#if childPage.type?? && childPage.type == "external">
                <#-- Assign the link property of the externallink compound -->
                <h2 class="cta__title"><a href="${childPage.link}">${childPage.title}</a></h2>
                <#elseif hst.isBeanType(childPage, 'org.hippoecm.hst.content.beans.standard.HippoBean')>
                <#-- In case the childPage is not a compound but still a document in the cms, then create a link to it-->
                <h2 class="cta__title"><a href="<@hst.link var=link hippobean=childPage />">${childPage.title}</a></h2>
                </#if>
                <p class="cta__text">${childPage.shortsummary}</p>
            </article>
        </div>
    <#if childPage?is_even_item || childPage?counter==childPages?size>
    </div>
    </#if>
</#list>
</#if>
</#macro>
