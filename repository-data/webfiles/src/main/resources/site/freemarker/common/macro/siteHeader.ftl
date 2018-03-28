<#ftl output_format="HTML">
<#macro siteHeader enableSearch>
<header class="site-header" role="banner" id="header">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="grid-row">
            <div class="column column--reset">
                <div class="grid-wrapper grid-wrapper--collapse">
                    <div class="grid-row">
                        
                        <div class="column column--18-75 column--reset">
                            <a href="<@hst.link siteMapItemRefId='root'/>"><img src="<@hst.webfile path="/images/nhs-logo.png"/>" alt="NHS Digital logo" class="site-header__logo"></a>
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
                                        <#include "../search-strip.ftl">
                                        </#if>
                                    </div>

                                    <nav>
                                        <ol class="list list--reset list--inline">
                                            <li><a href="/data-and-information">Data and information</a></li>
                                            <li><a href="/services">Systems and services</a></li>
                                            <li><a href="/news-and-events">News and events</a></li>
                                            <li><a href="/about-nhs-digital">About NHS Digital</a></li>
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