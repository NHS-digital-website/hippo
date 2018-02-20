<#ftl output_format="HTML">
<#macro childPageGrid childPages>
<#if childPages?has_content>
<#list childPages as childPage>
    <#if childPage?is_odd_item>
    <div class="grid-row">
    </#if>
        <div class="column column--one-half ${childPage?is_odd_item?then("column--left", "column--right")}">
            <article class="cta">
                <h2 class="cta__title"><a href="<@hst.link hippobean=childPage />">${childPage.title}</a></h2>
                <p class="cta__text">${childPage.shortsummary}</p>
            </article>
        </div>
    <#if childPage?is_even_item || childPage?counter==childPages?size>
    </div>
    </#if>
</#list>
</#if>
</#macro>