<#ftl output_format="HTML">

<#include "../../include/imports.ftl">
<#include "../../common/macro/svgMacro.ftl">

<#assign getRemoteSvg="uk.nhs.digital.freemarker.svg.SvgFromUrl"?new() />

<@hst.setBundle basename="rb.generic.intranet.headers"/>

<div class="nhsd-t-grid nhsd-t-grid--nested">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-12">
            <#if wrappingDocument?? && wrappingDocument.title?? && wrappingDocument.title?has_content>
                <#assign title>${wrappingDocument.title}</#assign>
            <#else>
                <#assign title><@fmt.message key="headers.tasks" /></#assign>
            </#if>
            <h2 class="nhsd-t-heading-m">${title}</h2>
        </div>
    </div>
    <#if pageable?? && pageable.items?has_content>
        <#list pageable.items as task>
            <#if task?index % 2 == 0>
                <div class="nhsd-t-row">
            </#if>
            <div class="nhsd-t-col-s-6 nhsd-!t-margin-bottom-6">
                <div class="nhsd-m-card nhsd-m-card--full-height">
                    <a href="<@hst.link hippobean=task />" class="nhsd-a-box-link nhsd-a-box-link--focus" aria-label="${task.title}" >
                        <div class="nhsd-a-box nhsd-a-box--bg-dark-green">
                            <div class="nhsd-m-card__content_container">
                                <div class="nhsd-m-card__content-box">
                                    <div style="display: flex; justify-content: space-between">
                                        <h2 class="nhsd-t-heading-xs">${task.title}</h2>
                                        <span class="nhsd-a-icon nhsd-a-icon--size-m nhsd-a-icon--col-white">
                                            <#if task.icon == "other" >
                                                <@hst.link hippobean=task.linkIcon var="iconLink" />
                                                <#if iconLink?contains(".svg")>
                                                    <@svgWithoutAltText svgString=task.svgXmlFromRepository/>
                                                <#else>
                                                    <img style="width: auto" src="${iconLink}">
                                                </#if>
                                            <#else>
                                                <#assign svg = getRemoteSvg(task.icon) />
                                                ${svg.data?replace('<svg ', '<svg style="width: auto" ')?no_esc}
                                            </#if>
                                        </span>
                                    </div>
                                </div>
                                <div class="nhsd-m-card__button-box"><span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-white">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/></svg>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
            </div>
            <#if task?index % 2 == 1 || task?is_last>
                </div>
            </#if>
        </#list>
        <#if wrappingDocument?? && wrappingDocument.title?? && wrappingDocument.title?has_content>
            <div class="nhsd-t-row nhsd-!t-margin-top-4 nhsd-!t-margin-bottom-6">
                <div class="nhsd-t-col-12">
                    <@hst.link var="link" hippobean=wrappingDocument/>
                    <a class="nhsd-a-button nhsd-a-button--outline" href="${link}">
                        <span class="nhsd-a-button__label">${wrappingDocument.title}</span>
                    </a>
                </div>
            </div>
        </#if>
    </#if>
</div>