<#ftl output_format="HTML">

<#-- Add CSS class to mark JS enabled -->
<#include "js-enabled.js.ftl"/>

<#include "vanilla-js-utils.js.ftl"/>

<#-- Cookiebot loading -->
<#include "cookiebot-load.js.ftl"/>

<#-- GA Tracking code -->
<#include "google-analytics.js.ftl"/>

<!--[if IE]>
<script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script>
<![endif]-->
<script src="<@hst.webfile path="/js/jquery/jquery-3.3.1.min.js"/>"></script>
