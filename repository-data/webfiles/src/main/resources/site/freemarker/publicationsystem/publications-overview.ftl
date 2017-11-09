<#-- DECLARATIONS: -->
<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] />
<#assign fmt=JspTaglibs ["http://java.sun.com/jsp/jstl/fmt"] />
<#assign truncate="uk.nhs.digital.ps.directives.TruncateFormatterDirective"?new() >

<#macro restrictableDate date>
    <#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />
<span>
    <#if date??>
        <@formatRestrictableDate value=date/>
    </#if>
<span>
</#macro>

<#-- @ftlvariable name="publication" type="uk.nhs.digital.ps.beans.Publication" -->


<#-- CONTENT: -->
<div class="column-half__left">
    <h2>Latest publications</h2>
    <#if publishedPublications??>
        <ul>
            <#list publishedPublications as publication>
                <@hst.link hippobean=publication var="link"/>
                <li>
                <a href="${link}">${publication.title}</a> <@restrictableDate date=publication.nominalPublicationDate/>
                <p><@truncate text=publication.summary size=100/></p>
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
                <li>
                ${publication.title} <@restrictableDate date=publication.nominalPublicationDate/>
            </li>
            </#list>
    </ul>
    <#else>
        (None)
    </#if>
</div>
