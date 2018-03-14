<#ftl output_format="HTML">
<#macro hubPageComponents components>
<#if components?has_content>
<#assign component_list = components?keys/>
<#assign component_children_list = components?values/>
<#list component_list as component>
<section class="article-section article-section--hub-component">
    <div class="grid-row">
        <div class="column column--two-thirds column--left">
            <div class="rich-text-content">
                <@hst.html hippohtml=component.component contentRewriter=gaContentRewriter/>
            </div>

            <#assign seq_index = component_list?seq_index_of(component) />
            <#assign componentChildPages = component_children_list[seq_index]/>
            <#if componentChildPages?has_content>
            <ol class="component-list list list--reset cta-list">
                <#list componentChildPages as child>
                <li>
                    <article class="cta">
                        <h2 class="cta__title"><a href="<@hst.link hippobean=child/>">${child.title}</a></h2>
                        <p class="cta__text">${child.shortsummary}</p>
                    </article>
                </li>
                </#list>
            </ol>
            </#if>
        </div>
    </div>
</section>
</#list>
</#if>
</#macro>