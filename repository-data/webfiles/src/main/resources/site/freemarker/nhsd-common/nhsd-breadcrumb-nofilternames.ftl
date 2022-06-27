<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/iconGenerator.ftl">

<@hst.setBundle basename="publicationsystem.breadcrumbs"/>

<#if ciBreadcrumb?? && ciBreadcrumb.items?size gte 1>
    <#assign breadcrumb = ciBreadcrumb/>
</#if>

<#assign filterNames = hstRequest.requestContext.resolvedSiteMapItem.hstComponentConfiguration.children["hst:pages/cyberalerthub/breadcrumb"].localParameters["filterValues"]?split(",")/>

<#if breadcrumb?? && breadcrumb.items?size gte 1>
<div class="nhsd-t-grid nhsd-!t-padding-top-3 nhsd-!t-padding-bottom-3">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col">
            <nav class="nhsd-m-breadcrumbs" aria-label="Breadcrumbs">
                <ol class="nhsd-m-breadcrumbs__list" data-uipath="document.breadcrumbs">
                    <li class="nhsd-m-breadcrumbs__item">
                        <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="<@hst.link siteMapItemRefId='root'/>">NHS Digital</a>
                        <@buildInlineSvg "chevron-right" "xxs", "nhsd-a-icon--col-dark-grey" />
                    </li>
                    <#if breadcrumb.clinicalIndicator?? && breadcrumb.clinicalIndicator>
                        <li class="nhsd-m-breadcrumbs__item">
                            <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="${cilink}">Data and information</a>
                            <@buildInlineSvg "chevron-right" "xxs", "nhsd-a-icon--col-dark-grey" />
                        </li>
                    <#elseif isStatisticalPublication?? && isStatisticalPublication>
                        <li class="nhsd-m-breadcrumbs__item">
                            <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="<@hst.link siteMapItemRefId='data'/>">Data</a>
                            <@buildInlineSvg "chevron-right" "xxs", "nhsd-a-icon--col-dark-grey" />
                        </li>
                        <li class="nhsd-m-breadcrumbs__item">
                            <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="<@fmt.message key="breadcrumbs.publicationLink"/>">Publications</a>
                            <@buildInlineSvg "chevron-right" "xxs", "nhsd-a-icon--col-dark-grey" />
                        </li>
                    </#if>
                    ${hstRequestContext.setAttribute("bread",breadcrumb.items[0].title)}
                    <#list breadcrumb.items as item>
                        <#if !filterNames?seq_contains(item.title)>
                            <li class="nhsd-m-breadcrumbs__item">
                                <#if !item?is_last>
                                    <@hst.link var="link" link=item.link/>
                                    <a class="nhsd-a-link nhsd-a-link nhsd-a-link--col-dark-grey" href="${link}" data-text="${item.title}">${item.title}</a>
                                    <@buildInlineSvg "chevron-right" "xxs", "nhsd-a-icon--col-dark-grey" />
                                <#else>
                                    <span class="nhsd-t-body-s" aria-current="page" data-text="${item.title}">${item.title}</span>
                                </#if>
                            </li>
                        </#if>
                    </#list>
                </ol>
            </nav>
        </div>
    </div>
</div>
</#if>
