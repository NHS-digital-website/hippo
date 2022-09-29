<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<div class="nav">
    Navigation:
    <a class="nhsd-a-link nhsd-a-link--col-white" href="/site/automated-test-pages/macros?macro=card">Cards</a>
</div>

<#if macro == "card">
    <#include "./macro/cards.ftl">
</#if>
