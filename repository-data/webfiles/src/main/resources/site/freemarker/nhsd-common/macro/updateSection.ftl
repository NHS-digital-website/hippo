<#ftl output_format="HTML">

<#include "component/calloutBox.ftl">

<#macro updateSection updates>
    <div class="nhsd-!t-margin-bottom-6" data-test-section="updates">
        <#assign item = {} />
        <#list updates as update>
            <#assign item += update />
            <#assign item += {"calloutType":"update", "index":update?index} />
            <div class="nhsd-!t-margin-bottom-3">
                <@calloutBox item />
            </div>
        </#list>
    </div>
</#macro>
