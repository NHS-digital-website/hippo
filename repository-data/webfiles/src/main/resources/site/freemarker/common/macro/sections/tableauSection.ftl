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

                <div id="${divId}-viz" class="viz-wrapper-item hidden-viz viz-wrapper-item-${section.device}"></div>
                <img id="${divId}-placeholder" class="viz-wrapper-item" src="${section.placeholderImageLocation}" />
                <div id="${divId}-loading" class="viz-wrapper-item">
                    <div class="viz-wrapper-loading">
                        <span id="${divId}-loading-message"></span>
                        <img id="${divId}-loading-icon" src="<@hst.webfile  path="images/loading-circle.gif"/>" alt="Loading data " />
                    </div>
                </div>
            <#else>
                <div id="${divId}-viz" class="viz-wrapper-item viz-wrapper-item-${section.device}"></div>
            </#if>
        </div>

        <@hst.setBundle basename="tableau.lables"/>
        <script type="text/javascript">
            var vizMessages = {
                LOAD_ERROR: "<@fmt.message key="load-error"/>",
                LOADING_MESSAGE: "<@fmt.message key="loading-message"/>"
            };
        </script>

        <script type="text/javascript">

            // Viz instance
            var viz${index};

            // Viz instance supporting HTML elements
            var viz${index}Elements = {
                loadingDiv: function () {
                    return document.getElementById("${divId}-loading");
                },
                loadingIcon: function () {
                    return document.getElementById("${divId}-loading-icon");
                },
                loadingMessage: function (){
                    return document.getElementById("${divId}-loading-message");
                },
                placeholderImage: function () {
                    return document.getElementById("${divId}-placeholder");
                },
                vizDiv: function () {
                    return document.getElementById("${divId}-viz");
                },
                containerDiv: function () {
                    return document.getElementById("${divId}");
                }
            };

            // Setup reload attempt
            var viz${index}LoadTimer = setInterval(_retry, 15000);

            function loadViz(containerDiv, placeholderElements) {
                var url = "${section.url}";
                var options = {
                    "onFirstInteractive": function () {
                        clearInterval(viz${index}LoadTimer);
                        _onFirstInteractive(containerDiv, placeholderElements);
                    },
                    hideTabs: ${section.hidetabs?string}
                    <#if section.device??>
                    ,device: "${section.device}"
                    </#if>
                };
                if(typeof tableau !== 'undefined' && typeof tableau.Viz !== 'undefined') {
                    viz${index} = new tableau.Viz(containerDiv, url, options);
                } else {
                    _showLoadingError();
                }
            }

            function _onFirstInteractive(containerDiv, placeholderElements){
                containerDiv.classList.remove("hidden-viz");
                if (Array.isArray(placeholderElements) && placeholderElements.length) {
                    placeholderElements.forEach(function (el) {
                        if(el instanceof HTMLElement) {
                            el.remove();
                        }
                    });
                }
            }

            function _load() {
                <#if section.placeholderImageLocation?has_content>
                loadViz(viz${index}Elements.vizDiv(), [viz${index}Elements.placeholderImage(), viz${index}Elements.loadingDiv()]);
                <#else>
                loadViz(viz${index}Elements.vizDiv());
                </#if>
            }

            window.addEventListener('load', function() {
                _load();
            });

            function _retry() {
                _setRetryMessage();
                _load();
            }

            function _setRetryMessage() {
                var message${index} = viz${index}Elements.loadingMessage();
                if(!!(message${index})) {
                    message${index}.classList.add("fade-in-2");
                    message${index}.innerHTML = vizMessages.LOADING_MESSAGE;
                }
            }

            function _showLoadingError() {
                viz${index}Elements.containerDiv().innerHTML = vizMessages.LOAD_ERROR;
            }
        </script>
    </#if>
</#macro>
