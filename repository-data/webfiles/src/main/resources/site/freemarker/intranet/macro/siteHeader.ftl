<#ftl output_format="HTML">
<#include "./nhsdLogo.ftl">

<@hst.setBundle basename="rb.generic.texts"/>
<@fmt.message key="text.close-menu" var="closeMenu" />
<@fmt.message key="text.close-search" var="closeSearch" />

<#macro siteHeader enableSearch = true>
    <header class="nhsd-o-global-header nhsd-o-global-header--mobile-menu" id="nhsd-global-header">
        <div class="nhsd-t-grid">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col">
                    <div class="nhsd-o-global-header__content-box">
                        <div class="nhsd-o-global-header__logo-container">
                            <div class="nhsd-t-flex nhsd-t-flex--align-items-centre">
                                <a href="#" class="nhsd-waffle-menu">
                                    <svg width="30px" height="30px" viewBox="0 0 30 30" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                                        <g id="Pages" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                                            <g id="Intranet---Task" transform="translate(-77.000000, -35.000000)" fill="#005EB8">
                                                <g id="Group-20" transform="translate(77.000000, 35.000000)">
                                                    <circle id="Oval" cx="3" cy="3" r="3"></circle>
                                                    <circle id="Oval-Copy-3" cx="3" cy="15" r="3"></circle>
                                                    <circle id="Oval-Copy-6" cx="3" cy="27" r="3"></circle>
                                                    <circle id="Oval-Copy" cx="15" cy="3" r="3"></circle>
                                                    <circle id="Oval-Copy-4" cx="15" cy="15" r="3"></circle>
                                                    <circle id="Oval-Copy-7" cx="15" cy="27" r="3"></circle>
                                                    <circle id="Oval-Copy-2" cx="27" cy="3" r="3"></circle>
                                                    <circle id="Oval-Copy-5" cx="27" cy="15" r="3"></circle>
                                                    <circle id="Oval-Copy-8" cx="27" cy="27" r="3"></circle>
                                                </g>
                                            </g>
                                        </g>
                                    </svg>
                                </a>
                                <a class="nhsd-m-logo-link nhsd-o-global-header__logo" id="nhsd-global-header__logo" href="<@hst.link siteMapItemRefId='root'/>" aria-label="NHS Digital Intranet home">
                                    <@nhsdLogo></@nhsdLogo>
                                </a>
                            </div>
                            <div class="nhsd-o-global-header__product-name">
                                Corporate Intranet
                                <div class="nhsd-t-body-s nhsd-!t-col-dark-grey nhsd-!t-font-weight-regular nhsd-!t-margin-0">Morning first name last name</div>
                            </div>
                        </div>
                        <div class="nhsd-o-global-header__menu" id="nhsd-global-header__menu">
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
                                <@hst.include ref="top-menu"/>
                            </div>
                        </div>
                        <nav class="nhsd-m-button-nav nhsd-m-button-nav--condensed nhsd-m-button-nav--non-responsive nhsd-o-global-header__button-nav">
                            <a class="nhsd-a-button nhsd-a-button--circle" href="#">
                                <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16">
                                    <g>
                                    <path d="M 16 11.199219 L 15.332031 16 L 0.667969 16 L 0 11.199219 L 3.867188 9.667969 C 2.867188 8.601562 2.265625 7.199219 2.265625 5.734375 C 2.265625 2.601562 4.867188 0 8 0 C 11.132812 0 13.734375 2.601562 13.734375 5.734375 C 13.734375 7.199219 13.132812 8.601562 12.132812 9.667969 Z M 13.601562 12.667969 L 9.464844 11.066406 L 9.464844 8.867188 C 10.667969 8.066406 11.464844 7.199219 11.464844 5.667969 C 11.464844 3.800781 9.933594 2.265625 8.066406 2.265625 C 6.199219 2.265625 4.667969 3.800781 4.667969 5.667969 C 4.667969 7.199219 5.464844 8.066406 6.667969 8.867188 L 6.667969 11.066406 L 2.398438 12.667969 L 2.601562 13.734375 L 13.398438 13.734375 Z M 13.601562 12.667969 "/>
                                    </g>
                                    </svg>
                                </span>
                            </a>
                            <a class="nhsd-a-button nhsd-a-button--circle" id="nhsd-global-header__search-button" aria-label="Open search" aria-controls="nhsd-global-header__search" aria-expanded="false" href="/search">
                                <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                        <path d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/>
                                    </svg>
                                </span>
                            </a>
                            <button class="nhsd-a-button nhsd-o-global-header__menu-button" id="nhsd-global-header__menu-button" type="button" aria-controls="nhsd-global-header__menu" aria-expanded="false">
                                <span class="nhsd-a-button__label">Menu</span>
                                <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                        <rect x="1" y="3.1" class="st0" width="14" height="1.6"/>
                                        <rect x="1" y="7.2" class="st0" width="14" height="1.6"/>
                                        <rect x="1" y="11.3" class="st0" width="14" height="1.6"/>
                                    </svg>
                                </span>
                            </button>
                        </nav>
                        <div class="nhsd-o-global-header__search" id="nhsd-global-header__search">
                            <div class="nhsd-o-global-header__search-background"></div>
                            <div class="nhsd-o-global-header__search-content-box">
                                <a class="nhsd-a-icon-link nhsd-o-global-header__search-close-button" id="nhsd-global-header__search-close-button" href="#">
                                    <span class="nhsd-a-icon-link__label">Close search</span>
                                    <span class="nhsd-a-icon nhsd-a-icon--size-m">
                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                            <path d="M8.9,8l3.1,3.1L11.1,12L8,8.9L4.9,12L4,11.1L7.1,8L4,4.9L4.9,4L8,7.1L11.1,4L12,4.9L8.9,8z"/>
                                            <path d="M8,1.3c3.7,0,6.7,3,6.7,6.7s-3,6.7-6.7,6.7s-6.7-3-6.7-6.7S4.3,1.3,8,1.3 M8,0C3.6,0,0,3.6,0,8s3.6,8,8,8 s8-3.6,8-8S12.4,0,8,0L8,0z"/>
                                        </svg>
                                    </span>
                                </a>
                                <div class="nhsd-m-search-bar">
                                    <form role="search" method="get" action="${searchLink}" class="nhsd-t-form" novalidate autocomplete="off" aria-label="Search">
                                        <div class="nhsd-t-form-group">
                                            <div class="nhsd-t-form-control">
                                                <input class="nhsd-t-form-input" type="text" id="query" name="query" autocomplete="off" placeholder="What are you looking for today?" aria-label="Keywords" />
                                                <span class="nhsd-t-form-control__button">
                                                    <button class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent" type="submit" aria-label="Perform search" data-uipath="search.button">
                                                        <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                                                <path d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/>
                                                            </svg>
                                                        </span>
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
        <hr class="nhsd-a-horizontal-rule nhsd-!t-margin-0">
    </header>
</#macro>
