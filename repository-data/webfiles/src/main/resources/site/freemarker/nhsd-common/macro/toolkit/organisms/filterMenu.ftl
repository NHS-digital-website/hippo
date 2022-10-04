<#ftl output_format="HTML">
<#macro nhsdFilterMenu  facetFilter filters="">

    <form id="feed-list-filter" method="get">
        <div class="nhsd-o-filter-menu__search-bar">
            <div
                class="nhsd-m-search-bar nhsd-m-search-bar__full-width nhsd-m-search-bar__small">
                <div class="nhsd-t-form-control">
                    <input
                        class="nhsd-t-form-input js-feedhub-query"
                        type="text"
                        name="query"
                        value="${query}"
                        autocomplete="off"
                        placeholder="Filter search..."
                        aria-label="Filter search..."
                    />
                    <span class="nhsd-t-form-control__button">
                                        <button
                                            class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent"
                                            type="submit"
                                            aria-label="Perform search">
                                            <span
                                                class="nhsd-a-icon nhsd-a-icon--size-s">
                                                <svg
                                                    xmlns="http://www.w3.org/2000/svg"
                                                    preserveAspectRatio="xMidYMid meet"
                                                    focusable="false"
                                                    viewBox="0 0 16 16" width="100%"
                                                    height="100%">
                                                    <path
                                                        d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/>
                                                </svg>
                                            </span>
                                        </button>
                                    </span>
                </div>
            </div>
        </div>
        <div id="nhsd-filter-menu" class="js-filter-list">
            <#if filters?has_content && !facetFilter >
                <#list filters as filterOptions>
                    <#assign filterKey = filterOptions.key />
                    <#assign filterName = filterOptions.name />
                    <#assign active = (filterValues[filterOptions.key])?has_content || (activeFilters?has_content && activeFilters?seq_contains(filterKey)) />
                    <div
                        class="nhsd-m-filter-menu-section ${active?then("nhsd-m-filter-menu-section--active", "")}"
                        data-active-menu="${filterKey}">
                        <div class="nhsd-m-filter-menu-section__accordion-heading">
                            <button class="nhsd-m-filter-menu-section__menu-button"
                                    aria-expanded="${active?then("true", "false")}"
                                    type="button">
                                            <span
                                                class="nhsd-m-filter-menu-section__heading-text nhsd-t-body-s">
                                                <span
                                                    class="${active?then("active", "")}">
                                                    <span
                                                        class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                                        <svg
                                                            xmlns="http://www.w3.org/2000/svg"
                                                            preserveAspectRatio="xMidYMid meet"
                                                            focusable="false"
                                                            viewBox="0 0 16 16"
                                                            width="100%"
                                                            height="100%">
                                                            <path
                                                                d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"/>
                                                        </svg>
                                                    </span>
                                                </span>
                                                ${filterName}
                                            </span>
                            </button>
                            <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xxs"/>

                            <div
                                class="nhsd-m-filter-menu-section__accordion-panel">
                                <#list filterOptions.values as filterOption, value>
                                    <div
                                        class="nhsd-m-filter-menu-section__option-row">
                                            <span class="nhsd-a-checkbox">
                                                <label>
                                                    <input
                                                        name="${filterKey}"
                                                        type="checkbox"
                                                        data-input-id="${filterKey}-${filterOption}"
                                                        class="js-filter-checkbox"
                                                        value="${filterOption}"
                                                        <#if filterValues?has_content && filterKey?has_content && filterValues[filterKey]?has_content>
                                                            ${filterValues[filterKey]?seq_contains(filterOption)?then('checked=checked', '')}
                                                        </#if>
                                                    />
                                                    <#assign filterSize = "" />
                                                    <#if value gt 0>
                                                        <#assign filterSize = "(${value})" />
                                                    </#if>
                                                    <span
                                                        class="nhsd-a-checkbox__label nhsd-t-body-s">
                                                    <#if filterName = "Topic">
                                                        ${topicMap[filterOption]}
                                                    <#else>
                                                        ${filterOption}
                                                    </#if>
                                                        ${filterSize}</span>
                                                    <span
                                                        class="checkmark"></span>
                                                </label>
                                            </span>
                                    </div>
                                </#list>
                            </div>
                        </div>
                    </div>
                </#list>
            <#else>
                <div id="nhsd-filter-menu" class="js-filter-list">
                    <@hst.include ref="filters"/>
                </div>

            </#if>
        </div>
        <div class="nhsd-o-filter-menu__filter-button">
            <button class="nhsd-a-button" type="submit"><span
                    class="nhsd-a-button__label">Filter results</span></button>
        </div>
    </form>

</#macro>