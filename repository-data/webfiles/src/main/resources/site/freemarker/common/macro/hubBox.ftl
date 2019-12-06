<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro hubBox options>
    <#if options??>
        <#assign hasImageSection = options.imagesection?? />
        <article class="hub-box${(options.light??)?then(' hub-box--light', '')}">
            <#if options.background??>
            <div class="hub-box__image" style="background-image: url('${options.background}');"></div>
            </#if>
            
            <#if hasImageSection>
              <div class="hub-box-with-icon">
                <div class="hub-box__icon">
                  <#if options.imagesection != "EMPTY">
                  <#assign alttext = options.imagesection.alttext />
                  </#if>
                  
                  <#if options.imagesection == "EMPTY">
                      <img class="hub-box__icon__nhsimg" src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="NHS Digital article" >
                  <#elseif options.imagesection?has_content && options.imagesection.leadImage?has_content>
                      <@hst.link hippobean=options.imagesection.leadImage.original fullyQualified=true var="leadImage" />
                      <img class="hub-box__icon__img" src="${leadImage}" alt="${alttext}" />
                  <#else>
                      <img class="hub-box__icon__nhsimg" src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="NHS Digital article" >
                  </#if>
                </div>
            </#if>

            <div class="hub-box__contents">
                <#if options.colorbox?has_content>
                  <div class="colour-class-${options.colorbox}">${options.colorbox?upper_case}</div>
                </#if>

                <#if options.title??>
                    <h2 class="hub-box__title">
                        <#if options.link??>
                        <a class="hub-box__title-a" href="${options.link}">
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
            <#if hasImageSection>
            </div>
            </#if>
        </article>
    </#if>
</#macro>
