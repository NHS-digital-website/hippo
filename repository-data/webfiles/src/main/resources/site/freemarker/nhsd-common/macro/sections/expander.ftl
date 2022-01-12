<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Expander" -->

<#macro expander section heading="" content="">
    <div class="nhsd-m-expander ${(section.audience == "Most people")?then("nhsd-!t-margin-bottom-6", "nhsd-!t-margin-bottom-0")}" >
        <div class= "nhsd-a-box
            <#if section != "" && section.audience?? && section.audience == "Most people">
                nhsd-a-box--bg-light-grey
            <#else>
            nhsd-!t-padding-top-0
            </#if>"
        >
            <details>
                <summary class="nhsd-m-expander__heading-container" aria-label="${section.heading}">
                    <span class="nhsd-m-expander__icon nhsd-!t-margin-right-1">
                        <span class="nhsd-a-icon nhsd-a-icon--size-xs">
                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                <path d="M12,8l-6.5,7L4,13.5L9.2,8L4,2.5L5.5,1L12,8z"/>
                            </svg>
                        </span>
                    </span>
                    <span class="nhsd-m-expander__heading nhsd-t-body">
                        <#if heading?has_content>
                            ${heading}
                        <#else>
                            ${section.heading}
                        </#if>
                    </span>
                </summary>
                <div class="nhsd-m-expander__content-container">
                    <#if content?has_content>
                        <@hst.html hippohtml=content contentRewriter=brContentRewriter />
                    <#else>
                        <@hst.html hippohtml=section.content contentRewriter=brContentRewriter />
                    </#if>
                </div>
            </details>
        </div>
    </div>
</#macro>
