<#ftl output_format="HTML">
<#macro siteHeader enableSearch>
    <@hst.setBundle basename="op.london.bridge"/>
    <@fmt.message key="enable-black-site-features" var="blackSite" />
    <header
        class="site-header ${(blackSite == "true")?then("site-header--black", "")} <#if enableSearch>site-header--with-search<#else>site-header--without-search</#if>"
        id="header" style="margin-top: 0px">
        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
            <div class="grid-row">
                <div class="column column--reset" style="border-bottom: 4px solid #005eb8 !important;">
                    <div class="grid-wrapper grid-wrapper--collapse">
                        <div class="grid-row">
                            <div data-theme="white" class="nhse-global-menu" id="nhse-global-menu">
                                <div>
                                    <div class="nhsd-t-grid">
                                        <div class="nhsd-t-row">
                                            <div class="nhsd-t-col">
                                                <div
                                                    class="nhse-global-menu__container" style="background-color: #f0f4f5;">
                                                    <a aria-label="Visit NHS England website"
                                                       class="nhse-global-menu__logo"
                                                       href="/"
                                                       title="NHS England" style="border-bottom-color: transparent !important;">
                                                        <div class="nhse-global-menu__logo__img"></div>
                                                        <span
                                                            class="nhse-global-menu__logo__name" style="border-bottom: none">England</span>
                                                    </a>
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
        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" style="margin-top: 15px; margin-bottom: 15px !important;">
            <div class="grid-row">
                <div class="column column--reset">
                    <div class="grid-wrapper grid-wrapper--collapse">
                        <div class="grid-row">
                            <div class="column column--18-75 column--reset">
                                <div class="nhsd-o-global-header__descriptor-atom">
                                    <p class="digital-descriptor-text"
                                       id="digital-descriptor-text-id" style="font-family:'Frutiger W01', 'Arial', 'sans-serif'; padding-top: 4px !important;"> Digital </p>
                                </div>
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
                                    <button tabindex="0" class="main-nav__burger"
                                            aria-label="Navigation menu"
                                            id="navToggle">
                                    <span class="main-nav__burger-button">
                                        <span class="main-nav__burger-line-group">
                                            <span class="main-nav__burger-line">&nbsp;</span>
                                            <span class="main-nav__burger-line">&nbsp;</span>
                                            <span class="main-nav__burger-line">&nbsp;</span>
                                        </span>
                                    </span>
                                    </button>
                                    <div
                                        class="column <#if !enableSearch>column--81-25</#if> column--reset">
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
</#macro>
