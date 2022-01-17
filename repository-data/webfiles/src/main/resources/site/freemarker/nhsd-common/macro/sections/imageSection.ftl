<#ftl output_format="HTML">
<#include "../heroes/hero.ftl">
<#include "../heroes/hero-options.ftl">

<#macro imageSection section>
    <div class="${section.link?has_content?then('nhsd-m-image-with-link', '')} nhsd-!t-margin-bottom-6" data-uipath="ps.publication.image-section">
        <#assign link = section.link />

        <#if section.link?has_content && ! section.link?starts_with("http") >
          <#assign link = "http://" + section.link />
        </#if>

        <#if link?has_content && !section.caption?has_content>
            <a href="${link}" data-uipath="ps.publication.image-section.link" >
        </#if>

		<@hst.link var="imgName" hippobean=section.image/>
		
		<#if section.imageSize?contains("hero image")>
        	<#assign heroOptions = getHeroOptions(document) />
        	<#assign heroOptions += {
				"image": {"src": imgName, "alt": "${section.altText}"},
				"introText": "",
				"title": "",
				"summary": section.text,
				"inline": "yes",
				"colourBar": 1 == 0,
				"caption": section.caption
				}/>
			
			<#if section.imageSize?contains("left")>
				<#assign heroType = "accentedImageMirrored"/>
			<#else>
				<#assign heroType = "image"/>
			</#if>

        	<#if section.imageSize == 'Blue hero image'>
				<#assign heroOptions += {
					"colour": "Dark blue"
				}/>
        	<#elseif section.imageSize == 'Black hero image'>
				<#assign heroOptions += {
					"colour": "Black"
				}/>
        	</#if>

        	<@hero heroOptions heroType />
		<#else>
            <#assign figureStyle = "width: 100%;"/>
        	<#assign imageStyle = "width: 100%"/>

         	<#if section.imageSize == 'Half width inline'>
         		<#assign figureStyle = "width: 50%; float: left;"/>
         		<#assign imageStyle = "float: left;"/>
        	<#elseif section.imageSize == 'Full width'>
        		<#assign figureStyle = "position: relative; left: calc(-1 * (50vw - 58%)/2); width: 100vw;"/>
        	<#elseif section.imageSize == 'Right column' && (document.docType == "Feature" || document.docType == "Blog")>
				<div style = "position: relative; left: calc(-1 * (50vw - 50%)/2); width: 100vw;">
					<div style = "width: 50%; float: right; padding-right: 10px;">
        	<#elseif section.imageSize == 'Left column' && (document.docType == "Feature" || document.docType == "Blog")>
        		<div style = "position: relative; left: calc(-1 * (50vw - 50%)/2); width: 100vw;">
					<div style = "width: 50%; float: left;">
        	</#if>

            <figure class="nhsd-a-image nhsd-a-image--no-scale nhsd-a-image--round-corners nhsd-!t-margin-bottom-2"
            	aria-hidden="true"
            	style="${figureStyle}">
               	<img style="${imageStyle}" src="${imgName}" alt="${section.altText}" data-uipath="ps.publication.image-section.image"/>
	            <#if section.caption?has_content>
	            	<figcaption style="text-align:center">
		                <#if link?has_content>
		                    <div class="nhsd-t-heading-s">
		                        <a class="nhsd-a-link" href="${link}" data-uipath="ps.publication.image-section.link">${section.caption}</a>
		                    </div>
		                <#else>
		                    <div class="nhsd-t-heading-s">
		                        <span>${section.caption}</span>
		                    </div>
		                </#if>
		                <#if link?has_content && !section.caption?has_content>
				            </a>
				        </#if>
	                </figcaption>
	            </#if>
            </figure>
            
            <#if (section.imageSize == 'Right column' || section.imageSize == 'Left column') && (document.docType == "Feature" || document.docType == "Blog")>
		            </div>
		            <div style="padding-right: 10px;">${section.text}</div>
	            </div>
            </#if>
        </#if>
    </div>
    <div style="clear: both;"/> <#-- clear float so the next section does not wrap around the image -->
</#macro>
