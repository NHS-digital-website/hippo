<#ftl output_format="HTML">

<#include "../../../include/imports.ftl">
<#include "../documentIcon.ftl">

<#function sizeToDisplay size=0>
  <#assign sizeString = "${size} bytes" />
  <#if size gt 1000000>
    <#assign sizemb = size/1000000 />
    <#assign sizeString = "${sizemb?round} MB" />
  <#elseif size gt 1000>
    <#assign sizekb = size/1000 />
    <#assign sizeString = "${sizekb?round} KB" />
  </#if>
  <#return sizeString>
</#function>

<#macro downloadBlockAsset classname resource title shortsummary mimeType size external=false small=false prompt=false>
    </br>Cehck this downloadBlockAsset --> ${prompt?c}
    <#if size?has_content>
        <#assign sizeString = sizeToDisplay(size) />
    </#if>
    <#assign iconTypeFromMime = getFormatByMimeType("${mimeType?lower_case}") />

    <@externalstorageLink resource; url>
        <div class="${(small == true)?then('nhsd-m-download-card', 'nhsd-m-download-card nhsd-!t-margin-bottom-6')}">
            <#if prompt>
            </br>  This prompt --> ${prompt?c}
            <a href="${(external == true)?then(resource, url)}"
               class="nhsd-a-box-link"
               onClick="${getOnClickMethodCall(classname, (external == true)?then(resource, url), true)}"
               onKeyUp="return vjsu.onKeyUp(event)" data-modal-open="ods-modal">
                <#else >
                <a href="${(external == true)?then(resource, url)}"
                   class="nhsd-a-box-link"
                   onClick="${getOnClickMethodCall(classname, (external == true)?then(resource, url), true)}"
                   onKeyUp="return vjsu.onKeyUp(event)">
                    </#if>
                <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                    <div class="${(small == true)?then('nhsd-m-download-card__image-box small', 'nhsd-m-download-card__image-box')}">
                        <#-- macro to get the svg accepts type and size but size defaults to medium which is what we want -->
                        <#if small>
                            <@documentIcon "${iconTypeFromMime}" "extra-small" />
                        <#else>
                            <@documentIcon "${iconTypeFromMime}"/>
                        </#if>
                    </div>

                    <div class="${(small == true)?then('nhsd-m-download-card__content-box small', 'nhsd-m-download-card__content-box')}">

                        <#if title?has_content>
                        <p class="${(small == true)?then('nhsd-t-heading-xs nhsd-!t-margin-bottom-2', 'nhsd-t-heading-s')}">${title}</p>
                        </#if>

                        <div class="nhsd-m-download-card__meta-tags">
                            <#assign fileFormat = ""/>
                            <@hst.link hippobean=resource var="filename" />
                            <#if filename != "" >
                                <#assign fileFormat = getFileExtension(filename?lower_case) />
                            </#if>
                            <#if external == true && fileFormat == "">
                                <#assign fileFormat = getFileExtension(resource?lower_case) />
                            </#if>
                            <#if fileFormat == "">
                                <#assign fileFormat = iconTypeFromMime />
                            </#if>

                            <span class="nhsd-a-tag nhsd-a-tag--meta">${fileFormat}</span>

                            <#if sizeString?has_content && external == false>
                                <span class="nhsd-a-tag nhsd-a-tag--meta-light">${sizeString}</span>
                            </#if>
                        </div>

                        <#if shortsummary?has_content>
                            <p class="nhsd-t-body nhsd-!t-margin-top-2">${shortsummary}</p>
                        </#if>

                        <#if small == false>
                        <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-arrow--down nhsd-a-icon--size-s">
                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                <path d="M15,8.5L8,15L1,8.5L2.5,7L7,11.2L7,1l2,0l0,10.2L13.5,7L15,8.5z"/>
                            </svg>
                        </span>
                        </#if>
                    </div>

                    <#if small>
                    <div class="nhsd-m-download-card__arrow-icon-box">
                        <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-arrow--down nhsd-a-icon--size-xs">
                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                <path d="M15,8.5L8,15L1,8.5L2.5,7L7,11.2L7,1l2,0l0,10.2L13.5,7L15,8.5z"/>
                            </svg>
                        </span>
                    </div>
                    </#if>
                </div>
            </a>
        </div>
    </@externalstorageLink>
    <div class="nhsd-m-modal nhsd-m-modal--close" id="ods-modal">
        <div role="dialog" class="nhsd-m-modal__container"
             aria-labelledby="default_dialog_label" aria-modal="true">
            <div class="nhsd-a-box nhsd-!t-padding-3">
                <h1 id="default_dialog_label" class="nhsd-t-heading-xs">
                    ${modalHeader}
                </h1>
                <p class="nhsd-t-body-s">${modalIntro}.</p>
                <p class="nhsd-t-body-s">
                    <input id="data1" list="data">
                    <datalist id="data">
                    </datalist>
                </p>
                <nav class="nhsd-m-button-nav nhsd-m-button-nav--condensed">
                    <button class="nhsd-a-button nhsd-!t-margin-bottom-0"
                            type="button" data-modal-close onclick="${(external == true)?then(resource, url)}">
                        <span class="nhsd-a-button__label">${confirmButton}</span>
                    </button>
                </nav>
                <a>${declineText}</a></br>
                <a>${orgNotListed}</a>
            </div>
        </div>
    </div>
</#macro>
