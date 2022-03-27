<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "common/macro/searchTabsComponent.ftl">
<#include "macro/globalHeader.ftl">
<#include "common/macro/siteFooter.ftl">
<#include "common/macro/metaTags.ftl">
<#include "common/macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">
    <#include "nhsd-homepage-head.ftl">
        <body class="debugs">
            <@skipLink />
            <#-- Add IE banner -->
            <@hst.include ref="ie-banner"/>
            <#-- Add site header with the search bar -->
            <@globalHeader false></@globalHeader>
            <hr class="nhsd-a-horizontal-rule nhsd-!t-margin-0">
            <@hst.include ref="coronavirus-banner"/>
            <#-- Add breadcrumbs -->
            <#include "macro/pubsBreadcrumb.ftl">
            <@pubsBreadcrumb "Search results"></@pubsBreadcrumb>
            <@hst.include ref="top"/>
            <@hst.include ref="main"/>
            <@siteFooter />
            <@hst.include ref="footer-menu"/>
            <#include "scripts/footer-scripts.ftl" />
            <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
        </body>
</html>
