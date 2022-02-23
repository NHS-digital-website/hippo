<#ftl output_format="HTML">

<script>
    document.querySelector('html').classList.remove('nhsd-no-js');
</script>

<#-- Google Tag Manager -->
<script type="text/plain" data-cookieconsent="statistics">(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start': new Date().getTime(),event:'gtm.js'});
var f=d.getElementsByTagName(s)[0],
j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-W6GJCR9');</script>
<#-- End Google Tag Manager -->

<#-- GA Tracking code -->
<script type="text/plain" data-cookieconsent="statistics">
window.dataLayer = window.dataLayer || [];

function gtag() {dataLayer.push(arguments);}

gtag('js', new Date());
gtag('config', 'GTM-W6GJCR9', {'anonymize_ip': true});

function logGoogleAnalyticsEvent(action, category, label) {
    gtag('event', action, {
        'event_category': category,
        'event_label': label
    });
}
</script>

<#-- Hotjar tracking -->
<#include "hotjar.js.ftl"/>

<@hst.webfile var="webfilepath" path="" />
<script>
    window.resourceBaseUrl = "${webfilepath}";
</script>
<script src="<@hst.webfile path="/dist/nhsd-priority-scripts.bundle.js"/>"></script>
