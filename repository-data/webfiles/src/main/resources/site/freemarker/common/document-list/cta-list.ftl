<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#if pageable?? && pageable.items?has_content>
    <div class="cta-list cta-list--grid cta-list--fitted-grid cta-list--data-and-information">
        <div class="grid-row">
          <#list pageable.items as item>
            <div class="column column--one-half ${item?counter?switch(1, "column--left", 2, "column--right")}">
                <div class="cta cta--large">
                    <#if hstRequestContext.preview>
                        <div style="position:relative">
                            <@hst.cmseditlink hippobean=item />
                        </div>
                    </#if>
                    <h3 class="cta__title">${item.title}</h3>
                    <p class="cta__text">${item.content}</p>
                    <#if item.internal?has_content>
                        <@hst.link var="link" hippobean=item.internal/>
                    <#else>
                        <#assign link=item.external/>
                    </#if>
                    <a href="${link}" class="cta__button button">${item.label}</a>
                </div>
            </div>
          </#list>
        </div>
    </div>
</#if>
