<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro hubBox options>
    <#if options??>
        <article class="hub-box ${(options.light??)?then('hub-box--light', '')}">
            <#if options.background??>
            <div class="hub-box__image" style="background-image: url('${options.background}');"></div>
            </#if>
            
            <div class="hub-box__contents">
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

                <#if options.date??>
                <span class="hub-box__meta">${options.date}</span>
                </#if>

                <#if options.text??>
                <p class="hub-box__text">${options.text}</p>
                </#if>                
                
                <#if options.types??>
                <ul class="tag-list">
                <#list options.types as type>
                    <li class="tag">${type}</li>
                </#list>
                </ul>
                </#if>
            </div>
        </article>
    </#if>
</#macro>
