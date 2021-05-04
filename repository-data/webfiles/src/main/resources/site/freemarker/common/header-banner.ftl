<#ftl output_format="HTML">
<#include "../nhsd-common/macros/header-banner.ftl">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">


<#assign banner1 = {
    "text": button1Text,
    "url": button1Url
}/>

<#assign banner2 = {
    "text": button2Text,
    "url": button2Url
}/>

<#if banner??>
    <#assign overridePageTitle>${banner.title}</#assign>
    <@metaTags></@metaTags>
<#global test="Hello"/>
    <@headerBanner banner banner1 banner2 color alignment digiblockposition />
</#if>
