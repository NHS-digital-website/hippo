<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#-- @ftlvariable name="wrappingDocument" type="uk.nhs.digital.website.beans.Calltoaction" -->
<div class="grid-row cyber-grid-row">
    <div class="column column--reset" <#if wrappingDocument??>id="${slugify(wrappingDocument.getTitle())}"</#if>>
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
            <div class="section-demarcation">
                <div class="section-headings">
                    <h2>Useful information</h2>
                </div>
                <div class="home-document-grid-container">
                    <#list pageable.items?chunk(4) as row>
                        <#list row as item>
                            <article class="home-document-grid-item">
                                <h3>
                                    <a href="<@hst.link hippobean=item />">${item.title}</a>
                                </h3>
                            </article>
                        </#list>
                    </#list>
                </div>
            </div>
        </#if>
    </div>
</div>
