<#ftl output_format="HTML">

<@hst.setBundle basename="design-system"/>
<@fmt.message key="design-system.url" var="designSystemUrl" />
<@fmt.message key="design-system.version" var="toolkitVersion" />
<#if !toolkitVersion?has_content>
    <#assign toolkitVersion = "latest"/>
</#if>

<script defer src="<@hst.webfile path="/dist/nhsd-frontend-scripts.bundle.js"/>"></script>
<script defer src="${designSystemUrl}/cdn/${toolkitVersion}/scripts/nhsd-frontend.js"></script>
