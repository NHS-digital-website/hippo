<#ftl output_format="HTML">

<#-- @ftlvariable name="options" type="uk.nhs.digital.website.beans.SociaMediaBar" -->

<#macro socialMediaBar>

<#include "../../include/imports.ftl">

<#assign options = {
        "enabled": document.socialMediaBar.enable,
        "horizontal": document.socialMediaBar.direction,
        "hexagons": document.socialMediaBar.hexagons,
        "large": document.socialMediaBar.imageSize
    } />
    
	<#if options?has_content && options.enabled>
		<#assign iconSize = (options?has_content && options.large)?then("nhsd-t-heading-xl", "nhsd-t-heading-m")/>
		<#assign direction = (options?has_content && options.horizontal)?then("nhsd-t-row", "nhsd-t-grid nhsd-!t-margin-bottom-4 nhsd-!t-no-gutters")/>
		<#assign style = (options?has_content && options.horizontal)?then("", "text-align: left;margin: auto;width: 200px;")/>

		<div class="nhsd-t-grid nhsd-!t-padding-top-3 nhsd-!t-padding-bottom-3"">
			<div style="${style}">
				<p class="${iconSize}">Follow Us</p>
		 		<div class="${direction}">
					<@shareThisPage document "Twitter" twitterUrl twitterIconPath options/>
					<@shareThisPage document "Facebook" facebookUrl facebookIconPath options/>
					<@shareThisPage document "LinkedIn" linkedInUrl linkedInIconPath options/>
					<@shareThisPage document "YouTube" youTubeUrl youTubeIconPath options/>
				</div>
			</div>
		</div>
	</#if>
</#macro>