<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/sections/sections.ftl">

<#--
NOTE:

This file is a modified copy of https://github.com/swagger-api/swagger-ui/blob/1f565b48475b350a5d8162eef5b28655e7555c8f/dev-helpers/oauth2-redirect.html
and is the target of the 'redirect URL' included in the configuration of the API's client application in https://portal.developer.nhs.uk/my-apps/
as 'Callback URL'.

The original embedded JavaScript calls window.close() (see below) and so with 'vanilla' Swagger UI the content of this file is never
actually displayed to the end users (the original <body> is empty anyway).

In our case, however, 'Try this API' opens in a popup, rather than in the main window as is the case with vanilla Swagger UI. OAuth2 authorisation flow is initiated
from said popup but completing it opens the current file in the _main_ browser window which is automatically brought forward, thus covering the popup.
This could easily be confusing to the end users, hence the aforementioned modifications were made so that the page rendered by the current file no longer closes
automatically and displayes an informative message instead (with basic styling).
-->

<html lang="en-US">
<title>Try this API: OAuth2 Redirect</title>

<#include "app-layout-head.ftl">

<body onload="run()">
<!-- Google Tag Manager (noscript) -->
<noscript>
    <iframe src="https://www.googletagmanager.com/ns.html?id=GTM-W6GJCR9"
            height="0" width="0" style="display:none;visibility:hidden"></iframe>
</noscript>
<!-- End Google Tag Manager (noscript) -->
    <#-- 'Page header' (banner with NHSD logo) above the 'document header' -->
    <header class="site-header site-header--with-search" id="header">
        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
            <div class="grid-row">
                <div class="column column--reset">
                    <div class="grid-wrapper grid-wrapper--collapse">
                        <div class="grid-row">
                            <div class="column column--18-75 column--reset">
                                <a class="site-header-a__logo" href="<@hst.link siteMapItemRefId='root'/>">
                                    <img src="<@hst.webfile path="/images/nhs-digital-logo.svg"/>" alt="NHS Digital logo" class="site-header__logo"></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">

            <div id="content" aria-label="Document content">

                <article class="article article--apispecification" itemscope>
                    <#assign section = {
                        "emphasisType": "Note",
                        "heading": "Authentication completed for 'Try this API'",
                        "bodyCustom": "You can now close this tab and switch back to the 'Try this API' popup (likely hiding behind this window)."
                    } />
                    <@emphasisBox section=section />
                </article>

            </div>
</body>
</html>
<script>
    'use strict';
    function run () {
        var oauth2 = window.opener.swaggerUIRedirectOauth2;
        var sentState = oauth2.state;
        var redirectUrl = oauth2.redirectUrl;
        var isValid, qp, arr;

        if (/code|token|error/.test(window.location.hash)) {
            qp = window.location.hash.substring(1);
        } else {
            qp = location.search.substring(1);
        }

        arr = qp.split("&")
        arr.forEach(function (v,i,_arr) { _arr[i] = '"' + v.replace('=', '":"') + '"';})
        qp = qp ? JSON.parse('{' + arr.join() + '}',
            function (key, value) {
                return key === "" ? value : decodeURIComponent(value)
            }
        ) : {}

        isValid = qp.state === sentState

        if ((
            oauth2.auth.schema.get("flow") === "accessCode"||
            oauth2.auth.schema.get("flow") === "authorizationCode"
        ) && !oauth2.auth.code) {
            if (!isValid) {
                oauth2.errCb({
                    authId: oauth2.auth.name,
                    source: "auth",
                    level: "warning",
                    message: "Authorization may be unsafe, passed state was changed in server Passed state wasn't returned from auth server"
                });
            }

            if (qp.code) {
                delete oauth2.state;
                oauth2.auth.code = qp.code;
                oauth2.callback({auth: oauth2.auth, redirectUrl: redirectUrl});
            } else {
                let oauthErrorMsg
                if (qp.error) {
                    oauthErrorMsg = "["+qp.error+"]: " +
                        (qp.error_description ? qp.error_description+ ". " : "no accessCode received from the server. ") +
                        (qp.error_uri ? "More info: "+qp.error_uri : "");
                }

                oauth2.errCb({
                    authId: oauth2.auth.name,
                    source: "auth",
                    level: "error",
                    message: oauthErrorMsg || "[Authorization failed]: no accessCode received from the server"
                });
            }
        } else {
            oauth2.callback({auth: oauth2.auth, token: qp, isValid: isValid, redirectUrl: redirectUrl});
        }

        // See 'NOTE' at the top of this page.
        // window.close();
    }
</script>
