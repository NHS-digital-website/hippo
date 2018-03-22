<#ftl output_format="HTML">

<div class="cookie-banner cookie-banner--hidden">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="grid-row">
            <div class="column column--reset">
                <div class="grid-wrapper">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="cookie-banner__contents-wrapper">
                                <div class="cookie-banner__contents">
                                    <div class="grid-row">
                                        <div class="column column--two-thirds column--reset">
                                            <span class="white-links white-links--secondary">We use cookies to ensure you get the best possible experience on our website.
                                            <br>
                                            <a href="/privacy-and-cookies">Find out more</a> about changing your cookie settings, otherwise we’ll assume you’re OK to continue.</span>
                                        </div>
                                        <div class="column column--one-third column--reset cookie-banner__button-wrapper">
                                            <a href="#" class="button button--tiny button--warning" id="cookieAcceptButton">I’m fine with this</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    (function() {
        var cookieName = 'acceptedcookie';
        var oldBrowser = window.addEventListener ? false : true;

        function cookieAccepted(cookieName) {
            var cookiesArray = document.cookie.split(";");
            cookieAccepted = false;

            for (var i = 0; i < cookiesArray.length; i ++) {
                var currentCookie = cookiesArray[i];
                if (currentCookie.indexOf(cookieName) >= 0) {
                    cookieAccepted = true;
                    break;
                }
            }
            
            return cookieAccepted;
        }

        function showCookieBanner() {
            var $cookieBanner = document.querySelector('.cookie-banner');
            $cookieBanner.className = $cookieBanner.className.replace('cookie-banner--hidden', '');
        }

        function hideCookieBanner() {
            var $cookieBanner = document.querySelector('.cookie-banner');
            $cookieBanner.className += 'cookie-banner--hidden';
        }

        function setCookie(name, value, days) {
            days = !isNaN(days) ? days : 30;

            if (days) {
                date = new Date();
                date.setTime(date.getTime() + (days*24*60*60*1000));
                expires = '; expires=' + date.toGMTString();
            } else {
                expires = '';
            }

            document.cookie = name + '=' + value.toString() + ';' + expires;
        }

        function initCookieBanner() {
            if (!cookieAccepted(cookieName)) {
                showCookieBanner();
            }

            var $acceptButton = document.getElementById('cookieAcceptButton');
            if (!oldBrowser) {
                $acceptButton.addEventListener('click', handleCookieButtonClick);
            } else {
                $acceptButton.attachEvent('onclick', handleCookieButtonClick);
            }
        }

        function handleCookieButtonClick(e) {
            e.preventDefault();
            
            setCookie(cookieName, true, 90);
            hideCookieBanner();
        }

        initCookieBanner();
    }())
</script>