<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<@hst.setBundle basename="coronavirus.banner"/>

<#if showBanner>
    <!--googleoff: index-->
    <article>
        <header class="coronavirus-banner" aria-labelledby="message-summary-title" role="alert" tabindex="-1">
            <div class="grid-wrapper grid-wrapper--collapse">
                <div class="grid-row">
                    <div class="column column--two-thirds column--reset">
                        <div class="article-header article-header--secondary">
                            <h1 class="h2" id="message-summary-title"><@fmt.message key="title"/></h1>
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
</#if>

<script>
    function hideCoronavirusBanner() {
        if (Cookiebot.consent.preferences) {
            setCookie("hide-coronavirus-banner", true, 5);
        }
        document.querySelectorAll(".coronavirus-banner").forEach(function (banner) {
            banner.remove();
        });
    }

    function setCookie(cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    }
</script>
