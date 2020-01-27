<#ftl output_format="HTML">

<#macro updateBox update>
    <#assign severity=(update.severity?has_content?then(update.severity, "information")) />
    <div class="update-box update-box--${severity}" role="complementary" aria-labelledby="update-box-heading-information">
        
        <div class="update-box__icon-wrapper">
            <#if update.severity == "important">
                <svg class="update-box__icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 217.07 187.02" preserveAspectRatio="xMidYMid meet" role="presentation" focusable="false">
                    <path d="M42.7,148.5,73.9,95.9l36.8-62.1a11,11,0,0,1,18.8,0L225,195.1a10.86,10.86,0,0,1-9.4,16.4H24.4A10.88,10.88,0,0,1,15,195.1h0C23,181.7,35.4,160.8,42.7,148.5Z" transform="translate(-11.47 -26.48)"/>
                    <path fill="#005EB8" d="M124.4,161.7h-8.5L114,82.3h12.1ZM114,174.4h12.1V187H114Z" transform="translate(-11.47 -26.48)"/>
                </svg>
            <#elseif update.severity == "critical">
                <svg class="update-box__icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 215.92 186.68" preserveAspectRatio="xMidYMid meet" role="presentation" focusable="false">
                    <path d="M47,145.78,19.54,192.3h0a10.83,10.83,0,0,0,9.33,16.35H219.1a10.84,10.84,0,0,0,10.84-10.84,10.71,10.71,0,0,0-1.5-5.51l-95.12-161a10.84,10.84,0,0,0-18.67,0L78,93.29Z" transform="translate(-16.03 -23.97)"/>
                    <path d="M111.4,178a13.28,13.28,0,0,1,1-5.21,12.72,12.72,0,0,1,2.82-4.27,13,13,0,0,1,4.27-2.82,13.91,13.91,0,0,1,10.42,0,12.85,12.85,0,0,1,7.09,7.09,13.91,13.91,0,0,1,0,10.42,12.85,12.85,0,0,1-7.09,7.09,13.91,13.91,0,0,1-10.42,0,13,13,0,0,1-4.27-2.82,12.72,12.72,0,0,1-2.82-4.27A13.28,13.28,0,0,1,111.4,178Zm24.17-21.42H113.86V71.29h21.71Z" transform="translate(-16.03 -23.97)"/>
                </svg>
            <#else>
                <svg class="update-box__icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 169 169" preserveAspectRatio="xMidYMid meet" role="presentation" focusable="false">
                    <path fill="#005EB8" d="M127.7,142.7h2.5a46.78,46.78,0,0,1-9,12.6c-3.7,3.7-7.2,5.5-10.7,5.5a8.16,8.16,0,0,1-6-2.5c-1.7-1.6-2.6-4-2.6-7.2,0-2.6,1.4-7.5,4.1-14.9l8.4-22.3c2-5.2,2.9-8.4,2.9-9.5a2.92,2.92,0,0,0-.7-2.2,2.82,2.82,0,0,0-2.1-.7c-1.9,0-4.2,1.5-6.8,4.4a31.92,31.92,0,0,0-5.5,8H99.7a42,42,0,0,1,9.2-12.6c3.5-3.1,6.7-4.6,9.6-4.6a7.38,7.38,0,0,1,5.6,2.3,8.56,8.56,0,0,1,2.2,6.2c0,2.6-1.3,7.3-3.9,14.2l-7,19c-2.6,6.9-3.9,11.2-3.9,13a5.58,5.58,0,0,0,.8,3.2,3.1,3.1,0,0,0,2.5,1.1C118.6,155.6,122.8,151.3,127.7,142.7Zm-5.4-78.3a5.83,5.83,0,0,1,1.9-4.5,6.36,6.36,0,0,1,4.5-1.9,5.83,5.83,0,0,1,4.5,1.9,6.36,6.36,0,0,1,1.9,4.5,6.36,6.36,0,0,1-10.9,4.5A6.13,6.13,0,0,1,122.3,64.4Z" transform="translate(-35.5 -32)"/>
                    <circle cx="84.5" cy="84.5" r="82.5"/>
                </svg>
            </#if>
        </div>

        <div class="update-box__content">
            <h2 class="update-box__content-heading" id="update-box-heading-information">${update.title}</h2>
            <div class="update-box__content-description">
                <@hst.html hippohtml=update.content contentRewriter=gaContentRewriter />
            </div>
        </div>
    </div>
</#macro>
