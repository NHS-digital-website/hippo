<#ftl output_format="HTML">

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<#macro visualhubBox link>

    <#if link.title??>
        <#assign title = link.title>
    </#if>
    <#if link.summary??>
        <#assign summary = link.summary>
    </#if>
    <#if link.linkType == "internal">
        <#assign title = link.link.title>
    <#elseif link.linkType == "external">
        <#assign summary = link.shortsummary>
    </#if>

    <article class="nhsd-m-card nhsd-m-card--with-icon">
        <#if link.linkType == "internal">
            <@hst.link hippobean=link.link var="internalLink"/>
        <#-- Below does not work if declared in section above -->
            <a href="${internalLink}" class="nhsd-a-box-link " aria-label="${title}"  onClick="logGoogleAnalyticsEvent('Link click','Card Link',${internalLink});">
        <#elseif link.linkType == "external">
            <a href="${link.link}" onKeyUp="return vjsu.onKeyUp(event)" class="nhsd-a-box-link " aria-label="${title}" onClick="logGoogleAnalyticsEvent('Link click','Card Link',${link.link});">
        <#elseif link.linkType == "asset">
                <@hst.link hippobean=link.link var="assestLink"/>
                <a href="${assestLink}" onKeyUp="return vjsu.onKeyUp(event)" class="nhsd-a-box-link " aria-label="${title}" onClick="logGoogleAnalyticsEvent('Link click','Card Link',${assestLink});">
        </#if>
            <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                <div class="nhsd-m-card__content_container">
                    <div class="nhsd-m-card__content-box">
                        <h1 class="nhsd-t-heading-s">${title}</h1>
                        <span class="nhsd-a-icon nhsd-a-icon--size-xl nhsd-a-icon--col-black nhsd-m-card__icon">
                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16">
                                <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="42%" height="42%" x="29%" y="29%"/>
                            </svg>
                            <#if (link.icon.original)??>
                                <@hst.link var="icon" hippobean=link.icon.original fullyQualified=true />
                                <#if icon?ends_with("svg")>
                                     <#if title?? && title?has_content>
                                        <img src="data:image/svg+xml;base64,${base64(colour(link.svgXmlFromRepository, "231f20"))}" alt="${title}" class="visual-hub-box-content-img" />
                                    <#else>
                                         <img src="data:image/svg+xml;base64,${base64(colour(link.svgXmlFromRepository, "231f20"))}" class="visual-hub-box-content-img" />
                                    </#if>
                                <#else>
                                    <img src="${icon}" alt="${title}" class="visual-hub-box-content-img" />
                                </#if>
                            </#if>
                        </span>
                        <p class="nhsd-t-body-s">${summary}</p>
                    </div>
                    <div class="nhsd-m-card__button-box">
                        <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-black">
                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                            </svg>
                        </span>
                    </div>
                </div>
            </div>
        </a>
    </article>

</#macro>
