<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#assign dateFormat="dd/MM/yyyy"/>
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />
<#assign formatCoverageDates="uk.nhs.digital.ps.directives.CoverageDatesFormatterDirective"?new() />

<#macro publicationHeader publication>

    <div class="local-header article-header">

        <#if publication.informationType?has_content>
            <#list publication.informationType as type>
                <#if type == "National statistics">
                    <div class="local-header__national-statistics" data-uipath="ps.publication.national-statistics" title="National statistics"></div>
                    <#break>
                </#if>
            </#list>
        </#if>

        <h4 class="local-header__subtitle"><@fmt.message key="labels.publication"/></h4>

        <h1 data-uipath="document.title" class="local-header__title">${publication.title}</h1>

        <#if publication.parentDocument??>
            <h3 class="local-header__subtitle">
                This is part of
                <a href="<@hst.link hippobean=publication.parentDocument.selfLinkBean/>"
                    title="${publication.parentDocument.title}">
                    ${publication.parentDocument.title}
                </a>
            </h3>
        </#if>

        <div class="local-header__intro"></div>

        <table class="local-header__table">

            <@nominalPublicationDate publication/>

            <#if publication.informationType?has_content>
                <tr data-uipath="ps.publication.information-types">
                    <th>Information type</th>
                    <td><#list publication.informationType as type>${type}<#sep>, </#list></td>
                </tr>
            </#if>

            <#if publication.geographicCoverage?has_content>
                <tr>
                    <th><@fmt.message key="headers.geographical-coverage"/></th>
                    <td data-uipath="ps.publication.geographic-coverage">
                        <#list publication.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                    </td>
                </tr>
            </#if>

            <#if publication.granularity?has_content >
                <tr>
                    <th><@fmt.message key="headers.geographical-granularity"/></th>
                    <td data-uipath="ps.publication.granularity"><#list publication.granularity as granularityItem>${granularityItem}<#sep>, </#list></td>
                </tr>
            </#if>

            <#if publication.coverageStart?? >
                <tr>
                    <th><@fmt.message key="headers.date-range"/></th>
                    <td data-uipath="ps.publication.date-range">
                        <#if publication.coverageStart?? && publication.coverageEnd??>
                            <@formatCoverageDates start=publication.coverageStart.time end=publication.coverageEnd.time/>
                        <#else>
                            (Not specified)
                        </#if>
                    </td>
                </tr>
            </#if>

        </table>

    </div>


    <#if publication.pages?has_content>
    <section data-uipath="ps.publication.pages">
        <ul>
            <li><a href="<@hst.link hippobean=publication/>">Overview</a></li>
            <#list publication.pages as page>
                <li><a href="<@hst.link hippobean=page/>" title="${page.title}">${page.title}</a></li>
            </#list>
        </ul>
    </section>
    </#if>

</#macro>

<#macro nominalPublicationDate publication>
    <tr>
        <th><@fmt.message key="headers.publication-date"/></th>
        <td data-uipath="ps.publication.nominal-publication-date"><@formatRestrictableDate value=publication.nominalPublicationDate/></td>
    </tr>
</#macro>
