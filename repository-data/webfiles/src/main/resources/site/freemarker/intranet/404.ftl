<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<#assign document = { "title": documentTitle } />
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.intra.404-page"/>

<article class="nhsd-t-grid">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col">
            <h1 data-uipath="ps.document.title" class="nhsd-t-heading-l"><@fmt.message key="page.title" /></h1>
        </div>
    </div>

    <div class="nhsd-t-row">
        <div class="nhsd-t-col">
            <h2 class="nhsd-t-heading-m"><@fmt.message key="404.error.code" /></h2>

            <p class="nhsd-t-body"><@fmt.message key="404.error.message" /></p>
        </div>
    </div>
    <div class="nhsd-t-row">
        <div class="nhsd-t-col">
            <p class="nhsd-!t-font-weight-bold"><@fmt.message key="404.try.search.text" /></p>

            <#include "./include/search-strip.ftl">
        </div>
    </div>

    <@hst.setBundle basename="rb.intra.404-page"/>
    <div class="nhsd-t-row">
        <div class="nhsd-t-col nhsd-!t-margin-top-6">
            <p class="nhsd-t-body"><@fmt.message key="contact.text" /></p>
            <dl class="nhsd-a-summary-list">
                <@fmt.message key="contact.telephone" var="contactTelephone" />
                <#if contactTelephone?has_content>
                    <div class="nhsd-a-summary-list__item">
                        <dt>Telephone</dt>
                        <dd><span class="nhsd-a-summary-list__text"><a href="tel:${contactTelephone}">${contactTelephone}</a></span></dd>
                    </div>
                </#if>

                <@fmt.message key="contact.email" var="contactEmail" />
                <#if contactEmail?has_content>
                    <div class="nhsd-a-summary-list__item">
                        <dt>Email</dt>
                        <dd><span class="nhsd-a-summary-list__text"><a href="mailto:${contactEmail}">${contactEmail}</a></span></dd>
                    </div>
                </#if>
            </dl>
        </div>
    </div>
</article>
