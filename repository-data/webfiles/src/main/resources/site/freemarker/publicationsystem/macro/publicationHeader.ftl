<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#assign dateFormat="dd/MM/yyyy"/>
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />
<#assign formatCoverageDates="uk.nhs.digital.ps.directives.CoverageDatesFormatterDirective"?new() />

<#macro publicationHeader publication>
    <section class="document-header" aria-label="Document Header">
        <div class="document-header__inner">

            <#if publication.informationType?has_content>
                <#list publication.informationType as type>
                    <#if type == "National statistics">
                        <div class="media__icon--national-statistics" data-uipath="ps.publication.national-statistics" title="National statistics"></div>
                        <#break>
                    </#if>
                </#list>
            </#if>

            <h3 class="flush--bottom push-half--top"><@fmt.message key="labels.publication"/></h3>

            <h3 data-uipath="ps.publication.information-types" class="flush--bottom push-half--top">
                <#if publication.informationType?has_content>
                    <#list publication.informationType as type>${type}<#sep>, </#list>
                </#if>
            </h3>

            <h1 class="layout-5-6" data-uipath="document.title">${publication.title}</h1>

            <#if publication.parentDocument??>
                <p class="flush--top">
                    This is part of
                    <a class="label label--parent-document" href="<@hst.link hippobean=publication.parentDocument.selfLinkBean/>"
                        title="${publication.parentDocument.title}">
                        ${publication.parentDocument.title}
                    </a>
                </p>
            </#if>

            <div class="flex push--top push--bottom">
                <div class="flex__item">
                    <div class="media">
                        <div class="media__icon media__icon--calendar"></div>
                        <@nominalPublicationDate publication/>
                    </div>
                </div><!--

                --><#if publication.geographicCoverage?has_content><div class="flex__item">
                    <div class="media">
                        <div class="media__icon media__icon--geographic-coverage"></div>
                        <dl class="media__body">
                            <dt id="geographic-coverage"><@fmt.message key="headers.geographical-coverage"/></dt>
                            <dd data-uipath="ps.publication.geographic-coverage">
                                <#list publication.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                            </dd>
                        </dl>
                    </div>
                </div></#if><!--

                --><#if publication.granularity?has_content ><div class="flex__item">
                    <div class="media">
                        <div class="media__icon media__icon--granularity"></div>
                        <dl class="media__body">
                            <dt><@fmt.message key="headers.geographical-granularity"/></dt>
                            <dd data-uipath="ps.publication.granularity">
                                <#list publication.granularity as granularityItem>${granularityItem}<#sep>, </#list>
                            </dd>
                        </dl>
                    </div>
                </div></#if><!--

                --><#if publication.coverageStart?? ><div class="flex__item">
                    <div class="media">
                        <div class="media__icon media__icon--date-range"></div>
                        <dl class="media__body">
                            <dt><@fmt.message key="headers.date-range"/></dt>
                            <dd data-uipath="ps.publication.date-range">
                                <#if publication.coverageStart?? && publication.coverageEnd??>
                                    <@formatCoverageDates start=publication.coverageStart.time end=publication.coverageEnd.time/>
                                <#else>
                                    (Not specified)
                                </#if>
                            </dd>
                        </dl>
                    </div>
                </div></#if>

            </div>
        </div>
    </section>

	<#if publication.pages?has_content>
		<section class="document-content" data-uipath="ps.publication.pages">
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
    <dl class="media__body">
        <dt><@fmt.message key="headers.publication-date"/></dt>
        <dd class="zeta" data-uipath="ps.publication.nominal-publication-date">
            <@formatRestrictableDate value=publication.nominalPublicationDate/>
        </dd>
    </dl>
</#macro>
