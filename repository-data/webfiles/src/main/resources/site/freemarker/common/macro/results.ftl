<#ftl output_format="HTML">
<@hst.setBundle basename="publicationsystem.labels,nationalindicatorlibrary.headers,nationalindicatorlibrary.labels,website.labels"/>

<#macro searchResults items>
    <div class="cta-list">
        <#list items as document>
            <#if document.class.name == "uk.nhs.digital.ps.beans.Publication">
                <@publication item=document />
            <#elseif document.class.name == "uk.nhs.digital.ps.beans.LegacyPublication">
                <@legacypublication item=document />
            <#elseif document.class.name == "uk.nhs.digital.ps.beans.Archive">
                <@archive item=document />
            <#elseif document.class.name == "uk.nhs.digital.ps.beans.Series">
                <@series item=document />
            <#elseif document.class.name == "uk.nhs.digital.ps.beans.Dataset">
                <@dataset item=document />
            <#elseif document.class.name == "uk.nhs.digital.nil.beans.Indicator">
                <@indicator item=document />
            <#elseif document.class.name == "uk.nhs.digital.website.beans.Service">
                <@service item=document />
            <#elseif document.class.name == "uk.nhs.digital.website.beans.General" ||
                     document.class.name == "uk.nhs.digital.website.beans.Hub">
                <@general item=document />
            </#if>
        </#list>
    </div>
</#macro>

<#macro publication item>
    <#assign stampedPublication = false />
    <#if item.informationType?has_content>
        <#list item.informationType as type>
            <#if type == "National statistics">
                <#assign stampedPublication = true />
                <#break>
            </#if>
        </#list>
    </#if>

    <div class="cta cta--detailed ${stampedPublication?then(" cta--stamped", "")}" data-uipath="ps.search-results.result">
        <#if stampedPublication>
            <div class="cta__stamped-header">
                <div class="cta__stamped-header-col cta__stamped-header-col--left">
        </#if>

        <div>
            <h3 class="cta__label" data-uipath="ps.search-results.result.type"><@fmt.message key="labels.publication"/></h3>
        </div>

        <a class="cta__title cta__button" href="<@hst.link hippobean=item.selfLinkBean/>" title="${item.title}" data-uipath="ps.search-results.result.title">
            ${item.title}
        </a>
        <span class="cta__meta" data-uipath="ps.search-results.result.date"><@formatRestrictableDate value=item.nominalPublicationDate/></span>

        <#if stampedPublication>
                </div>

                <div class="cta__stamped-header-col cta__stamped-header-col--right">
                    <img src="<@hst.webfile  path="images/national-statistics-logo@2x.png"/>" data-uipath="ps.search-results.result.national-statistics" title="National Statistics" class="image-icon image-icon--large" />
                </div>
            </div>
        </#if>

        <#if item.publiclyAccessible>
            <p class="cta__text" data-uipath="ps.search-results.result.summary">
                <@truncate text=item.summary.firstParagraph size="300"/>
            </p>
        <#else>
            <span class="cta__meta" data-uipath="ps.search-results.result.summary">
                <@fmt.message key="labels.upcoming-publication"/>
            </span>
        </#if>
    </div>
</#macro>

<#macro legacypublication item>
    <div class="cta cta--detailed" data-uipath="ps.search-results.result">
        <div>
            <h3 class="cta__label" data-uipath="ps.search-results.result.type"><@fmt.message key="labels.publication"/></h3>
        </div>
        <a class="cta__title cta__button" href="<@hst.link hippobean=item.selfLinkBean/>" title="${item.title}" data-uipath="ps.search-results.result.title">
            ${item.title}
        </a>
        <span class="cta__meta" data-uipath="ps.search-results.result.date"><@formatRestrictableDate value=item.nominalPublicationDate/></span>
    </div>
</#macro>

<#macro series item>
    <div class="cta cta--detailed" data-uipath="ps.search-results.result">
        <div>
            <h3 class="cta__label" data-uipath="ps.search-results.result.type"><@fmt.message key="labels.series"/></h3>
        </div>
        <a class="cta__title cta__button" href="<@hst.link hippobean=item.selfLinkBean/>" title="${item.title}" data-uipath="ps.search-results.result.title">
            ${item.title}
        </a>
        <p class="cta__text" data-uipath="ps.search-results.result.summary"><@truncate text=item.summary.firstParagraph size="300"/></p>

        <#if item.latestPublication??>
        <p class="cta__text" data-uipath="ps.search-results.result.latest-publication">
            Latest publication:
            <a href="<@hst.link hippobean=item.latestPublication.selfLinkBean/>" title="${item.latestPublication.title}">
                ${item.latestPublication.title}
            </a>
        </p>
        </#if>
    </div>
