<#ftl output_format="HTML">

<script>
    document.querySelector('html').classList.remove('nhsd-no-js');
</script>

<#-- GA Tracking code -->
<script>
    window.dataLayer = window.dataLayer || [];

    function gtag() {
        dataLayer.push(arguments);
    }

    gtag('js', new Date());
    gtag('config', 'UA-76954916-2', {'anonymize_ip': true});
</script>

<#-- Hotjar tracking -->
<#include "hotjar.js.ftl"/>

<@hst.webfile var="webfilepath" path="" />
<script>
    window.resourceBaseUrl = "${webfilepath}";
</script>
<script src="<@hst.webfile path="/dist/nhsd-priority-scripts.bundle.js"/>"></script>
