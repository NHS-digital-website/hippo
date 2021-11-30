<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<@hst.setBundle basename="ie.banner"/>

<#if showBanner?? && showBanner>
    <article id="ieBanner" class="ie-banner">
        <div class="grid-wrapper grid-wrapper--collapse" aria-labelledby="ie-banner-title" role="banner" tabindex="-1">
            <div class="grid-row">
                <div class="column column--two-thirds column--reset">
                    <div class="article-header article-header--secondary">
                        <h2 class="ie-banner__title" id="ie-banner-title"><@fmt.message key="title"/></h2>
                    </div>
                </div>
            </div>

            <div class="grid-row">
                <div class="column column--reset">
                    <div id="ie-banner-message" class="article-section article-section--summary no-border">
                        <div class="grid-row">
                            <div class="column column--reset">
                                <div class="rich-text-content">
                                    <p><@fmt.message key="message"/></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </article>

    <script>
        'use strict';

        var _UA = window.navigator.userAgent;
        var ua = _UA.toLowerCase();

        // test regex against user agent
        if(/(trident|msie|iemobile|ie)/g.test(ua)) {
            document.getElementById('ieBanner').classList.add('ie-banner--is-visible');
        } else {
            document.getElementById('ieBanner').classList.remove('ie-banner--is-visible');
        }
    </script>
</#if>

