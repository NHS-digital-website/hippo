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

    <article class="nhsd-m-card nhsd-m-card--full-height nhsd-m-card--with-icon">
        <#if link.linkType == "internal">
            <@hst.link hippobean=link.link var="internalLink"/>
        <#-- Below does not work if declared in section above -->
            <a href="${internalLink}" class="nhsd-a-box-link" aria-label="${title}">
        <#elseif link.linkType == "external">
            <a href="${link.link}" class="nhsd-a-box-link" aria-label="${title}">
        <#elseif link.linkType == "asset">
                <@hst.link hippobean=link.link var="assestLink"/>
                <a href="${assestLink}" class="nhsd-a-box-link" aria-label="${title}">
        </#if>
            <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                <div class="nhsd-m-card__content_container">
                    <div class="nhsd-m-card__content-box">
                        <h5 class="nhsd-t-heading-s">${title}</h5>
                        <p class="nhsd-t-body-s">${summary}</p>
                    </div>
                    <div class="nhsd-m-card__button-box">
                        <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-black">
                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                            </svg>
                        </span>
                    </div>
                    <#if (link.icon.original)??>
                        <div class="nhsd-m-card__icon-container">
                            <span class="nhsd-a-icon nhsd-a-icon--size-xxxl nhsd-a-icon--col-black nhsd-m-card__icon">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16">
                                    <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>

                                    <@hst.link var="icon" hippobean=link.icon.original fullyQualified=true />
                                    <#if icon?ends_with("svg")>
                                        <#if title?? && title?has_content>
                                            <image x="0" y="0" width="100%" height="100%" href="data:image/svg+xml;base64,${base64(colour(link.svgXmlFromRepository, "231f20"))}" />
                                        <#else>
                                            <image x="0" y="0" width="100%" height="100%" href="data:image/svg+xml;base64,${base64(colour(link.svgXmlFromRepository, "231f20"))}" />
                                        </#if>
                                    <#else>
                                        <image x="4" y="4" width="8" height="8" href="${icon}" />
                                    </#if>
                                </svg>
                            </span>
                        </div>
                    </#if>
                </div>
            </div>
        </a>
    </article>

</#macro>
