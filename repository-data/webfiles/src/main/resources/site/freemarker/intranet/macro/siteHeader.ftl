<#ftl output_format="HTML">
<#macro siteHeader enableSearch = true>
    <header class="site-header site-header--intranet <#if enableSearch>site-header--with-search<#else>site-header--without-search</#if>" id="header">
        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
            <div class="grid-row">
                <div class="column column--reset">
                    <div class="grid-wrapper grid-wrapper--collapse">
                        <div class="grid-row">
                            <div class="column column--18-75 column--reset">
                                <a class="site-header__waffle-link" href="https://www.office.com/" target="_blank" aria-label="Office 365"><span class="ms-Icon ms-Icon--WaffleOffice365"></span></a>
                                <a class="site-header-a__logo" href="<@hst.link siteMapItemRefId='root'/>"><img src="<@hst.webfile path="/images/nhs-digital-intranet-logo.svg"/>" alt="NHS Digital Intranet logo" class="site-header__logo"></a>
                            </div>

                            <#assign wrapperClassName = "site-header__search-wrapper site-header__search-wrapper--collapse" />
                            <#if enableSearch>
                                <#assign wrapperClassName = "site-header__search-wrapper" />
                            </#if>

                            <div class="main-nav">

                                <div class="${wrapperClassName}">
                                    <#if enableSearch>
                                        <#include "../../include/search-strip.ftl">
                                    </#if>
                                </div>

                                <nav>
                                    <button tabindex="0" class="main-nav__burger" aria-label="Navigation menu" id="navToggle">
                                    <span class="main-nav__burger-button">
                                        <span class="main-nav__burger-line-group">
                                            <span class="main-nav__burger-line">&nbsp;</span>
                                            <span class="main-nav__burger-line">&nbsp;</span>
                                            <span class="main-nav__burger-line">&nbsp;</span>
                                        </span>
                                    </span>
                                    </button>
                                    <div class="column <#if !enableSearch>column--81-25</#if> column--reset">
                                        <div class="main-nav__menu">
                                            <@hst.include ref="top-menu"/>
                                        </div>
                                    </div>
                                </nav>
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
