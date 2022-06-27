<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro tabTileHeadings options type="page" area="" query="">
    <#if options??>
        <div class="tab-group" data-uipath="ps.${type}-tabs">
            <ul class="tab-group__list" role="tablist">
                <#list options as option>
                    <#assign count = "(" + option.tileCount???then(option.tileCount, 0) + ")" />
                    <#assign tab = slugify(option.tileSectionHeading) />
                    <#assign param = "?area="/>
                    <#assign queryParam = ""/>
                    <#if query?? && query?has_content>
                        <#assign queryParam = "&query=" + query/>
                    </#if>
                    <li class="tab-group__list-item ${(area=='${tab}')?string("tab-group__list-item--active","")}">
                        <a class="tab-group__list-link" id="tab-${tab}" href="${param}${tab}${queryParam}" title="${option.tileSectionHeading} tab" role="tab">${option.tileSectionHeading} ${count}</a>
                    </li>
                </#list>
            </ul>
        </div>
    </#if>
</#macro>
