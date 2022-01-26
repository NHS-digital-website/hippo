<#ftl output_format="HTML">

<#macro videoSection section>

<#if section.videoUrl?contains("www.youtube") || section.videoUrl?contains("youtu.be") || section.videoUrl?contains(".vimeo.com")>
    <#assign mute = section.videoUrl?contains(".vimeo.com")?then("muted", "mute")/>
    <#if hst.isBeanType(section.caption, 'org.hippoecm.hst.content.beans.standard.HippoHtml')>
        <@hst.html var="htmlCaption" hippohtml=section.caption contentRewriter=brContentRewriter />
    <#else>
        <#assign htmlCaption = section.caption/>
    </#if>
	<#if (document.docType == "Feature" || document.docType == "Blog") && (section.type == "Hero video right" || section.type == "Hero video left")>
		<#assign heroOptions = getHeroOptions(document) />
		<#assign heroOptions += {
			"colour": "Dark blue",
			"video": section.videoUrl,
			"introText": "",
			"title": "",
			"summary": section.text,
			"videoOptions": {
				"caption": "${htmlCaption?no_esc}",
				"autoplay": "${section.behaviour?then('1','0')}",
				"loop": "${section.loop?then('1','0')}",
				"playlist": "${section.playlist}",	<#-- need a playlist for loop to work -->
				"${mute}": "${section.behaviour?then('1','0')}" <#-- need to mute audio for autoplay to work -->
			},
			"inline": "yes",	<#-- make hero break out of its bootstrap container (new option) -->
			"colourBar": 1 == 0	<#-- make hero think we dont have a colour bar (existing option) -->
			}/>

		<#if section.type == "Hero video left">
			<#assign heroType = "accentedImageMirrored"/>
		<#else>
			<#assign heroType = "image"/>
		</#if>

		<@hero heroOptions heroType/>
	<#else>
		<#if (document.docType == "Feature" || document.docType == "Blog") && section.type == "Full width">
			<#assign fullWidth="position: relative; left: calc(-1 * (50vw - 50%)/2); width: 100vw; height: 100vh"/>
        <#else>
            <#assign fullWidth=""/>
		</#if>

		<figure style="position: relative; width: 50vw; height: 50vh; text-align: center; ${fullWidth}">
			<iframe src="${section.videoUrl}&autoplay=${section.behaviour?then('1','0')}&loop=${section.loop?then('1','0')}&${mute}=${section.behaviour?then('1','0')}&rel=0"
				title="${document.title}"
				allow="autoplay"
				style="position: absolute; top: 0; height: 90%; width: 100%; display: block; border: none;"></iframe>
	    	<link itemprop="embedUrl" href="${section.videoUrl}" />
	    	<#if section.caption?has_content>
	    		<figcaption style="position: absolute; bottom: 0; height: 10%; width: 100%; text-align: center;">${htmlCaption?no_esc}</figcaption>
			</#if>
	    </figure>
	</#if>
</#if>
</#macro>