<#ftl output_format="HTML">
<#include "../heroes/hero.ftl">
<#include "../heroes/hero-options.ftl">

<#macro imageModule section>
    <div class="nhsd-!t-margin-bottom-6" data-uipath="website.image-module">
		<@hst.link var="imgName" hippobean=section.image/>

		<#if section.imageType?contains("hero image")>
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
			
			<#if section.imageType?contains("left")>
				<#assign heroType = "accentedImageMirrored"/>
			<#else>
				<#assign heroType = "image"/>
			</#if>

        	<#if section.imageType?contains("Blue hero image")>
				<#assign heroOptions += {
					"colour": "Dark blue"
				}/>
        	<#elseif section.imageType?contains("Black hero image")>
				<#assign heroOptions += {
					"colour": "Black"
				}/>
        	</#if>

        	<@hero heroOptions heroType />
		<#else>
            <#assign figureStyle = "width: 100%;"/>
        	<#assign imageStyle = "width: 100%"/>

         	<#if section.imageType == 'Half width inline'>
         		<#assign figureStyle = "width: 50%; float: left;"/>
         		<#assign imageStyle = "float: left;"/>
        	<#elseif section.imageType == 'Full width'>
        		<#assign figureStyle = "position: relative; left: calc(-1 * (50vw - 58%)/3); width: 98vw;"/>
        	<#elseif section.imageType == 'Right column' && (document.docType == "Feature" || document.docType == "Blog")>
				<div style = "position: relative; left: calc(-1 * (50vw - 50%)/3); width: 98vw;">
					<div style = "width: 50%; float: right; padding-right: 10px;">
        	<#elseif section.imageType == 'Left column' && (document.docType == "Feature" || document.docType == "Blog")>
        		<div style = "position: relative; left: calc(-1 * (50vw - 50%)/3); width: 98vw;">
					<div style = "width: 50%; float: left;">
        	</#if>

            <figure class="nhsd-a-image nhsd-a-image--no-scale nhsd-a-image--round-corners nhsd-!t-margin-bottom-2"
            	aria-hidden="true"
            	style="${figureStyle}">
               	<img style="${imageStyle}" src="${imgName}" alt="${section.altText}" data-uipath="website.image-module.image"/>
	            <#if section.caption?has_content>
	            	<figcaption style="text-align:center">
	                    <div class="nhsd-t-heading-s">
	                        <span><@hst.html hippohtml=section.caption contentRewriter=brContentRewriter/></span>
	                    </div>
	                </figcaption>
	            </#if>
            </figure>
            
            <#if (section.imageType == 'Right column' || section.imageType == 'Left column') && (document.docType == "Feature" || document.docType == "Blog")>
		            </div>
		            <div style="padding-right: 10px;"><@hst.html hippohtml=section.text contentRewriter=brContentRewriter/></div>
	            </div>
            </#if>
        </#if>
    </div>
    <div style="clear: both;"/> <#-- clear float so the next section does not wrap around the image -->
</#macro>
