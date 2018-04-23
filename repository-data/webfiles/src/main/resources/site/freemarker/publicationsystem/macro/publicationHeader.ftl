<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro publicationHeader publication>
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">

                    <@nationalStatsStamp publication=publication/>

                    <span class="article-header__label"><@fmt.message key="labels.publication"/></span>
                    <h1 class="local-header__title" data-uipath="document.title">${publication.title}</h1>
                    <#if publication.parentDocument??>
                        <p class="article-header__subtitle">
                            This is part of
                            <a href="<@hst.link hippobean=publication.parentDocument.selfLinkBean/>"
                                title="${publication.parentDocument.title}">
                                ${publication.parentDocument.title}
                            </a>
                        </p>
                    </#if>

                    <span data-uipath="ps.publication.information-types">
                        <#if publication.informationType?has_content>
                            <#list publication.informationType as type>${type}<#sep>, </#list>
                        </#if>
                    </span>

                    <hr class="hr hr--short hr--light">

                    <div class="article-header__detail-lines">
                        <div class="article-header__detail-line">
                            <dl class="article-header__detail-list">
                                <dt class="article-header__detail-list-key"><@fmt.message key="headers.publication-date"/></dt>
                                <dd class="article-header__detail-list-value" data-uipath="ps.publication.nominal-publication-date">
                                    <@formatRestrictableDate value=publication.nominalPublicationDate/>
                                </dd>
                            </dl>
                        </div>

                        <#if publication.geographicCoverage?has_content>
                            <div class="article-header__detail-line">
                                <dl class="article-header__detail-list">
                                    <dt class="article-header__detail-list-key" id="geographic-coverage"><@fmt.message key="headers.geographical-coverage"/></dt>
                                    <dd class="article-header__detail-list-value" data-uipath="ps.publication.geographic-coverage">
                                        <#list publication.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                                    </dd>
                                </dl>
                            </div>
                        </#if>

                        <#if publication.granularity?has_content >
                            <div class="article-header__detail-line">
                                <dl class="article-header__detail-list">
                                    <dt class="article-header__detail-list-key"><@fmt.message key="headers.geographical-granularity"/></dt>
                                    <dd class="article-header__detail-list-value" data-uipath="ps.publication.granularity">
                                        <#list publication.granularity as granularityItem>${granularityItem}<#sep>, </#list>
                                    </dd>
                                </dl>
                            </div>
                        </#if>

                        <#if publication.coverageStart?? >
                            <div class="article-header__detail-line">
                                <dl class="article-header__detail-list">
                                    <dt class="article-header__detail-list-key"><@fmt.message key="headers.date-range"/></dt>
                                    <dd class="article-header__detail-list-value" data-uipath="ps.publication.date-range">
                                        <#if publication.coverageStart?? && publication.coverageEnd??>
                                            <@formatCoverageDates start=publication.coverageStart.time end=publication.coverageEnd.time/>
                                        <#else>
                                            (Not specified)
                                        </#if>
                                    </dd>
                                </dl>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <#if publication.pages?has_content>
        <div class="grid-wrapper grid-row" data-uipath="ps.publication.pages">
            <#assign chunks=publication.pages?chunk(publication.pages?size/2)/>

            <div class="column column--one-half column--left">
                <ul class="list list--reset cta-list">
                    <li><a href="<@hst.link hippobean=publication/>">Overview</a></li>
                    <#list chunks[0] as page>
                        <!--<li class=""><span class="">Overview</span></li>-->
                        <li><a href="<@hst.link hippobean=page/>" title="${page.title}">${page.title}</a></li>
                    </#list>
                </ul>
            </div>
            <div class="column column--one-half column--right">
                <ul class="list list--reset ">
                    <#list chunks[1] as page>
                        <li><a href="<@hst.link hippobean=page/>" title="${page.title}">${page.title}</a></li>
                    </#list>
                    <#if chunks?size == 3>
                        <#list chunks[2] as page>
                            <li><a href="<@hst.link hippobean=page/>" title="${page.title}">${page.title}</a></li>
                        </#list>
                    </#if>
                </ul>
            </div>
        </div>
    </#if>
</#macro>

<#macro nationalStatsStamp publication>
    <#if publication.informationType??>
        <#list publication.informationType as type>
            <#if type == "National statistics">
                <div class="article-header__stamp" data-uipath="ps.publication.national-statistics" title="National statistics">
                    <img src="<@hst.webfile path="images/national-statistics-logo@2x.png"/>" data-uipath="ps.publication.national-statistics" title="National Statistics" class="image-icon image-icon--large" />
                </div>
                <#break>
            </#if>
        </#list>
    </#if>
</#macro>
