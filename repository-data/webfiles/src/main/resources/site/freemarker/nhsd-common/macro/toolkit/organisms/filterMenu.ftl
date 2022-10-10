<#ftl output_format="HTML">
<#macro nhsdFilterMenu>

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
                        data-test-text="searchBox"
                    />
                    <span class="nhsd-t-form-control__button">
                                        <button
                                            class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent"
                                            type="submit"
                                            aria-label="Perform search" data-test-text="button">
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
        <#nested />

    </form>

</#macro>