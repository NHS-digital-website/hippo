<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<div class="nav">
    Navigation:
    <a class="nhsd-a-link nhsd-a-link--col-white" href="/site/automated-test-pages/macros?macro=header">Header</a>
    <a class="nhsd-a-link nhsd-a-link--col-white" href="/site/automated-test-pages/macros?macro=banner">Banners</a>
    <a class="nhsd-a-link nhsd-a-link--col-white" href="/site/automated-test-pages/macros?macro=card">Cards</a>
    <a class="nhsd-a-link nhsd-a-link--col-white" href="/site/automated-test-pages/macros?macro=case-study">Case Study</a>
    <a class="nhsd-a-link nhsd-a-link--col-white" href="/site/automated-test-pages/macros?macro=filterMenu">Filter Menu</a>
    <a class="nhsd-a-link nhsd-a-link--col-white" href="/site/automated-test-pages/macros?macro=footer">Footer</a>
</div>

<#if macro == "banner">
    <#include "./macro/banners.ftl">
<#elseif macro == "card">
    <#include "./macro/cards.ftl">
<#elseif macro == "case-study">
    <#include "./macro/caseStudy.ftl">
<#elseif macro == "filterMenu">
    <#include "./macro/filterMenu.ftl">
<#elseif macro == "footer">
    <#include "./macro/footer.ftl">
<#elseif macro == "header">
    <#include "./macro/header.ftl">
</#if>
