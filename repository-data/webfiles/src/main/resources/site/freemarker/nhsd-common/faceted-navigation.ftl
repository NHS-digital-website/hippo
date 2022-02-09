<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="month-names"/>
<#if facets??>
    <@hst.link var="baseLink" hippobean=facets />
    <#list facets.folders as facet>
        <div class="article-section-nav-wrapper faceted-nav-facet">
            <div class="js-filter-list">
                <#assign filterKey = facet.name />
                <#assign filterName = facet.displayName?cap_first />
                <div class="nhsd-m-filter-menu-section ${facet.folders[0].leaf?then('nhsd-m-filter-menu-section--active', '')}" data-active-menu="${filterKey}">
                    <div class="nhsd-m-filter-menu-section__accordion-heading">
                            <button class="nhsd-m-filter-menu-section__menu-button" type="button">
                                <span class="nhsd-m-filter-menu-section__heading-text nhsd-t-body-s">
                                    <span>
                                        <span class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                                <path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"/>
                                            </svg>
                                        </span>
                                    </span>
                                    ${filterName}
                                </span>
                            </button>
                        <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xxs" />

                        <div class="nhsd-m-filter-menu-section__accordion-panel">
                            <#list facet.folders as value>
                                <#assign valueName = value.displayName />
                                <div class="nhsd-m-filter-menu-section__option-row">
                                    <span class="nhsd-a-checkbox">
                                        <#assign filterName = facet.name?replace(' ', '+')/>
                                        <#assign filterValue = valueName?replace(' ', '+')/>
                                        <#assign newLink = baseLink + "/" + filterName + "/" + filterValue />
                                        <#if value.leaf>
                                            <#assign newLink = newLink?replace('/${filterName}/${filterValue}', '', 'i')>
                                        </#if>                                        
                                        <label>
                                            <#assign newLink = newLink + query?has_content?then("?query=" + query, "") />

                                            <#if filterKey="year">
                                                <#assign redirect = newLink?replace('month/(?:[0-9]|1[0-1])/?', '', 'ir')/>
                                            <#else>
                                                <#assign redirect = newLink />
                                            </#if>
                                            ${valueName} (${value.count})
                                            <#if facet.name="month">
                                                <@fmt.message key=value.name var="monthName"/>
                                                <#assign valueName=monthName/>
                                            </#if>
                                            <input name="${filterKey}" type="checkbox"
                                                data-input-id="${filterKey}-${valueName}"
                                                class="js-filter-checkbox" 
                                                value="${valueName}"
                                                ${value.leaf?then('checked=checked', '')}
                                                onClick="window.location='${redirect}'"
                                            />
                                            <span class="nhsd-a-checkbox__label nhsd-t-body-s">${valueName} (${value.count})</span>
                                            <span class="checkmark"></span>
        
                                        </label>
                                    </span>
                                </div>
                            </#list>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </#list>
</#if>
