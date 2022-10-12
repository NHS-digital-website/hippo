<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<div class="nav">
    Navigation:
    <a class="nhsd-a-link nhsd-a-link--col-white" href="/site/automated-test-pages/macros?macro=card">Cards</a>
    <a class="nhsd-a-link nhsd-a-link--col-white" href="/site/automated-test-pages/macros?macro=banner">Banners</a>
    <a class="nhsd-a-link nhsd-a-link--col-white" href="/site/automated-test-pages/macros?macro=filterMenu">Filter Menu</a>
    <a class="nhsd-a-link nhsd-a-link--col-white" href="/site/automated-test-pages/macros?macro=gallery">Gallery</a>
</div>

<#if macro == "card">
    <#include "./macro/cards.ftl">
<#elseif macro == "banner">
    <#include "./macro/banners.ftl">
<#elseif macro == "filterMenu">
    <#include "./macro/filterMenu.ftl">
<#elseif macro == "gallery">
    <#include "./macro/gallery.ftl">
</#if>
