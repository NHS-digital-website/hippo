<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#if document??>
<#assign hasLink = document.link?? />
<#assign hasImage = document.image?has_content />

<div class="grid-wrapper grid-wrapper--article">
    <div class="promo-banner-lightup">
        <#if hasLink>
        <a href="<@hst.link hippobean=document.link />">
        </#if>
            <div class="promo-banner__columns-lightup">
                <#if hasImage>
                <div class="promo-banner__column-lightup-left">
                    <@hst.link hippobean=document.image var="imageLink" />
                    <#assign style = "background-image: url(${imageLink});" />
                    <#assign style += "filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${imageLink}', sizingMethod='scale');" />
                    <#assign style += "-ms-filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${imageLink}', sizingMethod='scale');" />
                    <div class="promo-banner__graphics-lightup" style="${style}"></div>
                </div>
                </#if>
                
                <div class="promo-banner__column-lightup-right">
                    <div class="promo-banner__contents-lightup">
                        <#if document.title??>
                            <h2 class="promo-banner__title-lightup">${document.title}</h2>
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
