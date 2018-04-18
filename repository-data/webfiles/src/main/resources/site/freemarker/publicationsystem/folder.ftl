<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#if archive??>
    <#include "./archive.ftl">
<#elseif series??>
    <#include "./series.ftl">
<#elseif publication??>
    <#include "./publication.ftl">
<#elseif legacyPublication??>
    <#include "./legacy-publication.ftl">
<#else>
    <section class="document-content" aria-label="Document Content">
        <div class="layout layout--large">
            <div class="layout__item layout-3-3">
                <h1 data-uipath="document.title">Page not found</h1>
                <p>The page you're looking for does not exist. But we did find some other content.</p>
                <ul><#list publications as publication>
                    <li>
                        <a href="<@hst.link hippobean=publication.selfLinkBean/>">${publication.title}</a>
                        <p><@truncate text=publication.summary.firstParagraph size="300"/></p>
                    </li>
                </#list></ul>
            </div>
        </div>
    </section>
</#if>
