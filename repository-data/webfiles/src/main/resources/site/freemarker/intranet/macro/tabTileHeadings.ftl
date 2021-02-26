<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro tabTileHeadings options type="page" area="" query="">
    <#if options??>
        <nav class="nhsd-m-menu-bar" data-uipath="ps.${type}-tabs" role="tablist">
            <#list options as option>
                <#assign count = "(" + option.tileCount???then(option.tileCount, 0) + ")" />
                <#assign tab = slugify(option.tileSectionHeading) />
                <#assign param = "?area="/>

                <#assign queryParam = ""/>
                <#if query?? && query?has_content>
                    <#assign queryParam = "&query=" + query/>
                </#if>
                <a class="nhsd-a-menu-item ${(area=='${tab}')?string("js-active","")}" href="${param}${tab}${queryParam}">
                    <span class="nhsd-a-menu-item__label" id="tab-${tab}" title="${option.tileSectionHeading} tab" role="tab">${option.tileSectionHeading} ${count}</span>
                    <span class="nhsd-a-icon nhsd-a-icon--size-s">
                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                            <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                        </svg>
                    </span>
                </a>
            </#list>
        </nav>
    </#if>
</#macro>
