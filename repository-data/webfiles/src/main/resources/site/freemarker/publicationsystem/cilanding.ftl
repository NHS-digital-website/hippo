<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<@hst.setBundle basename="publicationsystem.headers"/>
<section class="document-content">
    <#if document??>
        <h1 data-uipath="ps.document.title">${document.title}</h1>
        <div class="layout layout--large">
            <div class="layout__item layout-2-3">
                <@hst.html hippohtml=document.content />
            </div><!--
        --><div class="layout__item layout-1-3">
                <div class="panel panel--grey push-half--bottom">
                    <h3><@fmt.message key="headers.ci-landing-actions"/></h3>
                    <ul>
                        <@hst.link var="backlink" mount="common-context" path="/" />
                        <li><a href="${backlink}" title="Back to Clinical Indicators">Back to Clinical Indicators</a></li>
                    </ul>
                </div>
            <#if document.resourceLinks?has_content>
                <div class="panel panel--grey push-half--bottom">
                    <h3><@fmt.message key="headers.ci-landing-resources"/></h3>
                    <ul data-uipath="ps.publication.resources">
                        <#list document.resourceLinks as link>
                            <li>
                                <a href="${link.linkUrl}" target="_blank">${link.linkText}</a>
                            </li>
                        </#list>
                    </ul>
                </div>
            </#if>
            </div>
        </div>
    </#if>
</section>
