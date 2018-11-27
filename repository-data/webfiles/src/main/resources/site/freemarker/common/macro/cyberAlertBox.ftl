<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro cyberAlertBox options>
    <#if options??>
        <article class="hub-box ${(options.light??)?then('hub-box--light', '')}">

            <div class="hub-box__contents">

                <span class="cta__label" data-uipath="ps.search-results.result.type">${options.threatId}</span>

                <#if options.title??>
                    <h2 class="hub-box__title">
                        <#if options.link??>
                            <a href="${options.link}">
                        </#if>
                        ${options.title}
                        <#if options.link??>
                            </a>
                        </#if>
                    </h2>                
                </#if>

                <#if options.publishedDate??>
                    <span class="hub-box__meta">Published: ${options.publishedDate}, Last updated: ${options.lastModifiedDate}</span>
                    <span class="hub-box__meta"></span>
                </#if>


                <#if options.text??>
                    <p class="hub-box__text">${options.text}</p>
                </#if>

                <span class="tag">Severity: ${options.severity}</span>

                <#if options.threatType??>
                    <span class="tag">Type: ${options.threatType}</span>
                </#if>

            </div>
        </article>
    </#if>
</#macro>
