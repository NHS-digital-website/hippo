<#ftl output_format="HTML">

<script>
    document.querySelector('html').classList.remove('nhsd-no-js');
</script>

<@hst.webfile var="webfilepath" path="" />
<script>
    window.resourceBaseUrl = "${webfilepath}";
</script>
<script src="<@hst.webfile path="/dist/nhsd-priority-scripts.bundle.js"/>"></script>
<script src="<@hst.webfile path="/dist/nhse-menu-widget.bundle.js"/>"></script>
