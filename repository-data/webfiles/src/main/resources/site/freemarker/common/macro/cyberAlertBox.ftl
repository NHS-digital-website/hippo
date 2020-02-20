<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro cyberAlertBox options>
    <#if options??>
        <article class="hub-box">

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

                <ul class="tag-list">
                  <li class="tag">Severity: ${options.severity}</li>
                  <#if options.threatType??>
                      <li class="tag">Type: ${options.threatType}</li>
                  </#if>
                </ul>

            </div>
        </article>
    </#if>
</#macro>
