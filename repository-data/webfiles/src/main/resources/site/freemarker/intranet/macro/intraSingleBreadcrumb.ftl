<#ftl output_format="HTML">

<#macro intraSingleBreadcrumb title>
<div class="nhsd-t-grid">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col">

        <nav class="nhsd-m-breadcrumbs" aria-label="Breadcrumb">
            <ol class="nhsd-m-breadcrumbs__list nhsd-!t-padding-top-1 nhsd-!t-padding-bottom-1">
                <li class="nhsd-m-breadcrumbs__item">
                    <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="<@hst.link siteMapItemRefId='root'/>">NHS Digital</a>
                </li>

                    <#if title?has_content>
                    <li class="nhsd-m-breadcrumbs__item">
                        <span class="nhsd-a-icon nhsd-a-icon--size-xxs nhsd-a-icon--col-dark-grey">
                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                <path d="M12,8l-6.5,7L4,13.5L9.2,8L4,2.5L5.5,1L12,8z"/>
                            </svg>
                        </span>
                        <span class="nhsd-t-body-s" aria-current="page">${title}</span>
                    </li>
                    </#if>
                </ol>
            </nav>
        </div>
    </div>
</div>
<hr class="nhsd-a-horizontal-rule nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-1" />
</#macro>
