<#ftl output_format="HTML">

<!-- Google Tag Manager -->
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
        new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
      j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
      'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
  })(window,document,'script','dataLayer','GTM-W6GJCR9');</script>
<!-- End Google Tag Manager -->

<#-- Hotjar tracking -->
<#include "../../common/scripts/hotjar.js.ftl"/>

<@hst.webfile var="webfilepath" path="" />
<script>
    window.resourceBaseUrl = "${webfilepath}";
</script>

<#--
Todo once confident that all this JS can go, come back and clean up:
    - repository-data/webfiles/src/main/resources/site/src/js/intranet-scripts.js
    - repository-data/webfiles/src/main/resources/site/src/js/nhsd-priority-scripts.js
<script defer src="<@hst.webfile path="/dist/intranet-scripts.bundle.js"/>"></script>
<script defer src="<@hst.webfile path="/dist/nhsd-priority-scripts.bundle.js"/>"></script>
-->
