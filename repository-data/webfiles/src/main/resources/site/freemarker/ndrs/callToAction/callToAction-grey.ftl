<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Calltoaction" -->

<div class="grid-row cta-grid-row call-to-action call-to-action--grey"
     id="${slugify(document.getTitle())}">
    <div class="call-to-action__content">
        <h2 class="call-to-action__content-title">${document.getTitle()}</h2>
        <p class="call-to-action__content-text">
            ${document.getContent()}
        </p>
        <@hst.manageContent hippobean=document />
        <#if document.internal?has_content>
            <@hst.link var="link" hippobean=document.internal/>
        <#else>
            <#assign link=document.external/>
        </#if>
        <#if document.getLabel()?? && document.getLabel()?has_content && link?? && link?has_content>
            <div class="call-to-action-header__cta call-to-action-header__cta--btn-right">
                <a class="nhsd-a-button nhsd-a-button--invert" href="${link}">
                    <span class="nhsd-a-button__label">${document.getLabel()}</span>
                </a>
            </div>
        </#if>
    </div>

</div>
