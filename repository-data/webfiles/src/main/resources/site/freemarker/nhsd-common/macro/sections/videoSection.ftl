<#ftl output_format="HTML">

<#include "../../../include/imports.ftl">
<#include "../heroes/hero-options.ftl"/>
<#include "../heroes/hero.ftl">

<#macro videoSection section>

<#if section.videoUrl?contains("www.youtube") || section.videoUrl?contains("youtu.be") || section.videoUrl?contains(".vimeo.com")>
    <#assign mute = section.videoUrl?contains(".vimeo.com")?then("muted", "mute")/>
    <#if hst.isBeanType(section.caption, 'org.hippoecm.hst.content.beans.standard.HippoHtml')>
        <@hst.html var="htmlCaption" hippohtml=section.caption contentRewriter=brContentRewriter />
    <#else>
        <#assign htmlCaption = section.caption/>
    </#if>

	<#if document.docType?has_content && (document.docType == "Feature" || document.docType == "Blog") && (section.type == "Hero video right" || section.type == "Hero video left")>
        <#assign videoUrl = section.videoUrl />
		<#assign videoUrl += "${section.videoUrl?contains('?')?then('&', '?')}autoplay=${section.behaviour?then('1','0')}" />
        <#assign videoUrl += "&loop=${section.loop?then('1','0')}" />
        <#assign videoUrl += "&playlist=${section.playlist}" />
        <#assign videoUrl += "&${mute}=${section.behaviour?then('1','0')}" />

        <#assign heroOptions = getHeroOptions(document) />
        <#assign heroOptions += {
            "video": {
                "src": videoUrl,
                "caption": htmlCaption?has_content?then(htmlCaption?no_esc,'')
            },
			"introText": "",
			"title": "",
			"summary": section.text,
			"colour": "darkBlue"
        }/>

		<#if section.type == "Hero video left">
			<#assign heroType = "imageMirrored"/>
		<#else>
			<#assign heroType = "image"/>
		</#if>

        </div></div></div>
		<@hero heroOptions heroType/>
        <div class="nhsd-t-grid"><div class="nhsd-t-row"><div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">
	<#else>
		<#assign fullWidth = (document.docType == "Feature" || document.docType == "Blog") && section.type == "Full width"/>
		<#if fullWidth>
			</div></div></div>
		</#if>

		<#assign videoOptions = "autoplay=${section.behaviour?then('1','0')}" +
				"&loop=${section.loop?then('1','0')}" +
				"&playlist=${section.playlist}" +
				"&${mute}=${section.behaviour?then('1','0')}"/>

        <div class="nhsd-t-ratio-16x9">
            <figure>
                <iframe src="${section.videoUrl}&autoplay=${section.behaviour?then('1','0')}&loop=${section.loop?then('1','0')}&${mute}=${section.behaviour?then('1','0')}&rel=0"
                    title="${document.title}"
                    allow="autoplay"></iframe>
                <link itemprop="embedUrl" href="${section.videoUrl}?${videoOptions}" />
                <#if htmlCaption?has_content>
                    <figcaption>${htmlCaption?no_esc}</figcaption>
                </#if>
            </figure>
	    </div>

        <#if fullWidth>
            <div class="nhsd-t-grid"><div class="nhsd-t-row"><div class="nhsd-t-col-xs-12 nhsd-t-col-s-8 nhsd-t-off-s-2 nhsd-t-col-m-8 nhsd-t-off-m-2 nhsd-t-col-l-8 nhsd-t-off-l-2 nhsd-t-col-xl-6 nhsd-t-off-xl-3">
        </#if>
	</#if>
</#if>
</#macro>