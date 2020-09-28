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
                <div id="${divId}-viz" class="viz-wrapper-item hidden-viz"></div>
                <img id="${divId}-placeholder" class="viz-wrapper-item" src="${section.placeholderImageLocation}" />
                <div id="${divId}-loading" class="viz-wrapper-item">
                    <img class="viz-wrapper-loading" src="<@hst.webfile  path="images/loading-circle.gif"/>" alt="Loading icon " />
                </div>
            <#else>
                <div id="${divId}-viz" class="viz-wrapper-item"></div>
            </#if>
        </div>

        <script type="text/javascript">


            var viz${index};
            function loadViz(containerDiv, placeholderElements) {
                var url = "${section.url}";
                var options = {
                    "onFirstInteractive": function () {
                        containerDiv.classList.remove("hidden-viz");
                        if (Array.isArray(placeholderElements) && placeholderElements.length) {
                            placeholderElements.forEach(function (el) {
                                if(el instanceof HTMLElement) {
                                    el.remove();
                                }
                            });
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
                function placeholderImage() {
                    return document.getElementById("${divId}-placeholder");
                }
                function loadingDiv() {
                    return document.getElementById("${divId}-loading");
                }
                function containerDiv() {
                    return document.getElementById("${divId}-viz");
                }
                <#if section.placeholderImageLocation?has_content>
                    loadViz(containerDiv(), [placeholderImage(), loadingDiv()]);
                <#else>
                loadViz(containerDiv());
                </#if>
            });
        </script>
    </#if>
</#macro>
