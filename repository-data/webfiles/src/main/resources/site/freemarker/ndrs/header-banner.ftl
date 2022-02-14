<#ftl output_format="HTML">
<#include "./macro/heroes/hero-options.ftl">
<#include "./macro/heroes/hero.ftl">
<#include "./include/imports.ftl">
<#include "./macro/metaTags.ftl">

<#assign heroOptions = getHeroOptions(banner) />

<#assign buttons = [] />
<#if button1Text?has_content && button1Url?has_content>
    <#assign buttons += [{
        "text": button1Text,
        "src": button1Url
    }]/>
</#if>

<#if button2Text?has_content && button2Url?has_content>
    <#assign buttons += [{
        "text": button2Text,
        "src": button2Url
    }]/>
</#if>
<#assign heroOptions += {"buttons": buttons} />

<#assign digiblocks = ['tr'] />
<#if digiblockposition == "Left & Right">
    <#assign digiblocks += ["bl"]/>
</#if>
<#assign heroOptions += {"digiblocks": digiblocks} />

<#if alignment?has_content && alignment == "Centre">
    <#assign heroOptions += {
        "alignment": "centre"
    }/>
</#if>

<#if colour?has_content>
    <#assign heroOptions += {
        "colour": colour
    }/>
</#if>

<#if banner??>
    <#assign overridePageTitle>${banner.title}</#assign>
    <@metaTags title summary></@metaTags>
    <#global test="Hello"/>
    <@hero heroOptions />
</#if>
