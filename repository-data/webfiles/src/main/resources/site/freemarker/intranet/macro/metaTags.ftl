<#ftl output_format="HTML">

<#macro metaTags>

<#assign siteTitle = "NHS Digital Intranet"/>
<#assign pageTitle = 'Home - ' + siteTitle />
<#assign siteSEOSummary = "We’re the national information and technology partner to the health and social care system using digital technology to transform the NHS and social care" />
<#assign pageSEOSummary = siteSEOSummary />

<#if document?? && document.title??>
    <#assign pageTitle = document.title + ' - ' + siteTitle />
</#if>
<#if overridePageTitle?? >
    <#assign pageTitle = overridePageTitle + ' - ' + siteTitle />
</#if>
<#if document?? && document.seosummary?? && document.seosummary?has_content>
    <#noautoesc>
      <!-- strip HTML tags -->
      <#assign tempSEOSummary =  document.seosummary.content?replace('<[^>]+>','','r') />
      <#if tempSEOSummary?has_content>
        <#assign pageSEOSummary = tempSEOSummary />
      </#if>
    </#noautoesc>
</#if>

<#-- Generic meta tags -->
<@hst.headContribution keyHint="metaTitle" category="intranetMeta">
    <title>${pageTitle}</title>
</@hst.headContribution>
<@hst.headContribution keyHint="metaTitle2" category="intranetMeta">
    <meta name="title" content="${pageTitle}" />
</@hst.headContribution>
<@hst.headContribution keyHint="metaDescription" category="intranetMeta">
    <meta name="description" content="${pageSEOSummary}" />
</@hst.headContribution>

</#macro>