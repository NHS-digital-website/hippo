<#ftl output_format="HTML">

<#-- Using test ID -->
<script type="text/javascript"> window.lpTag=window.lpTag||{};if(typeof window.lpTag._tagCount==='undefined'){window.lpTag={site:'35565217'||'',section:lpTag.section||'',autoStart:lpTag.autoStart===false?false:true,ovr:lpTag.ovr||{},_v:'1.6.0',_tagCount:1,protocol:'https:',events:{bind:function(app,ev,fn){lpTag.defer(function(){lpTag.events.bind(app,ev,fn);},0);},trigger:function(app,ev,json){lpTag.defer(function(){lpTag.events.trigger(app,ev,json);},1);}},defer:function(fn,fnType){if(fnType==0){this._defB=this._defB||[];this._defB.push(fn);}else if(fnType==1){this._defT=this._defT||[];this._defT.push(fn);}else{this._defL=this._defL||[];this._defL.push(fn);}},load:function(src,chr,id){var t=this;setTimeout(function(){t._load(src,chr,id);},0);},_load:function(src,chr,id){var url=src;if(!src){url=this.protocol+'//'+((this.ovr&&this.ovr.domain)?this.ovr.domain:'lptag.liveperson.net')+'/tag/tag.js?site='+this.site;}var s=document.createElement('script');s.setAttribute('charset',chr?chr:'UTF-8');if(id){s.setAttribute('id',id);}s.setAttribute('src',url);document.getElementsByTagName('head').item(0).appendChild(s);},init:function(){this._timing=this._timing||{};this._timing.start=(new Date()).getTime();var that=this;if(window.attachEvent){window.attachEvent('onload',function(){that._domReady('domReady');});}else{window.addEventListener('DOMContentLoaded',function(){that._domReady('contReady');},false);window.addEventListener('load',function(){that._domReady('domReady');},false);}if(typeof(window._lptStop)=='undefined'){this.load();}},start:function(){this.autoStart=true;},_domReady:function(n){if(!this.isDom){this.isDom=true;this.events.trigger('LPT','DOM_READY',{t:n});}this._timing[n]=(new Date()).getTime();},vars:lpTag.vars||[],dbs:lpTag.dbs||[],ctn:lpTag.ctn||[],sdes:lpTag.sdes||[],ev:lpTag.ev||[]};lpTag.init();}else{window.lpTag._tagCount+=1;}window.cafexAssistBootstrap={};window.cafexAssistBootstrap.laBootstrapLoaded=!1;window.cafexAssistBootstrap.cobrowseState;window.cafexAssistBootstrap.bootstrapLa=function(cobrowseServer){var oldScript=document.getElementById("assist-cobrowse-bootstrap-script");if(oldScript){oldScript.parentNode.removeChild(oldScript)}var laBootstrapJs=cobrowseServer+"/assist-bootstrap/assist-bootstrap.js";var script=document.createElement("script");script.id="assist-cobrowse-bootstrap-script";script.type="text/javascript";script.src=laBootstrapJs;window.cafexAssistBootstrap.cobrowseBootstrapLoaded=function(){window.cafexAssistBootstrap.laBootstrapLoaded=!0;switch(window.cafexAssistBootstrap.cobrowseState){case "accepted":window.cafexAssistBootstrap.cobrowseAccepted(window.cafexAssistBootstrap.agentId);break;case "rejected":window.cafexAssistBootstrap.cobrowseRejected(window.cafexAssistBootstrap.agentId);break}};document.head.appendChild(script)};if(sessionStorage.getItem("cobrowseServer")){window.cafexAssistBootstrap.bootstrapLa(sessionStorage.getItem("cobrowseServer"))}lpTag.events.bind({eventName:"cobrowseOffered",appName:"*",func:function(event){console.log(event);window.cafexAssistBootstrap.cobrowseState="offered";var elements=document.querySelectorAll('[data-assist-a'+event.agentId+']');var data=JSON.parse(atob(elements[elements.length-1].dataset['assistA'+event.agentId]));console.log(data);var serverUrl=document.createElement("a");serverUrl.href=data.server;var whitelistedRootDomains=[".cafex.com",".liveassistcloud.com",".liveassistfor365.com"];var domainWhitelisted=!1;var hostname=serverUrl.hostname;for(var i=0;i<whitelistedRootDomains.length;i++){var expectedRootDomain=whitelistedRootDomains[i];var index=hostname.indexOf(expectedRootDomain);if(index!=-1||index==(hostname.length-expectedRootDomain.length)){domainWhitelisted=!0;break}}if(!domainWhitelisted){console.log("Unexpected cobrowse domain "+hostname+" aborting cobrowse.");return}sessionStorage.setItem("cobrowseServer",data.server);window.cafexAssistBootstrap.bootstrapLa(data.server)},async:!0});lpTag.events.bind({eventName:"cobrowseAccepted",appName:"*",func:function(event){console.log(event);window.cafexAssistBootstrap.cobrowseState="accepted";window.cafexAssistBootstrap.agentId=event.agentId;if(window.cafexAssistBootstrap.laBootstrapLoaded){window.cafexAssistBootstrap.cobrowseAccepted(event.agentId)}},async:!0});lpTag.events.bind({eventName:"cobrowseDeclined",appName:"*",func:function(event){console.log(event);window.cafexAssistBootstrap.cobrowseState="rejected";window.cafexAssistBootstrap.agentId=event.agentId;if(window.cafexAssistBootstrap.laBootstrapLoaded){window.cafexAssistBootstrap.cobrowseRejected(event.agentId)}},async:!0})
</script>
