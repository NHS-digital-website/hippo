<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">


<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Calltoaction" -->
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Banner" -->
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Video" -->

<@hst.setBundle basename="rb.generic.texts"/>
<@fmt.message key="text.sr-only-link" var="srOnlyLinkText" />

<#assign hasDocument = document?has_content />

<#-- Size -->
<#assign isTall = size == "tall" />
<#assign accented = isTall?then("nhsd-o-hero-feature--accented","")/>
<#assign headingSize = isTall?then("nhsd-t-heading-xxl","nhsd-t-heading-l")/>
<#assign colSize = isTall?then("nhsd-t-col-xs-12","nhsd-t-col-xs-11")/>
<#assign marginSize = isTall?then("nhsd-!t-margin-bottom-9", "nhsd-!t-margin-bottom-0") />
<#assign imageSize = isTall?then("nhsd-a-image--square","")/>

<#-- Alignment -->
<#assign hasTextAlignment = textAlignment?has_content />
<#assign mirrored = (hasTextAlignment && textAlignment == "right")?then("nhsd-o-hero-feature--mirrored","")/>

<#assign breadcrumb  = hstRequestContext.getAttribute("bread")>

<#if (breadcrumb=="Coronavirus" || breadcrumb=="News") && !hstRequestContext.getAttribute("headerPresent")?if_exists>
    <#assign overridePageTitle>${breadcrumb}</#assign>
    <@metaTags></@metaTags>
</#if>

<#-- Colourbar -->
<#assign hasColourBar = isTall?then(displayColourBar, false) />

