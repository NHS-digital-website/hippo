<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">
<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Tableau" -->

<#macro tableau section index>

    <#if section?? && section.url??>
        <@hst.headContribution>
            <script type="text/javascript"
                    src="https://live.dashboards.data.digital.nhs.uk/javascripts/api/tableau-2.min.js"></script>
        </@hst.headContribution>

        <#assign divId = "tableau-${index}"/>

        <div id="${divId}"></div>

        <script type="text/javascript">
            window.addEventListener('load', initViz);

            function initViz() {
                var containerDiv = document.getElementById("${divId}");
                var url = "${section.url}";
                var options = {
                    hideTabs: ${section.hidetabs?string}
                    <#if section.device??>
                    ,device: "${section.device}"
                    </#if>
                };

                var viz = new tableau.Viz(containerDiv, url, options);
            }
        </script>
    </#if>
</#macro>
