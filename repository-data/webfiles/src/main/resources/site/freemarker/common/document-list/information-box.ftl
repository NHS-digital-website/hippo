<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#if pageable?? && pageable.items?has_content>
<div class="article-section__box article-section__box--information">
    <#list pageable.items as item>
        <div class="grid-row">
            <div class="column">
                <h3 class="article-section__box-title">${item.title}</h3>
            </div>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds">
                <div class="article-section__box-contents">
                    <p>${item.content}</p>

                    <#if item.internal?has_content>
                        <@hst.link var="link" hippobean=item.internal/>
                    <#else>
                        <#assign link=item.external/>
                    </#if>
                    <a href="${link}" class="link">${item.label}</a>
                </div>
            </div>
            <div class="column column--one-third" style="position: relative;">
                <div class="article-section__box-image-wrapper">
                    <img src="<@hst.link hippobean=item.image.original />" alt="Supporting image" class="article-section__box-image" />
                </div>
            </div>
        </div>
        <div class="grid-row">
            <div class="column">
                
            </div>
        </div>
    </#list>
</div>
</#if>