<#if hasDocument>
    <#assign isCtaDoc = document.class.simpleName?starts_with("Calltoaction") />
    <#assign isBannerDoc = document.class.simpleName?starts_with("Banner") />
    <#assign isVideoDoc = document.class.simpleName?starts_with("Video") />

    <#assign hasTitle = document.title?has_content />
    <#assign hasContent = document.content?has_content || document.introduction?has_content />
    <#assign hasLink = document.external?has_content || document.internal?has_content || document.link?has_content />
    <#assign hasImage = document.image?has_content />
    <#assign hasImageDesc = hasImage && isCtaDoc?then(document.altText?has_content, document.image.description?has_content) />
    <#assign altText = hasImageDesc?then(isCtaDoc?then(document.altText, document.image.description), isTall?then("image of the main hero image", "image of the secondary hero image")) />
    <#assign isDecorativeOnly = isCtaDoc && document.isDecorative?if_exists == "true" />
    
    <div class="nhsd-o-hero-feature ${accented} ${mirrored}">
        <div class="nhsd-t-grid nhsd-t-grid--full-width nhsd-!t-no-gutters">
            <div class="nhsd-t-row nhsd-t-row--centred">
                <div class="nhsd-t-col-s-6 nhsd-!t-no-gutters ${colSize}">
                    <div class="nhsd-o-hero__content-box">
                        <div class="nhsd-o-hero__content">
                            <#if hasTitle>
                                <#if hstRequestContext.getAttribute("headerPresent")?if_exists>
                                        <h2 class="${headingSize}">${document.title}</h2>
                                    <#else>
                                        <h1 class="${headingSize}">${document.title}</h1>
                                 </#if>
                            </#if>
                            <#if hasContent>
                                <p class="nhsd-t-body nhsd-!t-margin-bottom-6">
                                    <#if isBannerDoc>
                                        <@hst.html hippohtml=document.content var="summaryText" />
                                        ${summaryText?replace('<[^>]+>','','r')}
                                    <#elseif isCtaDoc>
                                        ${document.content}
                                    <#elseif isVideoDoc>
                                        ${document.getShortsummary()}
                                    </#if>
                                </p>
                            </#if>

                            <#if hasLink>
                                <#if document.internal?has_content>
                                    <@hst.link hippobean=document.internal var="link"/>
                                    <#assign linkLabel = document.label />
                                <#elseif document.external?has_content>
                                    <#assign link = document.external/>
                                    <#assign linkLabel = document.label />
                                <#elseif document.link?has_content>
                                    <@hst.link hippobean=document.link var="link"/>
                                    <#assign linkLabel = document.link.title />
                                </#if>

                                <#if linkLabel?has_content>
                                    <a class="nhsd-a-button ${marginSize}" href="${link}">
                                    <span class="nhsd-a-button__label">${linkLabel}</span>

                                    <#if document.external?has_content>
                                        <span class="nhsd-t-sr-only">${srOnlyLinkText}</span>
                                    </#if>
                                    </a>
                                </#if>
                            </#if>
                        </div>

                        <#if hasColourBar>
                            <span class="nhsd-a-colour-bar"></span>
                        </#if>
                    </div>
                </div>

                <div class="nhsd-t-col-s-6 nhsd-!t-no-gutters ${colSize}">
                    <figure class="nhsd-a-image ${imageSize}" aria-hidden="true">
                        <#if isVideoDoc && document.videoUri???has_content>
                            <style>.hero-video{padding-bottom:56.25%; position:relative}.hero-video.ended::after{content:"";position:absolute;top:0;left:0;bottom:0;right:0;cursor:pointer;background-color:black;background-repeat:no-repeat;background-position:center;background-size:64px 64px;background-image:url(data:image/svg+xml;utf8;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxMjgiIGhlaWdodD0iMTI4IiB2aWV3Qm94PSIwIDAgNTEwIDUxMCI+PHBhdGggZD0iTTI1NSAxMDJWMEwxMjcuNSAxMjcuNSAyNTUgMjU1VjE1M2M4NC4xNSAwIDE1MyA2OC44NSAxNTMgMTUzcy02OC44NSAxNTMtMTUzIDE1My0xNTMtNjguODUtMTUzLTE1M0g1MWMwIDExMi4yIDkxLjggMjA0IDIwNCAyMDRzMjA0LTkxLjggMjA0LTIwNC05MS44LTIwNC0yMDQtMjA0eiIgZmlsbD0iI0ZGRiIvPjwvc3ZnPg==)}.hero-video.paused::after{content:"";position:absolute;top:70px;left:0;bottom:50px;right:0;cursor:pointer;background-color:black;background-repeat:no-repeat;background-position:center;background-size:40px 40px;background-image:url(data:image/svg+xml;utf8;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZlcnNpb249IjEiIHdpZHRoPSIxNzA2LjY2NyIgaGVpZ2h0PSIxNzA2LjY2NyIgdmlld0JveD0iMCAwIDEyODAgMTI4MCI+PHBhdGggZD0iTTE1Ny42MzUgMi45ODRMMTI2MC45NzkgNjQwIDE1Ny42MzUgMTI3Ny4wMTZ6IiBmaWxsPSIjZmZmIi8+PC9zdmc+)}</style>
                            <div class="hero-video-outer-wrapper">
                                <div class="hero-video">
                                    <iframe style="width:100%; height:100%; position:absolute" src="${document.videoUri}&rel=0&enablejsapi=1" allow="autoplay; encrypted-media; picture-in-picture" allowfullscreen></iframe>
                                </div>
                            </div>
                            <script>"use strict";document.addEventListener('DOMContentLoaded',function(){if(window.hideYTActivated)return;if(typeof YT==='undefined'){let tag=document.createElement('script');tag.src="https://www.youtube.com/iframe_api";let firstScriptTag=document.getElementsByTagName('script')[0];firstScriptTag.parentNode.insertBefore(tag,firstScriptTag);} let onYouTubeIframeAPIReadyCallbacks=[];for(let playerWrap of document.querySelectorAll(".hero-video")){let playerFrame=playerWrap.querySelector("iframe");let onPlayerStateChange=function(event){if(event.data==YT.PlayerState.ENDED){playerWrap.classList.add("ended");}else if(event.data==YT.PlayerState.PAUSED){playerWrap.classList.add("paused");}else if(event.data==YT.PlayerState.PLAYING){playerWrap.classList.remove("ended");playerWrap.classList.remove("paused");}};let player;onYouTubeIframeAPIReadyCallbacks.push(function(){player=new YT.Player(playerFrame,{events:{'onStateChange':onPlayerStateChange}});});playerWrap.addEventListener("click",function(){let playerState=player.getPlayerState();if(playerState==YT.PlayerState.ENDED){player.seekTo(0);}else if(playerState==YT.PlayerState.PAUSED){player.playVideo();}});} window.onYouTubeIframeAPIReady=function(){for(let callback of onYouTubeIframeAPIReadyCallbacks){callback();}};window.hideYTActivated=true;});</script>
                        <#elseif document.image?has_content>
                            <picture class="nhsd-a-image__picture">
                                <@hst.link hippobean=document.image var="image" />
                                <img src="${image}" alt="<#if !isDecorativeOnly>${altText}</#if>">
                            </picture>
                        <#else>
                            <picture class="nhsd-a-image__picture">
                                <img src="https://digital.nhs.uk/binaries/content/gallery/website/about-nhs-digital/fibre_57101102_med.jpg" alt="<#if !isDecorativeOnly>${altText}</#if>">
                            </picture>
                        </#if>
                    </figure>
                </div>
            </div>
        </div>
    </div>
</#if>
