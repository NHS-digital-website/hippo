<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../nhsd-common/macro/heroes/hero-options.ftl">

<#assign heroOptions = getHeroOptions(banner) />

<div class="nhsd-m-emphasis-box" id="entriesFooter">
    <div class="nhsd-a-box nhsd-a-box--border-grey">
        <div class="nhsd-m-emphasis-box__content-box">
            <h5 class="nhsd-t-heading-s nhsd-t-word-break">${banner.title}</h5>
            <@hst.html var="htmlSummary" hippohtml=heroOptions.summary />
            ${htmlSummary?no_esc}
        </div>
    </div>
</div>
