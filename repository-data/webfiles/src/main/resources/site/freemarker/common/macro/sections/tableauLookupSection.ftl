<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">
<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.TableauLookup" -->

<#macro tableauLookup section index>

    <@hst.setBundle basename="tableau.postcode.labels, tableau.lables, tableau.postcode.configuration"/>

    <#if section?? && section.url??>

        <@hst.headContribution>
            <script src="${section.tableauBase}javascripts/api/tableau-2.min.js"></script>
        </@hst.headContribution>

        <#assign divId = "tableau-${index}"/>

        <div class="eforms">
            <form action="javascript:void(0);">
                <div class="eforms-field" style="max-width: 300px">
                    <label for="${divId}-postcode" class="eforms-label"><@fmt.message key="postcode-label"/><span class="eforms-req"></span></label>
                    <input type="text" id="${divId}-postcode" aria-describedby="${divId}-postcode-hint" />
                    <div id="${divId}-postcode-validation-message" class="eforms-field__error-message visually-hidden" aria-live="polite"></div>
                    <span class="eforms-hint" id="${divId}-postcode-hint"><@fmt.message key="postcode-hint"/></span>
                </div>
                <div class="eforms-field" style="max-width: 300px">
                    <label for="${divId}-distance" class="eforms-label"><@fmt.message key="distance-label"/><span class="eforms-req"></span></label>
                    <select id="${divId}-distance" style="margin-top: 8px;" aria-describedby="${divId}-distance-hint">
                        <option value="1">1 mile</option>
                        <option value="2">2 miles</option>
                        <option selected value="3">3 miles</option>
                        <option value="4">4 miles</option>
                        <option value="5">5 miles</option>
                        <option value="6">6 miles</option>
                        <option value="7">7 miles</option>
                        <option value="8">8 miles</option>
                        <option value="9">9 miles</option>
                        <option value="10">10 miles</option>
                    </select>
                    <span class="eforms-hint" id="${divId}-distance-hint"><@fmt.message key="distance-hint"/></span>
                </div>
                <div class="eforms-buttons">
                    <input class="button" type="submit" value="Go" onclick="lookup()"/>
                </div>
            </form>

        </div>

        <div class="viz-wrapper">
            <div id="${divId}-viz" class="viz-wrapper-item" aria-live="polite"></div>
            <div id="${divId}-loading" class="viz-wrapper-item visually-hidden" aria-busy="true">
                <div class="viz-wrapper-loading">
                    <span id="${divId}-loading-message"></span>
                    <div class="viz-wrapper-loading-icon">
                        <img id="${divId}-loading-icon" src="<@hst.webfile  path="images/loading-circle.gif"/>" alt="Loading data " />
                    </div>
                </div>
            </div>
        </div>

        <div class="grid-row">
            <div class="column column--reset">
                <a target="_blank" class="block-link visually-hidden" id="${divId}-downLoadData" aria-live="polite" aria-atomic="true">
                    <div class="block-link__header">
                        <span class="icon icon--csv icon--download" aria-hidden="true"></span>
                    </div>
                    <div class="block-link__body">
                        <span class="block-link__title">Download the coronavirus data for <span id="${divId}-downLoadData-label"><!-- dynamic value  --></span></span>
                        <p class="cta__text">You can download the neighbourhood data as a .csv file.</p>
                    </div>
                </a>
            </div>
        </div>



        <script>
            var vizMessages = {
                LOAD_ERROR: "<@fmt.message key="load-error"/>",
                LOADING_MESSAGE: "<@fmt.message key="loading-message"/>",
                LOADING_MESSAGE_LONGER: "<@fmt.message key="loading-message-longer"/>",
                LOADING_FAILED_MESSAGE: "<@fmt.message key="loading-failed-message"/>"
            };
        </script>

        <script>
            // Viz instance
            var viz${index};

            // Helper varables
            var viz${index}Url;
            var viz${index}Loaded;
            var viz${index}LoadingTimerStart;
            var viz${index}LoadingRetryAtempIntervales;
            var viz${index}LoadingTimer;

            // Viz instance supporting HTML elements
            var viz${index}Elements = {
                vizDiv: function () {
                    return document.getElementById("${divId}-viz");
                },
                validationMessageDiv: function () {
                    return document.getElementById("${divId}-postcode-validation-message");
                },
                loadingDiv: function () {
                    return document.getElementById("${divId}-loading");
                },
                loadingIcon: function () {
                    return document.getElementById("${divId}-loading-icon");
                },
                loadingMessage: function (){
                    return document.getElementById("${divId}-loading-message");
                },
                postcode: function () {
                    return document.getElementById("${divId}-postcode");
                },
                distance: function () {
                    return document.getElementById("${divId}-distance");
                },
                vizLink: function (){
                    return document.getElementById('${divId}-downLoadData');
                },
                vizLinkLabel: function (){
                    return document.getElementById('${divId}-downLoadData-label');
                }
            };
            function loadViz() {
                function options() {
                    var options = {
                        "onFirstInteractive": function () {
                            viz${index}Load = true;
                            clearInterval(viz${index}LoadingTimer);
                            _hideLoadingSpinner();
                        },
                        hideTabs: ${section.hidetabs?string},
                        hideToolbar: true
                        <#if section.device??>
                        ,device: "${section.device}"
                        </#if>
                    };
                    if(options.device && options.device === "default"){
                        if(window.matchMedia("(min-width: 924px)").matches) {
                            options.device = "desktop";
                        }
                    }
                    return options;
                }
                if(typeof tableau !== 'undefined' && typeof tableau.Viz !== 'undefined') {
                    if(!!viz${index}) {
                        viz${index}.dispose();
                    }
                    viz${index} = new tableau.Viz(viz${index}Elements.vizDiv(), viz${index}Url, options());
                } else {
                    _fail();
                }
            }
            function lookup() {
                _hideLoadingSpinner();
                _clearValidationMessage(viz${index}Elements.validationMessageDiv());
                fetch(postcodeApiUrl(viz${index}Elements.postcode().value))
                    .then(response => {
                        if (!response.ok) {
                            // Rejected if not a 2xx response.
                            throw response;
                        }
                        return response.json()
                    })
                    .then(data => {
                        // Make Viz URL
                        var msoa = data["result"]["msoa"];
                        var postcode = data["result"]["postcode"];
                        var latitude = data["result"]["latitude"];
                        var longitude = data["result"]["longitude"];
                        viz${index}Url = encodeURI("${section.url}".split("?")[0] + "?MSOA Code=" + msoa + "&Lat=" + latitude + "&Lon=" + longitude + "&Distance=" + parseInt(viz${index}Elements.distance().value) + "&Postcode=" + postcode + "&:refresh=yes");

                        // Start Viz load
                        _hideLoadingSpinner();
                        loadViz();
                        _showLoadingSpinner();

                        // build the download link
                        _hideDownloadLink();
                        _showDownloadLink(encodeURI("${section.url}".split("?")[0]+".csv" + "?MSOA Code=" + msoa + "&Lat=" + latitude + "&Lon=" + longitude + "&Distance=" + parseInt(viz${index}Elements.distance().value) + "&Postcode=" + postcode + "&:refresh=yes"));

                        // Init loading retry
                        viz${index}Loaded = false;
                        viz${index}LoadingTimerStart = Date.now();
                        viz${index}LoadingRetryAtempIntervales = [<#list section.retryIntervals as intervale>${intervale},</#list>];
                        clearInterval(viz${index}LoadingTimer); <#-- if set -->
                        viz${index}LoadingTimer = setInterval(_retry, 1000);
                    }).catch(error => {
                        if (error.json !== undefined) {
                            error.json().then(data => {
                                if (!!viz${index}) {
                                    viz${index}.dispose();
                                }
                                if (data.hasOwnProperty('error')) {
                                    _showValidationMessage(viz${index}Elements.validationMessageDiv(), data["error"]);
                                } else {
                                    _showValidationMessage(viz${index}Elements.validationMessageDiv(), vizMessages.LOAD_ERROR);
                                }
                            });
                        } else {
                            _showValidationMessage(viz${index}Elements.validationMessageDiv(), vizMessages.LOAD_ERROR);
                        }
                });
            }
            function formatPostcode(postcode, divider) {
                if ((typeof postcode === 'string' || postcode instanceof String) && postcode.length >= 5) {
                    var p = postcode.replace(/\s/g, "");
                    p = p.toUpperCase();
                    return p.substr(0, p.length - 3) + divider + p.substr(p.length - 3);
                } else {
                    return null;
                }
            }
            function postcodeApiUrl(postcode) {
                return "<@fmt.message key="postcode-api-address"/>" + formatPostcode(postcode, "/") + ".json";
            }
            function _clearValidationMessage(el) {
                if(el instanceof HTMLElement) {
                    el.innerText = "";
                    el.classList.add("visually-hidden");
                }
            }
            function _showValidationMessage(el, message) {
                if(el instanceof HTMLElement) {
                    el.innerText = message;
                    el.classList.remove("visually-hidden");
                }
            }
            function _showDownloadLink(link) {
                var linkDiv = viz${index}Elements.vizLink();
                if (linkDiv instanceof HTMLElement) {
                    setTimeout(function() {
                        linkDiv.classList.remove("visually-hidden");
                    }, 1000);
                    linkDiv.setAttribute("href", link);
                }
                var linkLabel = viz${index}Elements.vizLinkLabel();
                if (linkLabel instanceof HTMLElement) {
                    linkLabel.innerText = formatPostcode(viz${index}Elements.postcode().value, " ");
                }
            }
            function _showLoadingSpinner() {
                var loaderDiv = viz${index}Elements.loadingDiv();
                if (loaderDiv instanceof HTMLElement) {
                    loaderDiv.classList.remove("visually-hidden");
                }
                var loaderIcon = viz${index}Elements.loadingIcon();
                if (loaderIcon instanceof HTMLElement) {
                    loaderIcon.classList.remove("visually-hidden");
                }
                var message = viz${index}Elements.loadingMessage();
                if (message instanceof HTMLElement) {
                    message.classList.add("viz-wrapper-loading-message");
                    _setMessage(vizMessages.LOADING_MESSAGE);
                }
            }
            function _hideDownloadLink() {
                var linkDiv = viz${index}Elements.vizLink();
                if (linkDiv instanceof HTMLElement) {
                    linkDiv.classList.add("visually-hidden");
                }
            }
            function _hideLoadingSpinner() {
                var loader = viz${index}Elements.loadingDiv();
                if (loader instanceof HTMLElement) {
                    loader.classList.add("visually-hidden");
                }
                var message = viz${index}Elements.loadingMessage();
                if (message instanceof HTMLElement) {
                    message.classList.remove("viz-wrapper-loading-message");
                }
            }
            function _setMessage(message) {
                var message${index} = viz${index}Elements.loadingMessage();
                if(!!(message${index})) {
                    message${index}.innerHTML = message;
                }
            }
            function _retry() {
                if(!viz${index}Loaded && viz${index}LoadingRetryAtempIntervales.length <= 0){
                    clearInterval(viz${index}LoadingTimer);
                    _fail();
                } else {
                    if(!viz${index}Loaded && (Date.now() - viz${index}LoadingTimerStart) > (viz${index}LoadingRetryAtempIntervales[0] * 1000)){
                        viz${index}LoadingTimerStart += viz${index}LoadingRetryAtempIntervales.shift() * 1000;  <#-- Update for the next round -->
                        _setMessage(vizMessages.LOADING_MESSAGE_LONGER);
                        loadViz();
                    }
                }
            }
            function _fail(){
                setTimeout(function(){
                    if(!viz${index}Loaded) {
                        var loading${index} = viz${index}Elements.loadingIcon();
                        if(!!(loading${index})) {
                            if (loading${index} instanceof HTMLElement) {
                                loading${index}.classList.add("visually-hidden");
                            }
                        }
                        _setMessage(vizMessages.LOADING_FAILED_MESSAGE);
                    }
                }, 60000); <#-- Allow Tableau last atempt to load finish before showing the fail message. -->
            }
        </script>
    </#if>
</#macro>
