<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<div class="nhsd-t-float-left">
    <h6 class="nhsd-t-heading-xs nhsd-!t-margin-bottom-2">Search</h6>
</div>
<div class=" nhsd-!t-padding-0 nhsd-!t-margin-bottom-2">
    <div class="nhsd-t-form-control">
        <@hst.renderURL fullyQualified=true var="searchUrlLink" />
        <form
            action="${searchUrlLink}<#if showRetired>?&showRetired</#if>"
            method="get">
            <input
                class="nhsd-t-form-input"
                type="text"
                id="catalogue-search-bar-input"
                name="query"
                autocomplete="off"
                placeholder="What are you looking for today?"
                aria-label="Keywords"
                value="${(query)!}"
            />
            <span class="nhsd-t-form-control__button">
                <button class="nhsd-a-button nhsd-a-button--circle"
                        type="submit" aria-label="Search">
                  <span class="nhsd-a-icon nhsd-a-icon--size-s">
                  <svg xmlns="http://www.w3.org/2000/svg"
                       preserveAspectRatio="xMidYMid meet"
                       focusable="false" viewBox="0 0 16 16"><path
                          d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/></svg>
                  </span>
                </button>
            </span>
        </form>
    </div>
</div>