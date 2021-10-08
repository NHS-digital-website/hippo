<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro filterMenu>
    <a href="#content-id" class="nhsd-a-skip-link nhsd-a-skip-link--small nhsd-!t-margin-bottom-3">
        <span class="nhsd-a-skip-link__label">Skip to content</span>
    </a>
    <div class="">
        <div class="nhsd-o-filter-menu__search-bar">
            <div class="nhsd-m-search-bar nhsd-m-search-bar__full-width  nhsd-m-search-bar__small">
                <div class="nhsd-t-form-control">
                    <input class="nhsd-t-form-input" type="text" id="query" name="query" autocomplete="off" placeholder="Filter search..." aria-label="Keywords" />
                    <span class="nhsd-t-form-control__button">
                        <button  class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent"  type="submit" aria-label="Perform search"></button>
                    </span>
                </div>
            </div>
        </div>
        <div id="nhsd-filter-menu">
            <div class="nhsd-m-filter-menu-section">
                <div class="nhsd-m-filter-menu-section__accordion-heading">
                    <button class="nhsd-m-filter-menu-section__menu-button" aria-label="Heading 1" aria-expanded="false">
                        <span class="nhsd-m-filter-menu-section__heading-text nhsd-t-body-s">
                            <span id="icon">
                                <span class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"/></svg>
                                </span>
                            </span>
                            Heading 1
                        </span>
                    </button>
                    <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xxs" />
                    <div class="nhsd-m-filter-menu-section__accordion-panel">
                        <div class="nhsd-m-filter-menu-section__option-row">
                            <div class="nhsd-a-checkbox">
                                <label><input type="checkbox" />
                                    <div class="nhsd-a-checkbox__label">
                                        <span >Filter 1</span>
                                    </div>
                                    <div class="checkmark"></div>
                                </label>
                            </div>
                        </div>
                        <div class="nhsd-m-filter-menu-section__option-row">
                            <div class="nhsd-a-checkbox">
                                <label><input type="checkbox" />
                                    <div class="nhsd-a-checkbox__label">
                                        <span >Filter 2</span>
                                    </div>
                                    <div class="checkmark"></div>
                                </label>
                            </div>
                        </div>
                        <div class="nhsd-m-filter-menu-section__option-row">
                            <div class="nhsd-a-checkbox">
                                <label><input type="checkbox" />
                                    <div class="nhsd-a-checkbox__label">
                                        <span >Filter 3</span>
                                    </div>
                                    <div class="checkmark"></div>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="nhsd-m-filter-menu-section">
                <div class="nhsd-m-filter-menu-section__accordion-heading">
                    <button class="nhsd-m-filter-menu-section__menu-button" aria-label="Heading 2" aria-expanded="false">
                        <span class="nhsd-m-filter-menu-section__heading-text nhsd-t-body-s">
                            <span id="icon">
                                <span class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"/></svg>
                                </span>
                            </span>
                            Heading 2
                        </span>
                    </button>
                    <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xxs" />
                    <div class="nhsd-m-filter-menu-section__accordion-panel">
                        <div class="nhsd-m-filter-menu-section__option-row">
                            <div class="nhsd-a-checkbox">
                                <label><input type="checkbox" />
                                    <div class="nhsd-a-checkbox__label">
                                        <span>Filter 1</span>
                                    </div>
                                    <div class="checkmark"></div>
                                </label>
                            </div>
                        </div>
                        <div class="nhsd-m-filter-menu-section__option-row">
                            <div class="nhsd-a-checkbox">
                                <label><input type="checkbox" />
                                    <div class="nhsd-a-checkbox__label">
                                        <span >Filter 2</span>
                                    </div>
                                    <div class="checkmark"></div>
                                </label>
                            </div>
                        </div>
                        <div class="nhsd-m-filter-menu-section__option-row">
                            <div class="nhsd-a-checkbox">
                                <label><input type="checkbox" />
                                    <div class="nhsd-a-checkbox__label">
                                        <span >Filter 3</span>
                                    </div>
                                    <div class="checkmark"></div>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="nhsd-o-filter-menu__filter-button">
            <button class="nhsd-a-button" type="button">
                <span class="nhsd-a-button__label">Filter results</span>
            </button>
        </div>
    </div>
</#macro>
