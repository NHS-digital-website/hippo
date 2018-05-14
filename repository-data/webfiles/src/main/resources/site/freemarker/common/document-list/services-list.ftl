<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="systems-and-services.subTitle" var="subTitle"/>
<@fmt.message key="systems-and-services.buttonLabel" var="buttonLabel"/>

<#-- @ftlvariable name="item" type="uk.nhs.digital.website.beans.Service" -->
<div class="content-box content-box--primary content-box--services">
    <div class="grid-row">
        <div class="column">
            <h3>${subTitle}</h3>
        </div>
    </div>
    
    <#if pageable?? && pageable.items?has_content>
    <div class="grid-row">
        <div class="column">
            <div class="cta-list cta-list--grid cta-list--fitted-grid">
                <#list pageable.items as item>
                <#if item?is_odd_item>
                <div class="grid-row">
                </#if>
                    <div class="column column--one-half ${item?is_odd_item?then("column--left", "column--right")}">
                        <article class="cta">
                            <h2 class="cta__title"><a href="<@hst.link hippobean=item/>">${item.title}</a></h2>
                            <p class="cta__text">${item.shortsummary}</p>
                        </article>
                    </div>
                <#if item?is_even_item || item?counter==pageable.items?size>
                </div>
                </#if>
                </#list>
            </div>
        </div>
    </div>
    </#if>
</div>
<div class="grid-row">
    <div class="column column--reset">
        <a href="/services" class="button">${buttonLabel}</a>
    </div>
</div>
