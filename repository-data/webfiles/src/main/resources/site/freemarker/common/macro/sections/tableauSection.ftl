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
            var viz;

            function throttleViz(containerDiv) {
                <#if section.throttlingLocation?has_content>
                    <#local tableauThrottleSize="uk.nhs.digital.freemarker.tableau.RemoteThrottleSizeFromUrl"?new() />
                    <#local remote = tableauThrottleSize(section.throttlingLocation) />
                    var attemptProbability = ${remote.size};
                <#else>
                    var attemptProbability = 0.5;
                </#if>

                if (viz === undefined) {
                    if ((Math.floor(Math.random() * 101) / 100.0) <= attemptProbability) {
                        cookies.setCookie('tableauWithoutThrottling', 'yes', 0.042 <#-- One hour -->);
                        containerDiv.innerHTML = '';
                        loadViz(containerDiv);
                    } else {
                        containerDiv.innerHTML = '[Due to high demand, we can\'t connect you to the dashboard right now. Please wait and we will try and reconnect you automatically. Do not refresh your browser]';
                        setTimeout(function() {
                            throttleViz(containerDiv);
                        }, ${section.retry?string("#")});
                    }
                }
            }

            function loadViz(containerDiv) {
                var url = "${section.url}";
                var options = {
                    hideTabs: ${section.hidetabs?string}
                    <#if section.device??>
                    ,device: "${section.device}"
                    </#if>
                };
                viz = new tableau.Viz(containerDiv, url, options);
            }

            window.addEventListener('load', function() {
                function ifThrottling() {
                    if(cookies.getCookie('tableauWithoutThrottling') === 'yes') {
                        return false;
                    }
                    return ${section.throttling?then('true', 'false')};
                }
                function containerDiv() {
                    return document.getElementById("${divId}");
                }
                ifThrottling() ? throttleViz(containerDiv()) : loadViz(containerDiv());
            });
        </script>
    </#if>
</#macro>
