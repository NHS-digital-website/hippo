<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "./shareThisPage.ftl">

<#macro socialMediaBar options followMe=0>
	<#if options.bar?exists && options.bar.enable>
		<#assign youTubeIconPath = "/images/icon/rebrand-youtube.svg" />
		<#assign twitterIconPath = "/images/icon/rebrand-twitter.svg" />
		<#assign linkedInIconPath = "/images/icon/rebrand-linkedin.svg" />
		<#assign facebookIconPath = "/images/icon/rebrand-facebook.svg" />
		<#assign socialMediaIconPath = "/images/icon/socialmedia.svg" />

		<#if followMe == 0>
			<#assign title = "Follow us"/>
			<#--  Facebook  -->
			<#assign facebookUrl = "http://www.facebook.com/sharer.php?u=${getDocumentUrl()?url}"/>
			<#--  LinkedIn  -->
			<#assign linkedInUrl = "http://www.linkedin.com/shareArticle?mini=true&url=${getDocumentUrl()?url}&title=${options.title?url}&summary=${options.shortsummary?url}"/>
			<#--  Twitter -->
            <#assign hashtags ='' />
            <#if options.twitterHashtag?has_content>
                <#list options.twitterHashtag as tag>
                    <#assign hashtags = hashtags + tag?starts_with("#")?then(tag?keep_after('#'), tag) + tag?is_last?then('', ',')>
                </#list>
            </#if>
            <#assign twitterUrl = "https://twitter.com/intent/tweet?via=nhsdigital&url=${getDocumentUrl()?url}&text=${options.title?url}&hashtags=${hashtags?url}"/>
            <#-- YouTube -->
			<#assign youTubeUrl = "https://studio.youtube.com/channel/?via=nhsdigital&url=${getDocumentUrl()?url}&text=${options.title?url}&hashtags=${hashtags?url}"/>
		<#else>
			<#assign title = "Follow me"/>
			<#--  LinkedIn  -->
			<#assign linkedInUrl = options.socialmedias.linkedinlink />
			<#--  Twitter -->
			<#assign hashtags ='' />
			<#--
			This 'document.twitterHashtag' cannot be a primative array!
			-->
			<#if options.twitterHashtag?has_content>
			    <#list options.twitterHashtag as tag>
			        <#assign hashtags = hashtags + tag?starts_with("#")?then(tag?keep_after('#'), tag) + tag?is_last?then('', ',')>
			    </#list>
			</#if>
			<#assign twitterUrl = "https://twitter.com/${options.socialmedias.twitteruser}"/>
		</#if>

		<#assign iconSize = (options.bar?has_content && options.bar.iconSize)?then("nhsd-t-heading-xl", "nhsd-t-heading-s")/>
		<#assign direction = (options.bar?has_content && options.bar.direction)?then("nhsd-t-row", "nhsd-t-grid nhsd-!t-margin-bottom-4 nhsd-!t-no-gutters")/>
		<#assign style = (options.bar?has_content && options.bar.direction)?then("", "text-align: left;margin: auto;width: 200px;")/>

		<div class="nhsd-t-grid nhsd-!t-padding-top-3 nhsd-!t-padding-bottom-3 nhsd-!t-padding-left-0">
			<div style="${style}">
				<p class="${iconSize}">${title}</p>
		 		<div class="${direction}">
		 			<#if twitterUrl?has_content>
						<@shareThisPage "Twitter" twitterUrl twitterIconPath options.bar/>
					</#if>
					<#if linkedInUrl?has_content>
						<@shareThisPage "LinkedIn" linkedInUrl linkedInIconPath options.bar/>
					</#if>
					<#if facebookUrl?has_content>
						<@shareThisPage "Facebook" facebookUrl facebookIconPath options.bar/>
					</#if>

					<#if options.socialmedias?has_content && options.socialmedias.othersocialmedias?has_content>
						<#list options.socialmedias.othersocialmedias as othersm>
							<#if othersm.link?lower_case?contains("facebook")>
								<#assign iconPath = facebookIconPath/>
							<#elseif othersm.link?lower_case?contains("youtube")>
								<#assign iconPath = youTubeIconPath/>
							<#else>
								<#assign iconPath = socialMediaIconPath/>
							</#if>
							<@shareThisPage othersm.title othersm.link iconPath options.bar/>
						</#list>
					</#if>
				</div>
			</div>
		</div>
	</#if>
</#macro>