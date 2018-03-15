<#ftl output_format="HTML">
<#assign dateFormat="dd/MM/yyyy"/>

<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] >
<#assign fmt=JspTaglibs["http://java.sun.com/jsp/jstl/fmt"] >

<#assign truncate="uk.nhs.digital.ps.directives.TruncateFormatterDirective"?new() >

<@hst.defineObjects />

<#if facets??>
    <@hst.link var="searchLink" hippobean=facets />
<#else>
    <@hst.link siteMapItemRefId="search" var="searchLink"/>
</#if>
