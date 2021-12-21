<#ftl output_format="HTML">
<#include "hero.ftl">
<#include "macro/heroes/hero-options.ftl">

<#macro imageSection section>
    <div class="${section.link?has_content?then('nhsd-m-image-with-link', '')} nhsd-!t-margin-bottom-6" data-uipath="ps.publication.image-section">
        <#assign link = section.link />
        <#assign heroOptions = getHeroOptions(document) />
        <#if section.link?has_content && ! section.link?starts_with("http") >
          <#assign link = "http://" + section.link />
        </#if>

        <#if link?has_content && !section.caption?has_content>
            <a href="${link}" data-uipath="ps.publication.image-section.link" >
        </#if>
XX${section.imageSize}XX
            <figure class="nhsd-a-image nhsd-a-image--no-scale nhsd-a-image--round-corners nhsd-!t-margin-bottom-2" aria-hidden="true">
                <picture class="nhsd-a-image__picture ">
                 	<#if section.imageSize == 'Half width in line'>
                	<#elseif section.imageSize == 'Full width'>
                	<#elseif section.imageSize == ' Blue hero image left'>
                	XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                		<#assign heroType="Blue hero">
                		<#assign heroOptions += {"colour": "Dark Blue"}>
                		<@hero heroOptions heroType />
                	<#elseif section.imageSize == 'Blue hero image right'>
                		<#assign heroType="Blue hero">
                		<#assign heroOptions += {"colour": "Dark Blue"}>
                		<@hero heroOptions heroType />
                	<#elseif section.imageSize == 'Black hero image left'>
                		<#assign heroType="Black hero">
                		<@hero heroOptions heroType />
                	<#elseif section.imageSize == 'Black hero image right'>
                		<#assign heroType="Black hero">
                		<@hero heroOptions heroType />
                	<#elseif section.imageSize == 'Right column'>
                	<#elseif section.imageSize == 'Left column'>
                	</#if>	<#-- default to In line -->
                	<img src="<@hst.link hippobean=section.image/>" alt="${section.altText}" data-uipath="ps.publication.image-section.image">
                </picture>
            </figure>

            <#if section.caption?has_content>
                <#if link?has_content>
                    <div class="nhsd-t-heading-s">
                        <a class="nhsd-a-link" href="${link}" data-uipath="ps.publication.image-section.link">${section.caption}</a>
                    </div>
                <#else>
                    <div class="nhsd-t-heading-s">
                        <span>${section.caption}</span>
                    </div>
                </#if>
            </#if>

        <#if link?has_content && !section.caption?has_content>
            </a>
        </#if>
    </div>
</#macro>
