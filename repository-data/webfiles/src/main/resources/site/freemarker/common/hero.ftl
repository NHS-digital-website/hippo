<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../nhsd-common/macro/heroes/hero.ftl">
<#include "../nhsd-common/macro/banners/nhsd-banner.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Calltoaction" -->
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Banner" -->
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Video" -->

<@hst.setBundle basename="rb.generic.texts"/>

<#assign hasDocument = document?has_content />

<#-- Size -->
<#assign isHero = size == "tall" />

<#-- Alignment -->
<#assign hasTextAlignment = textAlignment?has_content />
<#assign mirrored = (hasTextAlignment && textAlignment == "right")?then("nhsd-o-hero-feature--mirrored","")/>

<#assign breadcrumb  = hstRequestContext.getAttribute("bread")>

<#if (breadcrumb=="Coronavirus" || breadcrumb=="News") && !hstRequestContext.getAttribute("headerPresent")?if_exists>
    <#assign overridePageTitle>${breadcrumb}</#assign>
    <@metaTags></@metaTags>
</#if>

<#-- Colourbar -->
<#assign hasColourBar = isTall?then(displayColourBar, false) />

<#if hasDocument>
    <#assign heroOptions = getHeroOptions(document) />

    <#assign headingLevel = 1/>
    <#if hstRequestContext.getAttribute("headerPresent")?if_exists>
        <#assign headingLevel = 2/>
    </#if>
    <#assign heroOptions += {
        "headingLevel": headingLevel
    }/>

    <#if document.internal?has_content || document.external?has_content || document.link?has_content>
        <#if document.internal?has_content>
            <@hst.link hippobean=document.internal var="link"/>
            <#assign linkLabel = document.label />
        <#elseif document.external?has_content>
            <#assign link = document.external/>
            <#assign linkLabel = document.label />
        <#elseif document.link?has_content>
            <@hst.link hippobean=document.link var="link"/>
            <#assign linkLabel = document.link.title />
        </#if>

        <#if linkLabel?has_content>
            <@fmt.message key="text.sr-only-link" var="srOnlyLinkText" />
            <#assign button = {
                "text": linkLabel,
                "src": link,
                "srText": document.external?has_content?then(srOnlyLinkText, '')
            }/>
            <#assign heroOptions += {
                "buttons": [button]
            }/>
        </#if>
    </#if>

    <#if document.videoUri?has_content>
        <#assign heroOptions += {
            "video": "${document.videoUri}&rel=0&enablejsapi=1"
        }/>
    </#if>

    <#if displayColourBar?has_content>
        <#assign heroOptions += {
            "colourBar": displayColourBar
        }/>
    </#if>

    <#if colour?has_content>
        <#assign heroOptions += {
        "colour": colour
        }/>
    </#if>

    <#if isHero>
        <#assign heroType = "accentedImage" />
        <#if textAlignment?has_content && textAlignment == "right">
            <#assign heroType = "accentedImageMirrored" />
        </#if>
        <@hero heroOptions heroType/>
    <#else>
        <#assign mirrored = false />
        <#if textAlignment?has_content && textAlignment == "right">
            <#assign mirrored = true />
        </#if>
        <@nhsdBanner heroOptions mirrored/>
    </#if>
</#if>
