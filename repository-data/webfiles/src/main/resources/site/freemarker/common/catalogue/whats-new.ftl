<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../../nhsd-common/macro/pagination.ftl">

<div class="nhsd-!t-margin-bottom-2">
    <div class="nhsd-t-grid">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12">
                <h2 id="nhsd-az-nav-heading" class="nhsd-t-heading-m">What's new</h2>
            </div>
        </div>
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12">
                <#list pageable.items as item>
                    <div class="nhsd-t-row">
                        <@whatsnewitem item.title item.shortsummary item.getPublishedDate()/>
                    </div>
                </#list>
                <#if pageable?? && pageable.total gt 0>
                    <hr class="nhsd-a-horizontal-rule">
                    <@pagination />
                </#if>
            </div>
        </div>
    </div>
</div>

<#macro whatsnewitem name summary publishedTime>
    <@fmt.formatDate value=publishedTime type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" var="publishedDate" />
    <div class="nhsd-m-emphasis-box" id="entriesFooter">
        <div class="nhsd-a-box nhsd-a-box--border-grey">
            <div class="nhsd-m-emphasis-box__content-box">
                <h1 class="nhsd-t-heading-s nhsd-t-word-break">${name}</h1>
                <p class="nhsd-t-body nhsd-t-word-break">${summary}</p>
                <p class="nhsd-t-body-s nhsd-t-word-break">${publishedDate}</p>
            </div>
        </div>
    </div>
</#macro>
