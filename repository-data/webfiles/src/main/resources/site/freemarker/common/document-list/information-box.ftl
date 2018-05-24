<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#if pageable?? && pageable.items?has_content>
<div class="content-box content-box--tertiary content-box--information content-box--padded">
    <#list pageable.items as item>
    <div class="grid-row">
        <div class="column">
            <h3 class="content-box-title">${item.title}</h3>
        </div>
    </div>

    <div class="grid-row">
        <div class="column column--two-thirds">
            <div class="content-box-contents">
                <p>${item.content}</p>

                <#if item.internal?has_content>
                    <@hst.link var="link" hippobean=item.internal/>
                <#else>
                    <#assign link=item.external/>
                </#if>
                <a href="${link}" class="link">${item.label}</a>
            </div>
        </div>
        <div class="column column--one-third">
            <div class="content-box-image-wrapper">
                <#if item.image?? && item.image.original??>
                <img src="<@hst.link hippobean=item.image.original />" alt="Supporting image" class="content-box-image"/>
                </#if>
            </div>
        </div>
    </div>
    </#list>
</div>
</#if>
