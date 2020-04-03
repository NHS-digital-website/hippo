<#ftl output_format="HTML">
<#macro siteHeader enableSearch = true>
<header class="site-header site-header--intranet" id="header">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="grid-row">
            <div class="column column--reset">
                <div class="grid-wrapper grid-wrapper--collapse">
                    <div class="grid-row">
                        <div class="column column--18-75 column--reset">
                            <a class="site-header-a__logo" href="<@hst.link siteMapItemRefId='root'/>"><img src="<@hst.webfile path="/images/nhs-digital-intranet-logo.svg"/>" alt="NHS Digital Intranet logo" class="site-header__logo"></a>
                        </div>

                        <#assign wrapperClassName = "site-header__search-wrapper site-header__search-wrapper--collapse" />
                        <#if enableSearch>
                        <#assign wrapperClassName = "site-header__search-wrapper" />
                        </#if>

                        <div class="main-nav">
                            <div class="main-nav__burger" id="navToggle">
                                <button class="main-nav__burger-button" aria-label="Navigation menu">
                                    <span class="main-nav__burger-line">&nbsp;</span>
                                    <span class="main-nav__burger-line">&nbsp;</span>
                                    <span class="main-nav__burger-line">&nbsp;</span>
                                </button>
                            </div>

                            <div class="column column--81-25  column--reset">
                                <div class="main-nav__menu">
                                    <div class="${wrapperClassName}">
                                        <#if enableSearch>
                                        <#include "../../include/search-strip.ftl">
                                        </#if>
                                    </div>

                                    <nav>
                                        <ol class="list list--reset list--inline">
                                            <li><a href="#">Tasks</a></li>
                                            <li><a href="#">Teams & Networks</a></li>
                                            <li><a href="#">News & Announcements</a></li>
                                            <li><a href="#">Collaborate</a></li>
                                        </ol>
                                    </nav>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</header>

<script>
    (function(){
        var oldBrowser = window.addEventListener ? false : true;
        var $header = document.getElementById('header');
        var $nav = document.getElementById('nav');
        var $navToggle = document.getElementById('navToggle');

        function handleToggleClick() {
            if ($header.className.indexOf('menu-open') >= 0) {
                $header.className = $header.className.replace('menu-open', '');
            } else {
                $header.className += ' menu-open';
            }
            $header.className = $header.className.replace(/^\s+|\s+$/g,'');
        }

        function init () {
            if (!oldBrowser) {
                $navToggle.addEventListener('click', handleToggleClick);
                window.addEventListener('resize', onResize);
            } else {
                $navToggle.attachEvent('onclick', handleToggleClick);
                window.attachEvent('onresize', onResize);
            }
        }

        function onResize() {
            if (window.innerWidth > 1024 && $header.className.indexOf('menu-open') >= 0) {
                handleToggleClick();
            }
        }
        init();
    }())
</script>
</#macro>
