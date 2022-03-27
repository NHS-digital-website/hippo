<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../../macro/documentHeader.ftl">
<#assign overridePageTitle>${document.title}</#assign>
<#-- Add meta tags -->
<#include "../macro/metaTags.ftl">
<@metaTags title summary></@metaTags>

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Banner" -->
<#assign hasBannerImage = document.image?has_content />

<div style="position:relative">
    <@hst.manageContent hippobean=document />
</div>
<#if hasBannerImage>
    <@hst.link hippobean=document.image fullyQualified=true var="bannerImage" />
    <div class="banner-image banner-image--short article-header"
         aria-label="Document Header"
         style="background-image: url(${bannerImage});">
        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
            <div class="article-header--with-icon">
                <div class="grid-wrapper">
                    <div class="article-header__inner">
                        <div class="grid-row">
                            <div class="column--one-half column--reset">
                                <h1 class="local-header__title local-header__title--dark"
                                    data-uipath="document.title">${document.title}</h1>
                                <div class="article-header__subtitle article-header__subtitle--dark">
                                    <@hst.html hippohtml=document.content contentRewriter=gaContentRewriter/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<#else>
    <@documentHeader document 'cyber-data-security-homepage'></@documentHeader>
</#if>
