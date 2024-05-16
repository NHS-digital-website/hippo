<#ftl output_format="HTML">
<script>
    var br_data = br_data || {};
    br_data.acct_id = "6405";
    br_data.catalogs = [ { "name" : "content_en" } ];
    if ("${hstRequest.requestContext.resolvedMount.mount.virtualHost.hostGroupName}" != "prd"){
    br_data.test_data="true";
    }
    (function() {
    var brtrk = document.createElement('script');
    brtrk.type = 'text/javascript';
    brtrk.async = true;
    brtrk.src = "//cdns.brsrvr.com/v1/br-trk-6405.js";
    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(brtrk, s);
    })();
</script>
