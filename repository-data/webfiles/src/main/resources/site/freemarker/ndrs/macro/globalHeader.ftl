<#ftl output_format="HTML">
<#include "./ndrsLogo.ftl">
<#include "./iconGenerator.ftl">

<@hst.setBundle basename="rb.generic.texts"/>
<@fmt.message key="text.close-menu" var="closeMenu" />
<@fmt.message key="text.close-search" var="closeSearch" />

<#macro globalHeader enableHr>
    <header id="header">
        <div class="nhsd-o-global-header" id="nhsd-global-header">
            <div class="nhsd-t-grid">
                <div class="nhsd-t-row">
                    <div class="nhsd-t-col">
                        <div class="nhsd-o-global-header__content-box">
                            <div class="nhsd-o-global-header__background"></div>
                            <#-- Logo -->
                            <div class="nhsd-o-global-header__logo-container">
                                <a class="nhsd-m-logo-link nhsd-o-global-header__logo" id="nhsd-global-header__logo" href="<@hst.link siteMapItemRefId='root'/>" aria-label="NDRS Home">
                                    <@ndrsLogo />
                                </a>
                            </div>
                            <div class="nhsd-o-global-header__menu" id="nhsd-global-header__menu">
                            <nav>
                                <div class="column <#if !enableSearch>column--81-25</#if> column--reset">
                                    <div class="main-nav__menu">
                                        <@hst.include ref="top-menu"/>
                                    </div>
                                </div>
                            </nav>
                                <div class="nhsd-o-global-header__menu-background"></div>
                                <div class="nhsd-o-global-header__menu-content-box">
                                    <a class="nhsd-a-icon-link nhsd-o-global-header__menu-close-button" id="nhsd-global-header__menu-close-button" href="#">
                                        <span class="nhsd-a-icon-link__label">${closeMenu}</span>
                                        <span class="nhsd-a-icon nhsd-a-icon--size-m">
                                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                                <path d="M8.9,8l3.1,3.1L11.1,12L8,8.9L4.9,12L4,11.1L7.1,8L4,4.9L4.9,4L8,7.1L11.1,4L12,4.9L8.9,8z"></path>
                                                <path d="M8,1.3c3.7,0,6.7,3,6.7,6.7s-3,6.7-6.7,6.7s-6.7-3-6.7-6.7S4.3,1.3,8,1.3 M8,0C3.6,0,0,3.6,0,8s3.6,8,8,8 s8-3.6,8-8S12.4,0,8,0L8,0z"></path>
                                            </svg>
                                        </span>
                                    </a>
                                    <#-- Navigation -->
                                    <@hst.include ref="main-nav"/>
                                </div>
                            </div>
                            <nav class="nhsd-m-button-nav nhsd-m-button-nav--condensed nhsd-m-button-nav--non-responsive nhsd-o-global-header__button-nav">
                                <svg class="nhsd-!t-margin-right-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 72" width="74" aria-hidden="true" focusable="false">
                                    <path fill="#1d1d1b" d="M0.54,47.35H10.69v2.71H4v4.24h6.13V57H4v4.83h6.75v2.71H0.54V47.35Z"/>
                                    <path fill="#1d1d1b" d="M13.94,51.85h3.13v1.72h0a4.74,4.74,0,0,1,4.16-2c3,0,4.31,2.12,4.31,5v8h-3.3V57.76c0-1.55,0-3.64-2.14-3.64-2.39,0-2.91,2.58-2.91,4.21v6.21h-3.3V51.85Z"/>
                                    <path fill="#1d1d1b" d="M40.62,51.85V63.3c0,3.45-1.38,6.7-6.7,6.7a11.14,11.14,0,0,1-4.38-.86l0.27-2.81a9.54,9.54,0,0,0,3.69,1.11c3.64,0,3.82-2.73,3.82-4.92h0a4.53,4.53,0,0,1-3.94,2c-3.74,0-5.17-3-5.17-6.4,0-3.05,1.58-6.57,5.37-6.57a4.21,4.21,0,0,1,3.89,2h0V51.85h3.15Zm-3.3,6.21c0-2.17-.84-3.94-2.76-3.94-2.24,0-3,2.07-3,4,0,1.72.91,3.84,2.81,3.84C36.49,62,37.32,60.17,37.32,58.06Z"/>
                                    <path fill="#1d1d1b" d="M44.09,46.07h3.3V64.53h-3.3V46.07Z"/>
                                    <path fill="#1d1d1b" d="M51.43,52.52a11.35,11.35,0,0,1,4.41-1c3.87,0,5.47,1.6,5.47,5.34v1.63c0,1.28,0,2.24,0,3.18s0.07,1.85.15,2.83H58.59a15.66,15.66,0,0,1-.15-1.9h0a4.53,4.53,0,0,1-3.92,2.19c-2.24,0-4.43-1.36-4.43-3.77a3.73,3.73,0,0,1,2.17-3.62,10,10,0,0,1,4.26-.74H58.3c0-2-.91-2.73-2.86-2.73a6.64,6.64,0,0,0-3.92,1.38Zm3.87,9.9a2.8,2.8,0,0,0,2.32-1.13,4.21,4.21,0,0,0,.69-2.61H56.87c-1.48,0-3.67.25-3.67,2.19C53.2,61.95,54.11,62.42,55.29,62.42Z"/>
                                    <path fill="#1d1d1b" d="M64.55,51.85h3.13v1.72h0.05a4.73,4.73,0,0,1,4.16-2c3,0,4.31,2.12,4.31,5v8H72.9V57.76c0-1.55,0-3.64-2.14-3.64-2.39,0-2.91,2.58-2.91,4.21v6.21h-3.3V51.85Z"/>
                                    <path fill="#1d1d1b" d="M88,63.11H88a4.71,4.71,0,0,1-3.89,1.72c-3.89,0-5.32-3.2-5.32-6.67s1.43-6.6,5.32-6.6a4.51,4.51,0,0,1,3.79,1.77h0V46.07h3.3V64.53H88V63.11Zm-3-.84c2.19,0,2.9-2.31,2.9-4.11s-0.84-4-3-4-2.73,2.34-2.73,4S82.84,62.27,85,62.27Z"/>
                                    <path fill="#005eb8"  d="M91.53,0H0V36.86H91.53V0Z"/>
                                    <path fill="#FFFFFF" d="M88.1,4.37l-1.93,5.91A15.09,15.09,0,0,0,79.6,8.92c-3.16,0-5.72.46-5.72,2.85,0,4.2,11.58,2.64,11.58,11.65,0,8.2-7.65,10.33-14.57,10.33a33.43,33.43,0,0,1-9.22-1.54l1.88-6a15.58,15.58,0,0,0,7.35,1.7c2.48,0,6.36-.47,6.36-3.53,0-4.76-11.58-3-11.58-11.34C65.67,5.34,72.42,3,79,3c3.68,0,7.13.39,9.14,1.32"/>
                                    <polygon fill="#FFFFFF"  points="65.16 3.56 58.96 33.22 50.98 33.22 53.63 20.52 44.18 20.52 41.53 33.22 33.55 33.22 39.75 3.56 47.73 3.56 45.38 14.91 54.82 14.91 57.17 3.56 65.16 3.56"/>
                                    <polygon fill="#FFFFFF"  points="36.8 3.56 30.48 33.22 20.57 33.22 14.33 12.7 14.25 12.7 10.1 33.22 2.58 33.22 8.95 3.56 18.9 3.56 25.01 24.13 25.09 24.13 29.28 3.56 36.8 3.56"/>
                                </svg>

                                <a class="nhsd-a-button nhsd-a-button--circle" id="nhsd-global-header__search-button" aria-label="Open search" aria-controls="nhsd-global-header__search" aria-expanded="false" href="${searchLink}">
                                    <@buildInlineSvg "search" "s"/>
                                </a>
                                <button class="nhsd-a-button nhsd-o-global-header__menu-button" id="nhsd-global-header__menu-button" type="button" aria-controls="nhsd-global-header__menu" aria-expanded="false">
                                    <span class="nhsd-a-button__label">Menu</span>
                                    <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                            <rect x="1" y="3.1" class="st0" width="14" height="1.6"></rect>
                                            <rect x="1" y="7.2" class="st0" width="14" height="1.6"></rect>
                                            <rect x="1" y="11.3" class="st0" width="14" height="1.6"></rect>
                                        </svg>
                                    </span>
                                </button>
                            </nav>
                            <div class="nhsd-o-global-header__search" id="nhsd-global-header__search">
                                <div class="nhsd-o-global-header__search-background"></div>
                                <div class="nhsd-o-global-header__search-content-box">
                                    <div class="nhsd-o-global-header__header-section">
                                        <label for="query" class="nhsd-o-global-header__search-label nhsd-t-heading-m">Search</label>
                                        <a class="nhsd-a-icon-link nhsd-o-global-header__search-close-button" id="nhsd-global-header__search-close-button" href="#">
                                            <span class="nhsd-a-icon-link__label">${closeSearch}</span>
                                            <span class="nhsd-a-icon nhsd-a-icon--size-m">
                                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                                    <path d="M8.9,8l3.1,3.1L11.1,12L8,8.9L4.9,12L4,11.1L7.1,8L4,4.9L4.9,4L8,7.1L11.1,4L12,4.9L8.9,8z"></path>
                                                    <path d="M8,1.3c3.7,0,6.7,3,6.7,6.7s-3,6.7-6.7,6.7s-6.7-3-6.7-6.7S4.3,1.3,8,1.3 M8,0C3.6,0,0,3.6,0,8s3.6,8,8,8 s8-3.6,8-8S12.4,0,8,0L8,0z"></path>
                                                </svg>
                                            </span>
                                        </a>
                                    </div>
                                    <div class="nhsd-m-search-bar">
                                        <form role="search" method="get" action="${searchLink}" class="nhsd-t-form" novalidate="" autocomplete="off">
                                            <div class="nhsd-t-form-group">
                                                <div class="nhsd-t-form-control">
                                                    <input class="nhsd-t-form-input" type="text" id="query" name="query" autocomplete="off" placeholder="What are you looking for today?">
                                                    <span class="nhsd-t-form-control__button">
                                                        <button data-uipath="search.button" class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent" type="submit" aria-label="Perform search">
                                                            <@buildInlineSvg "search" "s"/>
                                                        </button>
                                                    </span>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <#if enableHr>
                <hr class="nhsd-a-horizontal-rule nhsd-!t-margin-0">
            </#if>
        </div>
    </header>
</#macro>
