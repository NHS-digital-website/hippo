<#ftl output_format="HTML">
<#include "../../common/macro/component/actionLink.ftl">
<#include "../../common/macro/svgIcons.ftl">
<#include "../../include/imports.ftl">
<#-- @ftlvariable name="wrappingDocument" type="uk.nhs.digital.website.beans.Calltoaction" -->
<#-- @ftlvariable name="task" type="uk.nhs.digital.intranet.beans.Task" -->

<div class="nhsd-a-box nhsd-a-box--bg-light-grey nhsd-!t-padding-0">
    <#if wrappingDocument.title?? && wrappingDocument.title?has_content>
        <h2 class="nhsd-t-heading-s nhsd-!t-padding-top-2 nhsd-!t-padding-left-2"> ${wrappingDocument.title}</h2>
    </#if>

    <#if pageable?? && pageable.items?has_content>
        <div class="nhsd-o-image-with-link-list nhsd-!t-margin-0 nhsd-!t-padding-0">
            <div class="nhsd-t-grid nhsd-!t-margin-0 nhsd-!t-no-gutters">
                <div class="nhsd-t-row nhsd-o-image-with-link-list__items nhsd-t-row--centred nhsd-!t-margin-0 nhsd=!t-padding-0">

                    <#list pageable.items as task>
                        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-!t-margin-0 nhsd=!t-padding-0 nhsd-!t-no-gutters">
                            <#if task.bannercontrols??>
                                <@hst.link hippobean=task.bannercontrols.icon fullyQualified=true var="image" />
                            <#else>
                                <@hst.link path="/binaries/content/gallery/website/icons/process/arrow-right.svg" fullyQualified=true var="image" />
                            </#if>
                            <#assign imageUrl = '${image?replace("/binaries", "/svg-magic/binaries")}' />
                            <#assign imageHoverUrl =  imageUrl + "?colour=00000" />
                            <#assign imageUrl += "?colour=005EB8" />

                            <div class="nhsd-m-image-with-link nhsd-!t-margin-0 nhsd=!t-padding-0">
                                <figure class="nhsd-a-image nhsd-a-image--round-corners nhsd-!t-margin-bottom-1" aria-hidden="true">
                                    <span class="nhsd-a-icon nhsd-a-icon--size-xxl">
                                    <img src="${imageUrl}" alt="${task.title} icon" aria-hidden="true" focusable="false" />
                                    </span>
                                </figure>
                                <div class="nhsd-t-grid nhsd-!t-padding-left-2 nhsd-!t-padding-right-2">
                                    <a class="nhsd-t-body-s nhsd-a-link" href="<@hst.link hippobean=task/>">${task.title}</a>
                                </div>
                            </div>
                        </div>
                    </#list>
                    
                </div>
            </div>
        </div>
    </#if>

    <#if wrappingDocument.internal?has_content>
        <@hst.link var="link" hippobean=wrappingDocument.internal/>
    <#else>
        <#assign link=wrappingDocument.external/>
    </#if>

    <a class="nhsd-a-button nhsd-a-button--responsive nhsd-!t-margin-left-2" href="${link}" >
        <span class="nhsd-a-icon nhsd-a-icon--size-s">
            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
            </svg>
        </span>
        <span class="nhsd-a-button__label">${wrappingDocument.getLabel()}</span>
    </a>
</div>
