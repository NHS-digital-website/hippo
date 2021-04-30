<#ftl output_format="HTML">
<#macro searchTabsComponent contentNames>
    <@hst.setBundle basename="feature-toggles" scope="pageContext"/>
    <@fmt.message key="feature.tabs" var="featureTabs"/>
    <#if contentNames?seq_contains("tabs") && featureTabs?boolean>
        <div class="nav-tabs">
            <@hst.include ref="tabs"/>
        </div>
    </#if>
</#macro>
