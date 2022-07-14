<#ftl output_format="HTML">

<#macro shareThisPage mediaTitle="" mediaLink="" iconPath="" smOptions={}>
<#assign divStyle = (!smOptions?has_content || (smOptions?has_content && smOptions.direction))?then("style='float:left;'", "class='nhsd-t-row'")/>
<#assign iconSize = (!smOptions?has_content || (smOptions?has_content && smOptions.iconSize))?then("nhsd-a-icon--size-xxl", "nhsd-a-icon--size-l")/>

<div ${divStyle}>
    <div class="nhsd-t-col-12 nhsd-!t-no-gutters">
        <a class="nhsd-a-icon-link nhsd-a-icon-link--dark-grey" href="${mediaLink}" rel="external" onClick="logGoogleAnalyticsEvent('Link click','Social media - ${mediaTitle}','${mediaLink}');">
            <span class="nhsd-a-icon ${iconSize}">
            	<#if !smOptions?has_content || (smOptions?has_content && smOptions.hexagons) >
	                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16">
	                    <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>
	                </svg>
                </#if>
                <img src="<@hst.webfile path="${iconPath}"/>" alt="Share on ${mediaTitle}" aria-hidden="true">
            </span>
            <span class="nhsd-a-icon-link__label">${mediaTitle}</span>
            <#if srOnlyLinkText?has_content>
                <span class="nhsd-t-sr-only">${srOnlyLinkText}</span>
            </#if>
        </a>
    </div>
</div>
</#macro>