<#ftl output_format="HTML">

<@hst.setBundle basename="design-system"/>
<@fmt.message key="design-system.url" var="designSystemUrl" />

<script data-cookieconsent="preferences,statistics" type="text/plain">(function(t){window.lpTag=window.lpTag||{};if(typeof window.lpTag._tagCount==="undefined"){window.lpTag={site:t,section:lpTag.section||"",autoStart:lpTag.autoStart===false?false:true,ovr:lpTag.ovr||{},_v:"1.6.0",_tagCount:1,protocol:"https:",events:{bind:function(t,e,o){lpTag.defer(function(){lpTag.events.bind(t,e,o)},0)},trigger:function(t,e,o){lpTag.defer(function(){lpTag.events.trigger(t,e,o)},1)}},defer:function(t,e){if(e==0){this._defB=this._defB||[];this._defB.push(t)}else if(e==1){this._defT=this._defT||[];this._defT.push(t)}else{this._defL=this._defL||[];this._defL.push(t)}},load:function(t,e,o){var s=this;setTimeout(function(){s._load(t,e,o)},0)},_load:function(t,e,o){var s=t;if(!t){s=this.protocol+"//"+(this.ovr&&this.ovr.domain?this.ovr.domain:"lptag.liveperson.net")+"/tag/tag.js?site="+this.site}var n=document.createElement("script");n.setAttribute("charset",e?e:"UTF-8");if(o){n.setAttribute("id",o)}n.setAttribute("src",s);document.getElementsByTagName("head").item(0).appendChild(n)},init:function(){this._timing=this._timing||{};this._timing.start=(new Date).getTime();var t=this;if(window.attachEvent){window.attachEvent("onload",function(){t._domReady("domReady")})}else{window.addEventListener("DOMContentLoaded",function(){t._domReady("contReady")},false);window.addEventListener("load",function(){t._domReady("domReady")},false)}if(typeof window._lptStop=="undefined"){this.load()}},start:function(){this.autoStart=true},_domReady:function(t){if(!this.isDom){this.isDom=true;this.events.trigger("LPT","DOM_READY",{t:t})}this._timing[t]=(new Date).getTime()},vars:lpTag.vars||[],dbs:lpTag.dbs||[],ctn:lpTag.ctn||[],sdes:lpTag.sdes||[],ev:lpTag.ev||[]};lpTag.init()}else{window.lpTag._tagCount+=1}window.cafexAssistBootstrap={};window.cafexAssistBootstrap.laBootstrapLoaded=false;window.cafexAssistBootstrap.cobrowseState;window.cafexAssistBootstrap.bootstrapLa=function(t){var e=document.getElementById("assist-cobrowse-bootstrap-script");if(e){e.parentNode.removeChild(e)}var o=t+"/assist-bootstrap/assist-bootstrap.js";var s=document.createElement("script");s.id="assist-cobrowse-bootstrap-script";s.src=o;window.cafexAssistBootstrap.cobrowseBootstrapLoaded=function(){window.cafexAssistBootstrap.laBootstrapLoaded=true;switch(window.cafexAssistBootstrap.cobrowseState){case"accepted":window.cafexAssistBootstrap.cobrowseAccepted(window.cafexAssistBootstrap.agentId);break;case"rejected":window.cafexAssistBootstrap.cobrowseRejected(window.cafexAssistBootstrap.agentId);break}};document.head.appendChild(s)};if(sessionStorage.getItem("cobrowseServer")){window.cafexAssistBootstrap.bootstrapLa(sessionStorage.getItem("cobrowseServer"))}lpTag.events.bind({eventName:"cobrowseOffered",appName:"*",func:function(t){console.log(t);window.cafexAssistBootstrap.cobrowseState="offered";var e=document.querySelectorAll("[data-assist-a"+t.agentId+"]");var o=JSON.parse(atob(e[e.length-1].dataset["assistA"+t.agentId]));console.log(o);if(y(o.server)===false){return}sessionStorage.setItem("cobrowseServer",o.server);window.cafexAssistBootstrap.bootstrapLa(o.server)},async:true});lpTag.events.bind({eventName:"cobrowseAccepted",appName:"*",func:function(t){console.log(t);window.cafexAssistBootstrap.cobrowseState="accepted";window.cafexAssistBootstrap.agentId=t.agentId;if(window.cafexAssistBootstrap.laBootstrapLoaded){window.cafexAssistBootstrap.cobrowseAccepted(t.agentId)}},async:true});lpTag.events.bind({eventName:"cobrowseDeclined",appName:"*",func:function(t){console.log(t);window.cafexAssistBootstrap.cobrowseState="rejected";window.cafexAssistBootstrap.agentId=t.agentId;if(window.cafexAssistBootstrap.laBootstrapLoaded){window.cafexAssistBootstrap.cobrowseRejected(t.agentId)}},async:true});function e(t,e){let s=o(t,e);document.dispatchEvent(s)}function o(t,e){let o;if(typeof window.CustomEvent==="function"){o=new CustomEvent(t,e)}else{let s=e||{bubbles:false,cancelable:false,detail:undefined};o=document.createEvent("CustomEvent");o.initCustomEvent(t,s.bubbles,s.cancelable,s.detail)}return o}document.addEventListener("click",function(t){if(t.target.dataset&&t.target.dataset.event!==undefined){try{let o=JSON.parse(t.target.dataset.event);let s=p(o.useRecentData,t.target.parentElement.dataset.bootStrap);d(o);r(o);h(t.target.parentElement.dataset.bootStrap,function(t){u(t);e(o.event,s)})}catch(e){console.log("could not process"+t.target.dataset.event+" "+e)}}});lpTag.events.bind({eventName:"state",appName:"*",func:function(t){if(t.state==="ended"){i()}},async:true});const s="liveAssistScripts";const n="liveAssistElements";const a="deleteLiveAssistScript";function i(){var t=document.querySelectorAll("[data-event]");t.forEach(function(t){let e=JSON.parse(t.dataset.event);d(e,true)})}function d(t,e){let o=document.getElementById(t.id);if(t.oneTime||e){o.disabled=true;if(typeof t.disabledStyle!=="undefined"){o.style.cssText=t.disabledStyle}o.style.cssText+="pointer-events: none;"}}function r(t){const e="element already exists in local storage list";g(sessionStorage,t,e,n)}function c(){let t=JSON.parse(sessionStorage.getItem(n));let e=[];for(let o in t){if(t[o].oneTime){e.push(f(t[o]))}}if(e.length!==0){l(e)}}function l(t){let e=document.head||document.getElementsByTagName("head")[0];let o=document.createElement("style");o.type="text/css";t.forEach(function(t){o.appendChild(document.createTextNode(t))});e.appendChild(o)}function f(t){let e="#"+t.id+"{";let o=t.disabledStyle.split(";");o.forEach(function(t){e+=t+" !important;"});e+="pointer-events: none;";e+="}";return e}function p(t,e){let o={detail:{data:e}};if(t){let t=JSON.parse(e).id;let s=document.querySelectorAll("[data-boot-strap]");for(let e=s.length-1;e>0;e--){let n=s[e].dataset["bootStrap"];if(JSON.parse(n).id===t){o={detail:{data:n}};break}}}return o}function u(t){const e="script already exists in local storage list";g(localStorage,t,e,s)}function g(t,e,o,s){let n=JSON.parse(t.getItem(s));if(!n){n={}}if(n[e.id]===undefined){n[e.id]=e;t.setItem(s,JSON.stringify(n));console.log("adding"+e.id+" to storage")}else{console.log(o)}}function m(t){w(localStorage,t,s)}function w(t,e,o){let s=JSON.parse(t.getItem(o));if(delete s[e]===false){console.log("script"+e+"does not exist in list")}t.setItem(o,JSON.stringify(s))}function v(t){let e=document.getElementById(t);if(e){e.parentNode.removeChild(e);e=null;console.log("script"+t+" removed from dom")}}function b(t,e){let o=document.getElementById(t.id);if(y(t.bootstrapJs)===false){console.log("script not in valid domain");return}if(o){o.parentNode.removeChild(o);o=null}o=document.createElement("script");o.id=t.id;o.async=true;o.onload=function(){e(t)};o.src=t.bootstrapJs;document.body.appendChild(o)}function h(t,e){let o=JSON.parse(t);if(o){b(o,e);return true}return false}function S(){let t=JSON.parse(localStorage.getItem(s));console.log("restoring scripts using local storage");for(let e in t){b(t[e],function(){console.log("script"+e+"restored")})}}document.addEventListener(a,function(t){v(t.detail.id);m(t.detail.id)},false);document.addEventListener("DOMContentLoaded",function(t){S();c()});function y(t){const e=[".cafex.com",".liveassistcloud.com",".liveassistfor365.com"];let o=false;let s=document.createElement("a");s.href=t;let n=s.hostname;for(let t=0;t<e.length;t++){let s=e[t];let a=n.indexOf(s);if(a!=-1||a==n.length-s.length){o=true;break}}if(!o){console.log("Unexpected domain "+n+" aborting...")}return o}})(6368614);</script>
<script data-cookieconsent="preferences,statistics" type="text/plain">(function(){function ShortCodeService(serviceUrl,orgId){let pendingPoll;function injectBootstrapScript(server,force){sessionStorage.setItem("cobrowseServer",server);let id="assist-cobrowse-bootstrap-script";let element=document.getElementById(id);if(element&&element!==null){if(!force){return}element.parentNode.removeChild(element)}let script=document.createElement("script");script.id=id;script.src=server+"/assist-bootstrap/assist-bootstrap.js";document.body.appendChild(script)}function startCobrowse(shortCodeData,successCallback){let laNodeUrl=shortCodeData.laNodeUrl;let laCorrelator=shortCodeData.laCorrelator;let laToken=shortCodeData.laToken;let laBootstrapVersion=shortCodeData.bootstrapVersion;window.cafexAssistBootstrap={};window.cafexAssistBootstrap.cobrowseBootstrapLoaded=function(){AssistSDK.startSupport({url:laNodeUrl,sessionToken:laToken,correlationId:laCorrelator,sdkPath:laNodeUrl+"/assistserver/sdk/web/consumer"});successCallback()};let bootstrapUrl=laNodeUrl+"/assist-bootstrap-"+laBootstrapVersion;injectBootstrapScript(bootstrapUrl,true)}function poll(shortCodeData,successCallback,failureCallback){pendingPoll=setTimeout(function(){let xhr=new XMLHttpRequest;let url=serviceUrl+"/assist-cloud-lb/visitorShortCode?orgId="+orgId+"&shortCode="+shortCodeData.shortCode+"&accessKey="+shortCodeData.shortCodeAccessKey;xhr.ontimeout=function(e){failureCallback("requestShortCode timeout",event.target.status,event.target)};xhr.addEventListener("load",function(event){if(event.target.response&&event.target.status==200){let shortCodeData=JSON.parse(event.target.response);startCobrowse(shortCodeData,successCallback)}else if(event.target.status==204){poll(shortCodeData,successCallback,failureCallback)}else{failureCallback("Short code timed out or was removed",event.target.status,event.target)}});xhr.addEventListener("error",function(event){failureCallback("Error polling for co-browse data",event.target.status,event.target)});xhr.open("GET",url);xhr.timeout=1e4;xhr.send()},2e3)}this.requestShortCode=function(shortCodeCallback,successCallback,failureCallback){if(typeof shortCodeCallback!="function"){throw"A function to handle the short code must be provided as the first argument."}if(shortCodeCallback.length<1){throw"The short code callback (first argument) must itself take an argument to receive the generated short code."}console.log("Requesting shortcode for org: "+orgId);let xhr=new XMLHttpRequest;let url=serviceUrl+"/assist-cloud-lb/visitorShortCode?orgId="+orgId;xhr.ontimeout=function(event){failureCallback("Timed out requesting short code",event.target.status,event.target)};xhr.addEventListener("load",function(event){if(event.target.response&&event.target.status==200){let shortCodeData=JSON.parse(event.target.response);shortCodeCallback(shortCodeData.shortCode);poll(shortCodeData,successCallback,failureCallback)}else{failureCallback("Failed requesting short code",event.target.status,event.target)}});xhr.addEventListener("error",function(event){failureCallback("Failed sending short code request",event.target.status,event.target)});xhr.open("POST",url);xhr.timeout=1e4;xhr.send()};this.cancel=function(){clearTimeout(pendingPoll)};window.addEventListener("load",function(){let serverUrl=sessionStorage.getItem("cobrowseServer");if(serverUrl){injectBootstrapScript(serverUrl,false)}})}window.LAShortCodeService = new ShortCodeService('https://service.eu1.liveassistfor365.com','ec7a4ce1-9ca7-4550-b012-80e4f48a01b4');})();</script>

<script defer src="<@hst.webfile path="/dist/nhsd-frontend-scripts.bundle.js"/>"></script>
<script defer src="${designSystemUrl}/cdn/v0.167.0/scripts/nhsd-frontend.js"></script>
