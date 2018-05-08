<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro hubBox options>
    <#if options??>
        <article class="cta hub-box">
            <#if options.background??>
            <div class="hub-box__image" style="background-image: url('${options.background}');"></div>
            </#if>
            
            <div class="hub-box__contents">
                <#if options.title??>
                    <h2 class="cta__title">
                        <#if options.link??>
                        <a href="${options.link}">
                        </#if>
                        ${options.title}
                        <#if options.link??>
                        </a>
                        </#if>
                    </h2>                
                </#if>

                <#if options.date??>
                <span class="cta__meta">${options.date}</span>
                </#if>

                <#if options.text??>
                <p class="cta__text">${options.text}</p>
                </#if>                
                
                <#if options.types??>
                <ul class="tag-list">
                <#list options.types as type>
                    <li class="tag">${type}</li>
                </#list>
                </#if>
                </ul>
            </div>
        </article>
    </#if>
</#macro>
