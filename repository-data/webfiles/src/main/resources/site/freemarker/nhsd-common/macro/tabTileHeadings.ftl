<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro tabTileHeadings options type="page" area="">
    <#if options??>
        <nav class="nhsd-m-tabs" data-uipath="ps.${type}-tabs">
            <div role="tablist">
            <#list options as option>
                <#assign tab = slugify(option.tileSectionHeading) />
                <#assign param = '?area='/>

                <#if option.tileSectionLinks?has_content>
                    <a class="nhsd-a-tab ${(area=='${tab}')?string("nhsd-a-tab__active","")}" href="${param}${tab}" aria-selected="false" role="tab">${option.tileSectionHeading}</a>
                </#if>
            </#list>
            </div>
        </nav>
        <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xxs nhsd-!t-margin-bottom-6" />
    </#if>
</#macro>
