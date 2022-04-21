<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../../common/macro/svgMacro.ftl">
<@hst.setBundle basename="homepage.website.labels"/>

<#list pageable.items>
    <div class="grid-row grid-row--icon-link-blocks">
        <div class="icon-link-blocks icon-link-blocks--${pageable.items?size}up">
            <#items as item>
                <a class="icon-link-block" href="<@hst.link hippobean=item/>">
                    <#if showIcon && (item.pageIcon)?has_content>
                        <@hst.link hippobean=item.pageIcon.original fullyQualified=true var="image" />
                        <#if image?ends_with("svg")>
                            <#if item.icon.description?? && item.icon.description?has_content>
                                <#assign imgDescription = item.icon.description />
                                <#assign altText = imgDescription?has_content?then(imgDescription, "image of ${id}") />
                            </#if>

                            <#if altText?? && altText?has_content>
                                <@svgWithAltText svgString=item.svgXmlFromRepository altText=altText/>
                            <#else>
                                <@svgWithoutAltText svgString=item.svgXmlFromRepository/>
                            </#if>
                        <#else>
                            <img src="${image}" alt="" aria-hidden="true"/>
                        </#if>
                    </#if>
                    <h3 class="icon-link-block__title">${item.title}</h3>
                    <#if (item.type)?trim?has_content>
                        <p class="icon-link-block__subtitle">View ${item.type}</p>
                    <#else>
                        <p class="icon-link-block__subtitle">View ${getDocTypeName(item.class.name)?lower_case}</p>
                    </#if>
                </a>
            </#items>
        </div>
    </div>
</#list>
