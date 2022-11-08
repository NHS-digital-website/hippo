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
                                <svg class="nhsd-logo nhsd-!t-margin-right-3" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 80 60" width="70" height="54" aria-hidden="true" focusable="false">
                                    <rect fill="#005BBB" x="0" width="80" height="32.1" />
                                    <path fill="#FFFFFF" d="M76.9,3.8L75.2,9c-1.3-0.6-3.2-1.2-5.7-1.2c-2.8,0-5,0.4-5,2.5c0,3.7,10.1,2.3,10.1,10.2c0,7.1-6.7,9-12.7,9c-2.7,0-5.8-0.6-8-1.3l1.6-5.3c1.4,0.9,4.1,1.5,6.4,1.5c2.2,0,5.5-0.4,5.5-3.1c0-4.2-10.1-2.6-10.1-9.9c0-6.7,5.9-8.7,11.6-8.7C72.1,2.7,75.1,3,76.9,3.8" />
                                    <polygon fill="#FFFFFF" points="56.9,3.1 51.5,29 44.5,29 46.8,17.9 38.6,17.9 36.3,29 29.3,29 34.7,3.1 41.7,3.1 39.6,13 47.8,13 49.9,3.1   " />
                                    <polygon fill="#FFFFFF" points="32.1,3.1 26.6,29 18,29 12.5,11.1 12.5,11.1 8.8,29 2.3,29 7.8,3.1 16.5,3.1 21.8,21 21.9,21 25.6,3.1  " />
                                    <rect x="63.5" y="38.5" fill="#231F20" width="3.1" height="16" />
                                    <path fill="#231F20" d="M51.6,43.1c1.3-0.6,2.7-0.9,4.1-0.9c3.6,0,5.1,1.5,5.1,5v1.7c0,1.2,0,2.1,0,3c0,0.9,0.1,1.7,0.1,2.6h-2.7c-0.1-0.6-0.1-1.2-0.1-1.8h0c-0.7,1.3-2.2,2-3.6,2c-2.1,0-4.1-1.3-4.1-3.5c0-1.8,0.8-3,2-3.6c1.2-0.6,2.7-0.7,4-0.7H58c0-1.9-0.8-2.5-2.7-2.5c-1.3,0-2.7,0.5-3.7,1.3V43.1z M55.2,52.3c0.8,0,1.7-0.4,2.2-1.1c0.5-0.7,0.6-1.5,0.6-2.4h-1.3c-1.4,0-3.4,0.2-3.4,2C53.3,51.9,54.1,52.3,55.2,52.3" />
                                    <path fill="#231F20" d="M42.7,45.2h-2.3v-2.2h2.3v-2.7l3.1-1v3.6h2.7v2.2h-2.7v5.3c0,1,0.3,1.9,1.4,1.9c0.5,0,1.1-0.1,1.4-0.3l0.1,2.4c-0.7,0.2-1.5,0.3-2.3,0.3c-2.4,0-3.7-1.5-3.7-3.8L42.7,45.2z" />
                                    <path fill="#231F20" d="M35.2,38.5h3.1v2.7h-3.1V38.5z M35.2,42.9h3.1v11.6h-3.1V42.9z" />
                                    <path fill="#231F20" d="M32.6,42.9v10.7c0,3.2-1.3,6-6.2,6c-1.4,0-2.8-0.3-4.1-0.8l0.3-2.6c0.9,0.5,2.5,1,3.4,1c3.4,0,3.6-2.3,3.6-4.3h0c-0.6,1-1.9,1.9-3.7,1.9c-3.5,0-4.8-2.8-4.8-6c0-2.8,1.5-6.1,5-6.1
                                                    c1.6,0,2.8,0.5,3.6,1.9h0v-1.6L32.6,42.9z M29.5,48.7c0-2-0.8-3.7-2.6-3.7c-2.1,0-2.8,1.9-2.8,3.7c0,1.6,0.8,3.6,2.6,3.6C28.7,52.3,29.5,50.7,29.5,48.7" />
                                    <path fill="#231F20" d="M15.9,38.5H19v2.7h-3.1V38.5z M15.9,42.9H19v11.6h-3.1V42.9z" />
                                    <path fill="#231F20" d="M0,38.5h4.4c4.9,0,9.2,1.7,9.2,8s-4.3,8-9.2,8H0V38.5z M3.2,52H5c2.7,0,5.3-2,5.3-5.5C10.3,43,7.7,41,5,41H3.2V52z" />
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
