<#ftl output_format="HTML">

<#macro mergerBanner>
    <@hst.setBundle basename="rb.merger-banner"/>
    <@fmt.message key="banner.colour" var="backgroundColor" />
    <@fmt.message key="banner.enabled" var="bannerEnabled" />

    <#assign bgColour = backgroundColor?upper_case />
    <#if !(['#7C2855', '#425563', '#E8EDEE', '#A0DAF2'])?seq_contains(bgColour)>
        <#assign bgColour = '#7C2855' />
    </#if>

    <#assign textColor = '#FFFFFF' />
    <#if (['#E8EDEE', '#A0DAF2'])?seq_contains(bgColour) >
        <#assign textColor = '#000000' />
    </#if>

    <#if bannerEnabled == "true">
        <div class="nhsd-!t-bg nhsd-!t-colour" style="--bg-colour: ${bgColour}; --content-colour: ${textColor}; --link-colour: ${textColor}">
            <div class="nhsd-t-grid">
                <div class="nhsd-t-row">
                    <div class="nhsd-t-col nhsd-!t-padding-top-6 nhsd-!t-padding-bottom-6"><@fmt.message key="banner.html" /></div>
                </div>
            </div>
        </div>
    </#if>
</#macro>
