<#ftl output_format="HTML">


<#-- Return an inline SVG icon by ID -->
<#macro nhsdIcon id>
    <#if !size?has_content>
        <#local size = 's' />
    </#if>

    <#if id?has_content>
        <#if !raw?has_content || !raw><span class="nhsd-a-icon nhsd-a-icon--size-${size}"></#if>
        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
            <#if id == 'search'>
                <path d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/>
            </#if>
        </svg>
        <#if !raw?has_content || !raw></span></#if>
    </#if>
</#macro>
