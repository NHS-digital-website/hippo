<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<@hst.setBundle basename="ie.banner"/>

<#if showBanner?? && showBanner>
    <!--googleoff: index-->
    <article id="ieBanner" class="nhsd-!t-bg-accessible-red nhsd-!t-display-hide">
        <div class="nhsd-a-box nhsd-!t-bg-accessible-red nhsd-!t-col-white nhsd-!t-padding-bottom-7" aria-labelledby="ie-banner-title" role="banner" tabindex="-1">
            <h1 id="ie-banner-title"><@fmt.message key="title"/></h1>
            <p><@fmt.message key="message"/></p>
        </div>
    </article>
    <script>
        'use strict';

        var _UA = window.navigator.userAgent;
        var ua = _UA.toLowerCase();

        // test regex against user agent
        if(/(trident|msie|iemobile|ie)/g.test(ua)) {
            document.getElementById('ieBanner').classList.remove('nhsd-!t-display-hide');
        }
    </script>
    <!--googleon: index-->
</#if>

