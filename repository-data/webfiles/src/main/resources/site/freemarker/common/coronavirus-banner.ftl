<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<@hst.setBundle basename="coronavirus.banner"/>

<#if showBanner?? && showBanner>
    <!--googleoff: index-->
    <article>
        <header class="coronavirus-banner" aria-labelledby="message-summary-title" tabindex="-1">
            <div class="grid-wrapper grid-wrapper--collapse">
                <div class="grid-row">
                    <div class="column column--two-thirds column--reset">
                        <div class="article-header article-header--secondary">
                            <span class="coronavirus-banner__title" id="message-summary-title"><@fmt.message key="title"/></span>
                        </div>
                    </div>
                </div>

                <div class="grid-row">
                    <div class="column column--two-thirds column--reset">
                        <div id="section-summary" class="article-section article-section--summary  no-border">
                            <div class="grid-row">
                                <div class="column column--reset">
                                    <div class="rich-text-content">
                                        <p><@fmt.message key="message"/></p>
                                        <p class="is-hidden-if-no-js"><a href="#" onclick="hideCoronavirusBanner()"><@fmt.message key="dismiss"/></a></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </header>
    </article>
    <!--googleon: index-->

    <script>
        function hideCoronavirusBanner() {
            if (Cookiebot.consent.preferences) {
                setCookie("hide-coronavirus-banner", true, 5);
            }
            Array.prototype.forEach.call(document.querySelectorAll(".coronavirus-banner"), function (banner) {
                banner.parentElement.removeChild(banner);
            });
        }

        function setCookie(cname, cvalue, exdays) {
            var d = new Date();
            d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
            var expires = "expires=" + d.toUTCString();
            document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
        }
    </script>
</#if>
