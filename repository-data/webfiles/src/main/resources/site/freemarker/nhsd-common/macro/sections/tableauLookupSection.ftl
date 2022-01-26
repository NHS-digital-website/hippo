<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">
<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.TableauLookup" -->

<#macro tableauLookup section index>

    <@hst.setBundle basename="tableau.postcode.labels, tableau.lables, tableau.postcode.configuration"/>

   <#if section?? && section.url??>

       <@hst.headContribution category="htmlBodyEnd">
            <script src="${section.tableauBase}javascripts/api/tableau-2.min.js"></script>
        </@hst.headContribution>

        <#assign divId = "tableau-${index}"/>

       <form action="javascript:void(0);" class="nhsd-t-form nhsd-!t-margin-top-6" novalidate="" autocomplete="off">
           <div class="nhsd-t-form-group" style="max-width: 300px">
               <label for="${divId}-postcode" class="nhsd-t-form-label">
                   <@fmt.message key="postcode-label"/>
               </label>
               <span class="nhsd-t-form-hint"><@fmt.message key="postcode-hint"/></span>
               <input type="text" id="${divId}-postcode" class="nhsd-t-form-input" autocomplete="off" />
               <div id="${divId}-postcode-validation-message" class="nhsd-t-form-error nhsd-!t-display-hide"></div>
           </div>
           <div class="nhsd-t-form-group" style="max-width: 300px">
               <label for="${divId}-distance" class="nhsd-t-form-label">
                   <@fmt.message key="distance-label"/>
               </label>
               <span class="nhsd-t-form-hint"><@fmt.message key="distance-hint"/></span>
               <div class="nhsd-t-form-control">
                   <select id="${divId}-distance" style="margin-top: 8px;"
                           class="nhsd-t-form-select" type="text">
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
                   <span class="nhsd-t-form-control__icon">
                        <span class="nhsd-a-icon nhsd-a-icon--size-s">
                              <svg xmlns="http://www.w3.org/2000/svg"
                                   preserveAspectRatio="xMidYMid meet"
                                   aria-hidden="true"
                                   focusable="false" viewBox="0 0 16 16"
                                   width="100%"
                                   height="100%">
                                    <path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"></path>
                              </svg>
                        </span>
                  </span>
               </div>
           </div>
           <button class="nhsd-a-button" type="button" onclick="lookup()">
               <span class="nhsd-a-button__label">Go</span>
           </button>
       </form>

       <div id="${divId}" class="viz-wrapper">
           <div id="${divId}-viz" class="viz-wrapper-item" aria-live="polite"></div>
           <div id="${divId}-loading" class="viz-wrapper-item nhsd-!t-display-hide" aria-busy="true">
               <div class="viz-wrapper-loading">
                   <span id="${divId}-loading-message"></span>
                   <div class="viz-wrapper-loading-icon">
                       <img id="${divId}-loading-icon" src="<@hst.webfile  path="images/loading-circle.gif"/>" alt="Loading data " />
                   </div>
               </div>
           </div>
       </div>

       <div class="nhsd-m-download-card nhsd-!t-margin-top-4 nhsd-!t-margin-bottom-6 nhsd-!t-display-hide">
           <a id="${divId}-downLoadData" class="nhsd-a-box-link" rel="external" aria-live="polite" aria-atomic="true">
               <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                   <div class="nhsd-m-download-card__image-box">
                       <span class="nhsd-a-document-icon">
                           <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet"
                                 aria-hidden="true" focusable="false" viewBox="0 0 39 46"><polygon
                                        points="30,9 39,9 39,9 30.1,0 30,0" fill="#006646"></polygon>
                              <path d="M28,11V0H3C1.4,0,0,1.3,0,3v40c0,1.7,1.3,3,3,3h33c1.7,0,3-1.3,3-3V11L28,11z"
                                    fill="#006646"></path>
                              <path d="M8,13v5h0v2h0v5h23V13H8z M29,18h-5v-3h5V18z M17,18v-3h5v3H17z M22,20v3h-5v-3H22z M10,15h5v3h-5V15z M10,20h5 v3h-5V20z M24,23v-3h5v3H24z"
                                    fill="#FFFFFF"></path>
                              <path d="M29,29H10c-1.7,0-3,1.3-3,3v6c0,1.7,1.3,3,3,3h19c1.7,0,3-1.3,3-3v-6C32,30.3,30.7,29,29,29z M14.2,39.1	c-2.5,0-4.2-1.2-4.2-3.8c0-2.6,1.6-4.1,4.2-4.1c0.5,0,1.2,0.1,1.7,0.3l-0.1,1.3c-0.6-0.3-1.1-0.4-1.7-0.4c-1.6,0-2.6,1.2-2.6,2.8 c0,1.6,1,2.7,2.6,2.7c0.6,0,1.3-0.1,1.7-0.3l0.1,1.3C15.4,39,14.8,39.1,14.2,39.1z M18.9,39.1c-0.8,0-1.2-0.1-2-0.3l0.1-1.4 c0.5,0.3,1.1,0.5,1.7,0.5c0.6,0,1.4-0.3,1.4-1c0-1.5-3.4-0.9-3.4-3.4c0-1.7,1.3-2.3,2.7-2.3c0.7,0,1.3,0.1,1.8,0.3l-0.1,1.3 c-0.5-0.2-1-0.3-1.6-0.3c-0.5,0-1.2,0.2-1.2,1.1c0,1.3,3.4,0.8,3.4,3.3C21.8,38.5,20.4,39.1,18.9,39.1z M26.9,39H25l-2.6-7.7H24l2,6 h0l1.9-6h1.5L26.9,39z"
                                    fill="#FFFFFF"></path>
                           </svg>
                       </span>
                   </div>
                   <div class="nhsd-m-download-card__content-box">
                       <p class="nhsd-t-heading-s">Download the coronavirus data for <span id="${divId}-downLoadData-label"><!-- dynamic value  --></span></p>
                       <p class="nhsd-t-body-s">You can download the neighbourhood data as a .csv file.</p>
                       <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-arrow--right nhsd-a-icon--size-s">
                           <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet"
                               aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="100%"
                               height="100%">
                            <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                           </svg>
                        </span>
                   </div>
               </div>
           </a>
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
                        if(!!viz${index}) {
                            viz${index}.dispose();
                        }
                        if(data.hasOwnProperty('error')){
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
                    el.classList.remove("nhsd-t-form-error");
                    el.classList.add("nhsd-!t-display-hide");
                    el.parentElement.classList.remove("nhsd-t-form-group--error");
                }
            }

            function _showValidationMessage(el, message) {
                if(el instanceof HTMLElement) {
                    el.innerText = message;
                    el.classList.add("nhsd-t-form-error");
                    el.classList.remove("nhsd-!t-display-hide");
                    el.parentElement.classList.add("nhsd-t-form-group--error");
                }
            }
            function _showDownloadLink(link) {
                var linkEl = viz${index}Elements.vizLink();
                if (linkEl instanceof HTMLElement) {
                    setTimeout(function() {
                        linkEl.parentElement.classList.remove("nhsd-!t-display-hide");
                    }, 1000);
                    linkEl.setAttribute("href", link);
                }
                var linkLabel = viz${index}Elements.vizLinkLabel();
                if (linkLabel instanceof HTMLElement) {
                    linkLabel.innerText = formatPostcode(viz${index}Elements.postcode().value, " ");
                }
            }
            function _showLoadingSpinner() {
                var loaderDiv = viz${index}Elements.loadingDiv();
                if (loaderDiv instanceof HTMLElement) {
                    loaderDiv.classList.remove("nhsd-!t-display-hide");
                }
                var loaderIcon = viz${index}Elements.loadingIcon();
                if (loaderIcon instanceof HTMLElement) {
                    loaderIcon.classList.remove("nhsd-!t-display-hide");
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
                    linkDiv.parentElement.classList.add("nhsd-!t-display-hide");
                }
            }
            function _hideLoadingSpinner() {
                var loader = viz${index}Elements.loadingDiv();
                if (loader instanceof HTMLElement) {
                    loader.classList.add("nhsd-!t-display-hide");
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
                                loading${index}.classList.add("nhsd-!t-display-hide");
                            }
                        }
                        _setMessage(vizMessages.LOADING_FAILED_MESSAGE);
                    }
                }, 60000); <#-- Allow Tableau last atempt to load finish before showing the fail message. -->
            }
        </script>
    </#if>
</#macro>
