<#ftl output_format="HTML">

<#macro stickyNavTags links affix title filter selectedFilter>
    <#if links??>
        <div class="nhsd-m-filter-menu-section nhsd-m-filter-menu-section--active" data-active-menu="sortBy">
            <div class="nhsd-m-filter-menu-section__accordion-heading">
                <button class="nhsd-m-filter-menu-section__menu-button" type="button" aria-expanded="true">
                    <span class="nhsd-m-filter-menu-section__heading-text nhsd-t-body-s">
                        <span>
                            <span class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                    <path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"></path>
                                </svg>
                            </span>
                        </span>
                        ${title}
                    </span>
                </button>
                <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xxs">

                <#list links as link>
                    <#assign hasCount = (link.count?? && link.count?has_content)?then("(" + link.count + ")", "") />
                    <#if selectedFilter?seq_contains(link.key)>
                        <#assign linkout = "&" + filter + "=" + selectedFilter?join("&" + filter + "=") + affix />
                        <#assign filterLink = getDocumentUrl() + linkout?replace("&" + filter + "=" + link.key, "")?replace("&", "?", "f") />
                        <#assign filterChecked = true />
                    <#else>
                        <#assign filterLink = "?" + filter + "=" + selectedFilter?join("&" + filter + "=", "", "&" + filter + "=") + link.key + affix />
                        <#assign filterChecked = false />
                    </#if>

                    <div class="nhsd-m-filter-menu-section__accordion-panel nhsd-!t-margin-top-2 nhsd-!t-margin-bottom-2">
                        <div class="nhsd-m-filter-menu-section__option-row">
                            <span class="nhsd-a-checkbox">
                                <label>
                                    ${link.title}
                                    <input name="${filter}" type="checkbox" data-input-id="${title}-${link.key}" class="js-filter-checkbox" value="${link.key}" ${filterChecked?then('checked', '')} onchange="document.getElementById('roadmap-form').submit()">
                                    <span class="nhsd-a-checkbox__label">${link.title}</span>
                                    <span class="checkmark"></span>
                                </label>
                            </span>
                        </div>
                    </div>
                </#list>
            </div>
        </div>
    </#if>
</#macro>
