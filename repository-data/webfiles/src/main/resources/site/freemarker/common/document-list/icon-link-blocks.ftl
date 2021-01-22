<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<@hst.setBundle basename="homepage.website.labels"/>

<#list pageable.items>
    <div class="grid-row grid-row--icon-link-blocks">
        <div class="icon-link-blocks icon-link-blocks--${pageable.items?size}up">
            <#items as item>
                <a class="icon-link-block" href="<@hst.link hippobean=item/>">
                    <#if showIcon && (item.pageIcon)?has_content>
                        <@hst.link hippobean=item.pageIcon.original fullyQualified=true var="image" />
                        <#if image?ends_with("svg")>
                            <#assign colour = '000000'>
                            <#assign imageUrl = '${image?replace("/binaries", "/svg-magic/binaries")}' />
                            <#assign imageUrl += "?colour=${colour}" />
                            <div class="icon-link-block__icon">
                                <img src="${imageUrl}" alt="" aria-hidden="true"/>
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
