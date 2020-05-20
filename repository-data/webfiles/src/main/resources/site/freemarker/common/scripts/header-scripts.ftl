<#ftl output_format="HTML">

<#-- GA Tracking code -->
<script src="https://www.googletagmanager.com/gtag/js?id=UA-76954916-2"></script>

<#-- Hotjar tracking -->
<#include "hotjar.js.ftl"/>

<@hst.webfile var="webfilepath" path="" />
<script>
    window.resourceBaseUrl = "${webfilepath}";
</script>
<script defer src="<@hst.webfile path="/dist/nhsd-priority-scripts.bundle.js"/>"></script>
