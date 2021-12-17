<#ftl output_format="HTML">

<#macro imageSection section>

    <div class="${section.link?has_content?then('nhsd-m-image-with-link', '')} nhsd-!t-margin-bottom-6" data-uipath="ps.publication.image-section">
        XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
<#list section?keys as key>
    ${key} - ${section[key]}<BR/>
</#list>
        <#assign link = section.link />
        <#if section.link?has_content && ! section.link?starts_with("http") >
          <#assign link = "http://" + section.link />
        </#if>

        <#if link?has_content && !section.caption?has_content>
            <a href="${link}" data-uipath="ps.publication.image-section.link" >
        </#if>

            <figure class="nhsd-a-image nhsd-a-image--no-scale nhsd-a-image--round-corners nhsd-!t-margin-bottom-2" aria-hidden="true">
                <picture class="nhsd-a-image__picture ">
                 	<#if section.imageSize == 'Half width in line'>
                	<#elseif section.imageSize == 'Full width'>
                	<#elseif section.imageSize == 'Blue hero image left'>
                	<#elseif section.imageSize == 'Blue hero image right'>
                	<#elseif section.imageSize == 'Black hero image left'>
                	<#elseif section.imageSize == 'Black hero image right'>
                	<#elseif section.imageSize == 'Right column'>
                	<#elseif section.imageSize == 'Left column'>
                	<#else>	<#-- default to In line -->
                		<img src="<@hst.link hippobean=section.image/>" alt="${section.altText}" data-uipath="ps.publication.image-section.image">
                	<#/if>
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
