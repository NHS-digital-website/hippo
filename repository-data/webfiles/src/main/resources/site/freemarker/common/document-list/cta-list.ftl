<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#if pageable?? && pageable.items?has_content>
    <div class="cta">
        <div class="grid-row">
          <#list pageable.items as item>
            <div class="column column--one-half article-section__column ${item?is_first?then("article-section__column--left", "article-section__column--right")}">
                <#if hstRequestContext.preview>
                    <div style="position:relative">
                        <@hst.cmseditlink hippobean=item />
                    </div>
                </#if>
                <h3>${item.title}</h3>
                <p>${item.content}</p>
                <#if item.internal?has_content>
                    <@hst.link var="link" hippobean=item.internal/>
                <#else>
                    <#assign link=item.external/>
                </#if>
                <a href="${link}" class="button">${item.label}</a>
            </div>
          </#list>
        </div>
    </div>
</#if>
