<#ftl output_format="HTML">

<#macro calloutBox callout>
    <#assign severity=(callout.severity?has_content?then(callout.severity, "information")) />
    <#assign update=callout.calloutType=="update" />
    <#assign calloutBoxId=callout.calloutType+"-"+callout.severity+"-"+callout.index />

    <#if severity == 'critical'>
        <#assign emphasisType = 'warning'/>
        <#assign borderColour = 'red' />
    <#elseif severity == 'important'>
        <#assign emphasisType = 'important' />
        <#assign borderColour = 'yellow' />
    <#elseif severity == 'information'>
        <#assign emphasisType = 'emphasis' />
        <#assign borderColour = 'blue' />
    </#if>

    <div class="nhsd-m-emphasis-box-wide nhsd-m-emphasis-box--${emphasisType} nhsd-!t-margin-bottom-6" role="complementary" aria-labelledby="callout-box-heading-${calloutBoxId}">
        <div class="nhsd-a-box nhsd-a-box--border-${borderColour}">
            <div class="nhsd-m-emphasis-box__icon-box">
                <span class="nhsd-a-icon nhsd-a-icon--size-xxl">
                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16">
                        <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>
                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 180 180" width="62%" height="62%" x="14%" y="14%">
                            <#if emphasisType == "important">
                                <path d="M124.4,161.7h-8.5L114,82.3h12.1ZM114,174.4h12.1V187H114Z" transform="translate(-11.47 -26.48)"/>
                            <#elseif emphasisType == "warning">
                                <path d="M111.4,178a13.28,13.28,0,0,1,1-5.21,12.72,12.72,0,0,1,2.82-4.27,13,13,0,0,1,4.27-2.82,13.91,13.91,0,0,1,10.42,0,12.85,12.85,0,0,1,7.09,7.09,13.91,13.91,0,0,1,0,10.42,12.85,12.85,0,0,1-7.09,7.09,13.91,13.91,0,0,1-10.42,0,13,13,0,0,1-4.27-2.82,12.72,12.72,0,0,1-2.82-4.27A13.28,13.28,0,0,1,111.4,178Zm24.17-21.42H113.86V71.29h21.71Z" transform="translate(-16.03 -23.97)"/>
                            <#elseif emphasisType == "emphasis">
                                <path d="M120.6,145h4.6c-3,6.2-7,11.7-11.8,16.6c-4.9,4.9-9.5,7.2-14.1,7.2c-3,0-5.7-1.1-7.9-3.3c-2.2-2.1-3.4-5.3-3.4-9.5c0-3.4,1.8-9.9,5.4-19.6l11-29.3c2.6-6.8,3.8-11,3.8-12.5c0-1.3-0.3-2.2-0.9-2.9c-0.7-0.7-1.6-0.9-2.8-0.9c-2.5,0-5.5,2-8.9,5.8c-3.4,3.9-5.9,7.4-7.2,10.5h-4.6c3.6-7,7.5-12.5,12.1-16.6c4.6-4.1,8.8-6,12.6-6c3,0,5.5,1.1,7.4,3c2,2.1,2.9,4.7,2.9,8.2c0,3.4-1.7,9.6-5.1,18.7l-9.2,25c-3.4,9.1-5.1,14.7-5.1,17.1c0,1.8,0.4,3.3,1.1,4.2c0.8,0.9,1.8,1.4,3.3,1.4C108.7,161.9,114.2,156.3,120.6,145z M113.5,42c0-2.4,0.8-4.3,2.5-5.9s3.6-2.5,5.9-2.5c2.4,0,4.3,0.8,5.9,2.5c1.6,1.7,2.5,3.6,2.5,5.9c0,2.4-0.8,4.3-2.5,5.9c-1.6,1.7-3.6,2.5-5.9,2.5s-4.3-0.8-5.9-2.5C114.3,46.2,113.5,44.2,113.5,42z"/>
                            </#if>
                        </svg>
                    </svg>
                </span>
            </div>

            <div class="nhsd-m-emphasis-box__content-box">
                <p class="nhsd-t-heading-s nhsd-t-word-break" id="nhsd-t-heading-s-${calloutBoxId}">
                    ${callout.title}
                </p>

                <#if callout.content?has_content>
                    <div class="nhsd-t-word-break">
                        <@hst.html hippohtml=callout.content contentRewriter=brContentRewriter />
                    </div>
                </#if>
            </div>
        </div>
    </div>
</#macro>
