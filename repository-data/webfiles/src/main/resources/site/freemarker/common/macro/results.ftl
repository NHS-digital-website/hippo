<#ftl output_format="HTML">
<@hst.setBundle basename="publicationsystem.labels,nationalindicatorlibrary.headers,nationalindicatorlibrary.labels"/>
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />
<#assign dateFormat="dd/MM/yyyy"/>

<#macro searchResults items>
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
        </#if>
    </#list>
</#macro>

<#macro publication item>
    <div class="push-double--bottom" data-uipath="ps.search-results.result">
        <h3 class="flush zeta" data-uipath="ps.search-results.result.type" style="font-weight:bold"><@fmt.message key="labels.publication"/></h3>
        <#if item.informationType?has_content>
            <#list item.informationType as type>
                <#if type == "National statistics">
                    <div class="media__icon--national-statistics" data-uipath="ps.search-results.result.national-statistics" title="National Statistics"></div>
                    <#break>
                </#if>
            </#list>
        </#if>
        <p class="flush">
            <a href="<@hst.link hippobean=item.selfLinkBean/>" title="${item.title}" data-uipath="ps.search-results.result.title">
                ${item.title}
            </a>
        </p>
        <p class="flush zeta" data-uipath="ps.search-results.result.date"><@formatRestrictableDate value=item.nominalPublicationDate/></p>
        <p class="flush" data-uipath="ps.search-results.result.summary"><@truncate text=item.summary.firstParagraph size="300"/></p>
    </div>
</#macro>

<#macro legacypublication item>
    <div class="push-double--bottom" data-uipath="ps.search-results.result">
        <h3 class="flush zeta" data-uipath="ps.search-results.result.type" style="font-weight:bold"><@fmt.message key="labels.publication"/></h3>
        <p class="flush">
            <a href="<@hst.link hippobean=item.selfLinkBean/>" title="${item.title}" data-uipath="ps.search-results.result.title">
                ${item.title}
            </a>
        </p>
        <p class="flush zeta" data-uipath="ps.search-results.result.date"><@formatRestrictableDate value=item.nominalPublicationDate/></p>
    </div>
</#macro>

<#macro series item>
    <div class="push-double--bottom" data-uipath="ps.search-results.result">
        <h3 class="flush zeta" data-uipath="ps.search-results.result.type" style="font-weight:bold"><@fmt.message key="labels.series"/></h3>
        <p class="flush">
            <a href="<@hst.link hippobean=item.selfLinkBean/>" title="${item.title}" data-uipath="ps.search-results.result.title">
                ${item.title}
            </a>
        </p>
        <p class="flush" data-uipath="ps.search-results.result.summary"><@truncate text=item.summary.firstParagraph size="300"/></p>
        <#if item.latestPublication??>
            <p class="flush zeta" data-uipath="ps.search-results.result.latest-publication" style="font-weight:bold">
                Latest publication:
                <a href="<@hst.link hippobean=item.latestPublication.selfLinkBean/>" title="${item.latestPublication.title}">
                    ${item.latestPublication.title}
                </a>
            </p>
        </#if>
    </div>
</#macro>

<#macro archive item>
    <div class="push-double--bottom" data-uipath="ps.search-results.result">
        <h3 class="flush zeta" data-uipath="ps.search-results.result.type" style="font-weight:bold"><@fmt.message key="labels.archive"/></h3>
        <p class="flush">
            <a href="<@hst.link hippobean=item.selfLinkBean/>" title="${item.title}" data-uipath="ps.search-results.result.title">
                ${item.title}
            </a>
        </p>
        <p class="flush" data-uipath="ps.search-results.result.summary"><@truncate text=item.summary.firstParagraph size="300"/></p>
    </div>
</#macro>

<#macro dataset item>
    <div class="push-double--bottom" data-uipath="ps.search-results.result">
        <h3 class="flush zeta" data-uipath="ps.search-results.result.type" style="font-weight:bold"><@fmt.message key="labels.dataset"/></h3>
        <p class="flush">
            <a href="<@hst.link hippobean=item.selfLinkBean/>" title="${item.title}" data-uipath="ps.search-results.result.title">
                ${item.title}
            </a>
        </p>
        <p class="flush zeta" data-uipath="ps.search-results.result.date"><@formatRestrictableDate value=item.nominalDate/></p>
        <p class="flush" data-uipath="ps.search-results.result.summary"><@truncate text=item.summary.firstParagraph size="300"/></p>
    </div>
</#macro>

<#macro indicator item>
    <div class="push-double--bottom" data-uipath="ps.search-results.result">
        <h3 class="flush zeta" data-uipath="ps.search-results.result.type" style="font-weight:bold"><@fmt.message key="labels.indicator"/></h3>
        <p class="flush">
            <a href="<@hst.link hippobean=item.selfLinkBean/>" title="${item.title}" data-uipath="ps.search-results.result.title">
                ${item.title}
            </a>
        </p>

        <#if item.assuredStatus>
            <div class="media__icon--assured-indicator" data-uipath="ps.search-results.result.assured-indicator-icon" title="Assured Indicator"></div>
            <p class="flush zeta" data-uipath="ps.search-results.result.assured-status"><@fmt.message key="labels.assured"/></p>
            <p class="flush zeta" data-uipath="ps.search-results.result.publisher-and-date" style="font-weight:bold"><@fmt.message key="headers.publishedBy"/>: ${item.publishedBy}. <@fmt.message key="headers.assured"/>: ${item.assuranceDate.time?string[dateFormat]}</p>
        <#else>
            <p class="flush zeta" data-uipath="ps.search-results.result.assured-status"><@fmt.message key="labels.unassured"/></p>
            <p class="flush zeta" data-uipath="ps.search-results.result.publisher-and-date" style="font-weight:bold"><@fmt.message key="headers.publishedBy"/>: ${item.publishedBy}. <@fmt.message key="headers.unassured"/>: ${item.assuranceDate.time?string[dateFormat]}</p>
        </#if>         
        
        <p class="flush" data-uipath="ps.search-results.result.brief-description">${item.details.briefDescription}</p>        
    </div>
</#macro>