</#macro>

<#macro archive item>
    <div class="cta cta--detailed" data-uipath="ps.search-results.result">
        <div>
            <h3 class="cta__label" data-uipath="ps.search-results.result.type"><@fmt.message key="labels.archive"/></h3>
        </div>
        <a class="cta__title cta__button" href="<@hst.link hippobean=item.selfLinkBean/>" title="${item.title}" data-uipath="ps.search-results.result.title">
            ${item.title}
        </a>
        <p class="cta__text" data-uipath="ps.search-results.result.summary"><@truncate text=item.summary.firstParagraph size="300"/></p>
    </div>
</#macro>

<#macro dataset item>
    <div class="cta cta--detailed" data-uipath="ps.search-results.result">
        <div>
            <h3 class="cta__label" data-uipath="ps.search-results.result.type"><@fmt.message key="labels.dataset"/></h3>
        </div>
        <a class="cta__title cta__button" href="<@hst.link hippobean=item.selfLinkBean/>" title="${item.title}" data-uipath="ps.search-results.result.title">
            ${item.title}
        </a>
        <span class="cta__meta" data-uipath="ps.search-results.result.date"><@formatRestrictableDate value=item.nominalDate/></span>
        <p class="cta__text" data-uipath="ps.search-results.result.summary"><@truncate text=item.summary.firstParagraph size="300"/></p>
    </div>
</#macro>

<#macro indicator item>
    <div class="cta cta--detailed" data-uipath="ps.search-results.result">
        <div>
            <h3 class="cta__label" data-uipath="ps.search-results.result.type"><@fmt.message key="labels.indicator"/></h3>
        </div>
        <a class="cta__title cta__button" href="<@hst.link hippobean=item.selfLinkBean/>" title="${item.title}" data-uipath="ps.search-results.result.title">
                ${item.title}
        </a>

        <p class="cta__text" data-uipath="ps.search-results.result.brief-description">${item.details.briefDescription}</p>

        <#if item.assuredStatus>
            <div class="cta__assurance">
                <div class="cta__metas">
                    <span class="cta__meta" data-uipath="ps.search-results.result.assured-status"><@fmt.message key="labels.assured"/></span>
                    <span class="cta__meta" data-uipath="ps.search-results.result.publisher-and-date">
                    <span class="strong"><@fmt.message key="headers.publishedBy"/>:</span> ${item.publishedBy}.
                    <span class="strong"><@fmt.message key="headers.assured"/>:</span> <@formatDate date=item.assuranceDate.time/></span>
                </div>
                <div class="cta__badge">
                    <span data-uipath="ps.search-results.result.assured-indicator-icon" title="Assured Indicator" class="badge badge--assured"></span>
                </div>
            </div>
        <#else>
            <div class="cta__metas">
                <span class="cta__meta" data-uipath="ps.search-results.result.assured-status"><@fmt.message key="labels.unassured"/></span>
                <span class="cta__meta" data-uipath="ps.search-results.result.publisher-and-date"><span class="strong"><@fmt.message key="headers.publishedBy"/>:</span> ${item.publishedBy}. <@fmt.message key="headers.unassured"/>: <@formatDate date=item.assuranceDate.time/></span>
            </div>
        </#if>
    </div>
</#macro>

<#macro service item>
    <div class="cta cta--detailed" data-uipath="ps.search-results.result">
        <div>
            <h3 class="cta__label" data-uipath="ps.search-results.result.type"><@fmt.message key="labels.service"/></h3>
        </div>
        <a class="cta__title cta__button" href="<@hst.link hippobean=item/>" title="${item.title}" data-uipath="ps.search-results.result.title">
            ${item.title}
        </a>
        <p class="cta__text" data-uipath="ps.search-results.result.summary"><@truncate text=item.shortsummary size="300"/></p>
    </div>
</#macro>

<#macro general item>
    <div class="cta cta--detailed" data-uipath="ps.search-results.result">
        <a class="cta__title cta__button" href="<@hst.link hippobean=item/>" title="${item.title}" data-uipath="ps.search-results.result.title">
            ${item.title}
        </a>
        <p class="cta__text" data-uipath="ps.search-results.result.summary"><@truncate text=item.shortsummary size="300"/></p>
    </div>
</#macro>
