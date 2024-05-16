<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/iconGenerator.ftl">

<@hst.setBundle basename="publicationsystem.breadcrumbs"/>

<#if ciBreadcrumb?? && ciBreadcrumb.items?size gte 1>
    <#assign breadcrumb = ciBreadcrumb/>
</#if>
<#assign count = 1>
<#if breadcrumb?? && breadcrumb.items?size gte 1>
<div class="nhsd-t-grid nhsd-!t-padding-top-3 nhsd-!t-padding-bottom-3">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col">
            <nav class="nhsd-m-breadcrumbs" aria-label="Breadcrumbs">
                <ol class="nhsd-m-breadcrumbs__list" data-uipath="document.breadcrumbs" itemscope itemtype="https://schema.org/BreadcrumbList">
                    <li class="nhsd-m-breadcrumbs__item" itemprop="itemListElement" itemscope itemtype="https://schema.org/ListItem">
                        <meta itemprop="position" content="${count}" />
                        <#assign count++>
                        <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="<@hst.link siteMapItemRefId='root'/>" itemprop="item">
                            <span itemprop="name">NHS Digital</span>
                        </a>
                        <@buildInlineSvg "chevron-right" "xxs", "nhsd-a-icon--col-dark-grey" />
                    </li>
                    <#if breadcrumb.clinicalIndicator?has_content && breadcrumb.clinicalIndicator>
                        <li class="nhsd-m-breadcrumbs__item" itemprop="itemListElement" itemscope itemtype="https://schema.org/ListItem">
                            <meta itemprop="position" content="${count}" />
                            <#assign count++>
                            <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="${cilink}" itemprop="item">
                                <span itemprop="name">Data and information</span>
                            </a>
                            <@buildInlineSvg "chevron-right" "xxs", "nhsd-a-icon--col-dark-grey" />
                        </li>
                    <#elseif isStatisticalPublication?? && isStatisticalPublication>
                        <li class="nhsd-m-breadcrumbs__item" itemprop="itemListElement" itemscope itemtype="https://schema.org/ListItem">
                            <meta itemprop="position" content="${count}" />
                            <#assign count++>
                            <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="<@hst.link siteMapItemRefId='data'/>" itemprop="item">
                                <span itemprop="name">Data</span>
                            </a>
                            <@buildInlineSvg "chevron-right" "xxs", "nhsd-a-icon--col-dark-grey" />
                        </li>
                        <li class="nhsd-m-breadcrumbs__item" itemprop="itemListElement" itemscope itemtype="https://schema.org/ListItem">
                            <meta itemprop="position" content="${count}" />
                            <#assign count++>
                            <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="<@fmt.message key="breadcrumbs.publicationLink"/>" itemprop="item">
                                <span itemprop="name">Publications</span>
                            </a>
                            <@buildInlineSvg "chevron-right" "xxs", "nhsd-a-icon--col-dark-grey" />
                        </li>
                    </#if>
                    ${hstRequestContext.setAttribute("bread",breadcrumb.items[0].title)}
                    <#list breadcrumb.items as item>
                        <li class="nhsd-m-breadcrumbs__item" itemprop="itemListElement" itemscope itemtype="https://schema.org/ListItem">
                            <#if !item?is_last>
                                <@hst.link var="link" link=item.link/>
                                <a class="nhsd-a-link nhsd-a-link nhsd-a-link--col-dark-grey" href="${link}" data-text="${item.title}" itemprop="item">${item.title}</a>
                                <meta itemprop="name" content="${item.title}" />
                                <meta itemprop="position" content="${count}" />
                                <#assign count++>
                                <@buildInlineSvg "chevron-right" "xxs", "nhsd-a-icon--col-dark-grey" />
                            <#else>
                                <meta itemprop="position" content="${count}" />
                                <#assign count++>
                                <span class="nhsd-t-body-s" aria-current="page" data-text="${item.title}">
                                    <span itemprop="name">${item.title}</span>
                                </span>
                            </#if>
                        </li>
                    </#list>
                </ol>
            </nav>
        </div>
    </div>
</div>
</#if>
