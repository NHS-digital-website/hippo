<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#if document.anchor>
<#include "list-article/ordered-list-article.ftl">
<#else>
<#include "list-article/unordered-list-article.ftl">
</#if>
