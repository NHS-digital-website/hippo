<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">
<#include "../../macro/toolkit/organisms/filterMenu.ftl">

<div class="nhsd-t-grid nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-6">
    <div id="FeedhubFilterMenu" data-variant="feedhub filter menu">
        <@nhsdFilterMenu/>
    </div>
    <div id="CyberAlertHub" data-variant="cyber alert filter menu">
        <@nhsdFilterMenu/>
    </div>
</div>