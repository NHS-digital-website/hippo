<#ftl output_format="HTML">
<@hst.setBundle basename="feature-toggles"/>
<#macro searchTabsComponent contentNames>
    <@fmt.message key="feature.tabs" var="featureTabs"/>
    <#if contentNames?seq_contains("tabs") && featureTabs?boolean>
        <div class="nav-tabs">
            <@hst.include ref="tabs"/>
        </div>
    </#if>
</#macro>