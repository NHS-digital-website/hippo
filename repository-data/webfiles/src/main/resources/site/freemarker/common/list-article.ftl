<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#if document.anchor>
<#include "list-article/ordered-list-article.ftl">
<#else>
<#include "list-article/unordered-list-article.ftl">
</#if>
