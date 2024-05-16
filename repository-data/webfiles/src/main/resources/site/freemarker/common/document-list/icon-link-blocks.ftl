<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<@hst.setBundle basename="homepage.website.labels"/>

<#list pageable.items>
    <div class="grid-row grid-row--icon-link-blocks">
        <div class="icon-link-blocks icon-link-blocks--${pageable.items?size}up">
            <#items as item>
                <a class="icon-link-block" href="<@hst.link hippobean=item/>">
                    <#if showIcon && (item.pageIcon)?has_content>
                        <@hst.link hippobean=item.pageIcon.original fullyQualified=true var="image" />
                        <#if image?ends_with("svg")>
                            <div class="icon-link-block__icon">
                                <#if item.icon.description?? && item.icon.description?has_content>
                                    <#assign imgDescription = item.icon.description />
                                    <#assign altText = imgDescription?has_content?then(imgDescription, "image of ${id}") />
                                </#if>

                                <#if altText?? && altText?has_content>
                                    <img src="data:image/svg+xml;base64,${base64(colour(item.svgXmlFromRepository, "000000"))}" alt="${altText}" aria-hidden="true"/>
                                <#else>
                                    <img src="data:image/svg+xml;base64,${base64(colour(item.svgXmlFromRepository, "000000"))}" alt="" aria-hidden="true"/>
                                </#if>
                            </div>
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
