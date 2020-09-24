<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">
<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Tableau" -->

<#macro tableau section index>

    <#if section?? && section.url??>
        <@hst.headContribution>
            <script src="${section.tableauBase}javascripts/api/tableau-2.min.js"></script>
        </@hst.headContribution>

        <#assign divId = "tableau-${index}"/>

        <div id="${divId}" class="viz-wrapper">
            <#if section.placeholderImageLocation?has_content>
                <img id="${divId}-placeholder" class="viz-wrapper-item" src="${section.placeholderImageLocation}" />
                <div id="${divId}-viz" class="viz-wrapper-item hidden-viz"></div>
            <#else>
                <div id="${divId}-viz" class="viz-wrapper-item"></div>
            </#if>
        </div>

        <script type="text/javascript">
            var viz${index};
            function loadViz(containerDiv, placeholderDiv) {
                var url = "${section.url}";
                var options = {
                    "onFirstInteractive": function () {
                        containerDiv.classList.remove("hidden-viz");
                        if (placeholderDiv) {
                            placeholderDiv.remove();
                        }
                    },
                    hideTabs: ${section.hidetabs?string}
                    <#if section.device??>
                    ,device: "${section.device}"
                    </#if>
                };
                if(typeof tableau !== 'undefined' && typeof tableau.Viz !== 'undefined') {
                    viz${index} = new tableau.Viz(containerDiv, url, options);
                } else {
                    containerDiv.innerHTML = '[Tableau loading error]';
                }
            }

            window.addEventListener('load', function() {
                function placeholderDiv() {
                    return document.getElementById("${divId}-placeholder");
                }
                function containerDiv() {
                    return document.getElementById("${divId}-viz");
                }
                <#if section.placeholderImageLocation?has_content>
                    loadViz(containerDiv(), placeholderDiv());
                <#else>
                loadViz(containerDiv());
                </#if>
            });
        </script>
    </#if>
</#macro>
