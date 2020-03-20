<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/cyberAlertBox.ftl">
<#include "../macro/sections/quoteSection.ftl">

<#-- @ftlvariable name="wrappingDocument" type="uk.nhs.digital.website.beans.Calltoaction" -->

<div class="grid-row cyber-grid-row">
    <div class="column column--reset" id="${slugify(wrappingDocument.getTitle())}">
        <#if wrappingDocument??>
            <div class="cyber-header">
                <div class="cyber-header__group">
                    <h2 class="cyber-header__title">${wrappingDocument.getTitle()}</h2>
                </div>

                <#if wrappingDocument.internal?has_content>
                    <@hst.link var="link" hippobean=wrappingDocument.internal/>
                <#else>
                    <#assign link=wrappingDocument.external/>
                </#if>

                <#if wrappingDocument.getLabel()?has_content && link?has_content>
                <div class="cyber-header__cta ctabtn-right"
                     aria-labelledby="ctabtn-${slugify(wrappingDocument.getLabel())}">
                    <a href="${link}" class="ctabtn--nhs-digital-button"
                       id="ctabtn-${slugify(wrappingDocument.getLabel())}">${wrappingDocument.getLabel()}</a>
                </div>
                </#if>
            </div>
        </#if>

        <#if pageable?? && pageable.items?has_content>
            <div class="quote-box-list--grid">
                <#list pageable.items?chunk(2) as row>
                    <div class="quote-box-list--grid-row">
                        <#list row as caseStudy>
                            <#assign item = caseStudy />
                            <#assign item += {"quote": caseStudy.summary, "person":caseStudy.authorName, "role":caseStudy.authorRole, "organisation":caseStudy.authorOrganisation} />
                            <#assign item += {"grid": true} />
                            <#assign item += {"newStyle": true} />
                            <div class="quote-box__container quote-box--col-${row?size}">
                                <@quoteSection item/>
                            </div>
                        </#list>
                    </div>
                </#list>
            </div>
        </#if>
    </div>
</div>



