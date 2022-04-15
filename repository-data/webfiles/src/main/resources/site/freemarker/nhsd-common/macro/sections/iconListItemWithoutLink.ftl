<#ftl output_format="HTML">

<#include "../../../common/macro/svgMacro.ftl">

<#macro iconListItemWithoutLink iconListItem>
    <article class="nhsd-m-icon-list-item nhsd-!t-margin-bottom-6">
         <div class="nhsd-m-icon-list-item__box nhsd-m-icon-list-item__without-link" >
            <span class="nhsd-a-icon nhsd-a-icon--size-xl" style="min-width:36px;min-height:36px;">
                <@hst.link hippobean=iconListItem.image.original fullyQualified=true var="iconImage" />
                <#if iconImage?ends_with("svg")>
                    <#if section.title?? && section.title?has_content>
                        <@svgWithAltText svgString=iconListItem.svgXmlFromRepository altText=section.title/>
                    <#else>
                        <@svgWithoutAltText svgString=iconListItem.svgXmlFromRepository/>
                    </#if>
                <#else>
                    <img aria-hidden="true" src="${iconImage}" alt="${section.title}" width="100" height="100" />
                </#if>
            </span>

            <div>
                <#if iconListItem.heading?has_content >
                    <span class="nhsd-t-heading-s" data-uipath="website.contentblock.iconlistitem.heading">${iconListItem.heading}</span>
                </#if>
                <div class="nhsd-t-word-break" data-uipath="website.contentblock.iconlistitem.description" >
                    <@hst.html hippohtml=iconListItem.description contentRewriter=brContentRewriter />
                </div>
            </div>
        </div>
    </article>

</#macro>