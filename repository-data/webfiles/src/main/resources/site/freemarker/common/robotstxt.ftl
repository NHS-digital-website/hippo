<#ftl output_format="plainText">
<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] >
<#if document??>
<#if document.sections??>
<#list document.sections as section>
<#if section.userAgent?? && (section.disallows?? || disallowedFacNavLinks??)>
User-agent: ${section.userAgent}
<#list section.disallows as path>Disallow: ${path}
</#list>
</#if>
</#list>
</#if>
<#if document.sitemaps??>
<#list document.sitemaps as sitemap>
Sitemap: ${sitemap}
</#list>
</#if>
</#if>
