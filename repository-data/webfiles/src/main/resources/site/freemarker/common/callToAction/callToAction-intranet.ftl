<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Calltoaction" -->

<div class="grid-row">
    <div class="column column--reset" id="${slugify(document.getTitle())}">
        <div class="emphasis-box emphasis-box-important">
            <article class="emphasis-box__content">
                <p>${document.getContent()}</p>
            </article>
            <@hst.manageContent hippobean=document />
            <#if document.internal?has_content>
                <@hst.link var="link" hippobean=document.internal/>
            <#else>
                <#assign link=document.external/>
            </#if>

            <#-- note what does this bit do -->
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
