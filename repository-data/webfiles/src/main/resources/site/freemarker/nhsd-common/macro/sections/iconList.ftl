<#ftl output_format="HTML">
<#include "iconListItemWithLink.ftl">
<#include "iconListItemWithoutLink.ftl">

<#macro iconList section isPreviousSectionEmphasisBox sectionCounter=1>
    <div id="${slugify(section.title)}">
        <div class="nhsd-o-icon-list">
            <#if section.title?has_content>
                <#if section.headingLevel == 'Main heading'>
                    <p class="nhsd-t-heading-xl" data-uipath="website.contentblock.iconlist.title">${section.title}</p>
                <#else>
                    <p class="nhsd-t-heading-l" data-uipath="website.contentblock.iconlist.title">${section.title}</p>
                </#if>
            </#if>

            <div class="nhsd-!t-margin-bottom-6" data-uipath="website.contentblock.iconlist.introduction">
                <@hst.html hippohtml=section.introduction contentRewriter=brContentRewriter />
            </div>

            <#list section.iconListItems as iconListItem>
                <#if iconListItem.itemlink?has_content>
                    <@iconListItemWithLink iconListItem />
                <#else>
                    <@iconListItemWithoutLink iconListItem />
                </#if>

                <#if !iconListItem?is_last >
                    <hr class="nhsd-a-horizontal-rule" />
                </#if>
            </#list>

        </div>
    </div>
</#macro>
