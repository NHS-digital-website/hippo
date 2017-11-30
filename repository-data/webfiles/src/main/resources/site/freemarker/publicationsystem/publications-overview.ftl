<#-- DECLARATIONS: -->
<#include "../include/imports.ftl">

<#macro restrictableDate date>
    <#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />
    <#if date??>
        <@formatRestrictableDate value=date/>
    </#if>
</#macro>

<#-- @ftlvariable name="publication" type="uk.nhs.digital.ps.beans.Publication" -->


<#-- CONTENT: -->
<div class="column-half__left">
    <h2>Latest publications</h2>
    <#if publishedPublications??>
        <ul>
            <#list publishedPublications as publication>
                <@hst.link hippobean=publication var="link"/>
                <li data-uipath="ps.overview.latest-publications.publication">
                    <a data-uipath="ps.overview.latest-publications.publication.hyperlink" href="${link}">
                        <span data-uipath="ps.overview.latest-publications.publication.title">${publication.title}</span>
                    </a>
                    <span data-uipath="ps.overview.latest-publications.publication.nominal-publication-date">
                        <@restrictableDate date=publication.nominalPublicationDate/>
                    </span>
                    <p data-uipath="ps.overview.latest-publications.publication.summary">
                        <@truncate text=publication.summary.firstParagraph size=100/>
                    </p>
                </li>
            </#list>
        </ul>
    <#else>
        (None)
    </#if>
</div>

<div class="column-half__right">
    <h2>Upcoming publications</h2>
    <#if upcomingPublications??>
        <ul>
            <#list upcomingPublications as publication>
                <li data-uipath="ps.overview.upcoming-publications.publication">
                    <span data-uipath="ps.overview.upcoming-publications.publication.title">${publication.title}</span>
                    <span data-uipath="ps.overview.upcoming-publications.publication.nominal-publication-date"><@restrictableDate date=publication.nominalPublicationDate/></span>
                </li>
            </#list>
        </ul>
    <#else>
        (None)
    </#if>
</div>
