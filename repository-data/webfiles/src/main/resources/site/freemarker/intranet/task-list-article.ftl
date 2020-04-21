<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#if document.anchor>
<#include "../common/list-article/ordered-list-article.ftl">
<#else>
<#include "../common/list-article/unordered-list-article.ftl">
</#if>
