<#ftl output_format="HTML">
<#include "./iconGenerator.ftl">

<#macro pubsBreadcrumb title>
    <div class="nhsd-t-grid nhsd-!t-padding-top-3 nhsd-!t-padding-bottom-3">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col">
                <nav class="nhsd-m-breadcrumbs" aria-label="Breadcrumbs">
                    <ol class="nhsd-m-breadcrumbs__list" data-uipath="document.breadcrumbs">
                        <li class="nhsd-m-breadcrumbs__item">
                            <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="<@hst.link siteMapItemRefId='root'/>">NDRS</a>
                            <@buildInlineSvg "chevron-right" "xxs", "nhsd-a-icon--col-dark-grey" />
                        </li>
                        <#if title?has_content>
                            <li class="nhsd-m-breadcrumbs__item">
                                <span class="nhsd-t-body-s" aria-current="page" data-text="${title}">${title}</span>
                            </li>
                        </#if>
                    </ol>
                </nav>
            </div>
        </div>
    </div>
</#macro>
