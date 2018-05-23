<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#if document??>
<#assign hasLink = document.link?? />
<#assign hasImage = document.image?has_content />

<div class="grid-wrapper grid-wrapper--article">
    <div class="promo-banner">
        <#if hasLink>
        <a href="<@hst.link hippobean=document.link />">
        </#if>
            <div class="promo-banner__columns">
                <#if hasImage>
                <div class="promo-banner__column">
                    <@hst.link hippobean=document.image var="imageLink" />
                    <#assign style = "background-image: url(${imageLink});" />
                    <#assign style += "filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${imageLink}', sizingMethod='scale');" />
                    <#assign style += "-ms-filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${imageLink}', sizingMethod='scale');" />
                    <div class="promo-banner__graphics" style="${style}"></div>
                </div>
                </#if>
                
                <div class="promo-banner__column">
                    <div class="promo-banner__contents">
                        <#if document.title??>
                            <h2 class="promo-banner__title">${document.title}</h2>
                        </#if>
                        <@hst.html hippohtml=document.content/>
                    </div>
                </div>
            </div>
        <#if hasLink>
            </a>
        </#if>
    </div>
</div>
</#if>
