<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Calltoaction" -->

<div class="grid-row cyber-grid-row">
    <div class="column column--reset" id="${slugify(document.getTitle())}">
        <div class="cta--intra">
            <article class="cta-body">
                <p class="cta-text">${document.getContent()}</p>
            </article>
            <@hst.manageContent hippobean=document />
            <#if document.internal?has_content>
                <@hst.link var="link" hippobean=document.internal/>
            <#else>
                <#assign link=document.external/>
            </#if>
            <article class="cta--intra">
                <#if document.getLabel()?? && document.getLabel()?has_content && link?? && link?has_content>
                    <div class="cyber-header__cta ctabtn-right"
                         aria-labelledby="ctabtn-${slugify(document.getLabel())}">
                        <a href="${link}"
                           class="ctabtn--nhs-digital-button"
                           id="ctabtn-${slugify(document.getLabel())}">${document.getLabel()}</a>
                    </div>
                </#if>
            </article>
        </div>

    </div>
</div>
