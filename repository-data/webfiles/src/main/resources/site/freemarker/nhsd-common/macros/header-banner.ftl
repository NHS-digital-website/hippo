<#ftl output_format="HTML">

<#macro headerBanner banner button1={} button2={} color=false textAlignment=false digiblockPosition=false>
    <#assign bgClass="">
    <#assign textClass="">

    <#assign digiblock1PositionClass="nhsd-a-digiblocks--pos-bl">
    <#assign digiblock2PositionClass="nhsd-a-digiblocks--pos-tr">

    <#assign digiblock1ColorClass="">

    <#if colour??>
        <#if colour == "Dark Grey">
            <#assign bgClass="nhsd-!t-bg-black">
            <#assign textClass="nhsd-o-hero--light-text">
            <#assign digiblock1ColorClass="nhsd-a-digiblocks--col-black">
        <#elseif colour == "Blue">
            <#assign bgClass="nhsd-!t-bg-bright-blue-20-tint">
            <#assign digiblock1ColorClass="nhsd-a-digiblocks--col-blue">
        <#elseif colour == "Yellow">
            <#assign bgClass="nhsd-!t-bg-yellow-20-tint">
            <#assign digiblock1ColorClass="nhsd-a-digiblocks--col-yellow">
        </#if>
    </#if>

    <#assign textAlignmentClass="">
    <#assign buttonAlignmentClass="nhsd-!t-text-align-left">
    <#if alignment == "Centre">
        <#assign textAlignmentClass="nhsd-!t-text-align-center">
        <#assign buttonAlignmentClass="">
    </#if>

    <#if digiblockposition == "Right">
        <#assign digiblock1PositionClass="">
    </#if>

    <#assign content="">
    <#if banner.content??>
        <#assign content = banner.content>
    <#elseif banner.summary??>
        <#assign content = banner.summary>
    </#if>

    <#assign displayButton1 = false>
    <#assign displayButton2 = false>

    <#if button1.url?has_content && button1.text?has_content>
        <#assign displayButton1 = true>
    </#if>
    <#if button2.url?has_content && button2.text?has_content>
        <#assign displayButton2 = true>
    </#if>

    <#if banner??>
        <div class="nhsd-o-hero ${bgClass} ${textClass} ${textAlignmentClass} nhsd-!t-margin-bottom-6">
            <div class="nhsd-t-grid">
                <div class="nhsd-t-row <#if digiblock1PositionClass?has_content>nhsd-t-row--centred</#if>">
                    <div class="<#if digiblock1PositionClass?has_content>
                        nhsd-t-col-xs-9 nhsd-t-col-s-10 nhsd-t-col-m-11 nhsd-t-col-l-8
                        <#else>
                        nhsd-t-col-xs-12 nhsd-t-col-s-11 nhsd-t-col-m-12 nhsd-t-col-l-10
                        </#if> nhsd-!t-text-align-l-left"
                        >
                        <h1 class="nhsd-t-heading-xxl nhsd-!t-margin-bottom-6 nhsd-!t-margin-top-6" data-uipath="document.title">${banner.title}</h1>
                        <div class="nhsd-t-heading-s nhsd-!t-margin-bottom-6" data-uipath="document.summary"><@hst.html hippohtml=content contentRewriter=stripTagsContentRewriter/></div>

                        <#if displayButton1 && displayButton2><nav class="nhsd-m-button-nav nhsd-m-button-nav--condensed ${buttonAlignmentClass} nhsd-!t-margin-bottom-6"></#if>
                            <#if displayButton1>
                                <a class="nhsd-a-button" href="${button1.url}">
                                    <span class="nhsd-a-button__label">${button1.text}</span>
                                </a>
                            </#if>
                            <#if displayButton2>
                                <a class="nhsd-a-button nhsd-a-button--invert" href="${button2.url}">
                                    <span class="nhsd-a-button__label">${button2.text}</span>
                                </a>
                            </#if>
                        <#if displayButton1 && displayButton2></nav></#if>
                    </div>
                </div>
            </div>

            <#if digiblock1PositionClass?has_content>
                <div class="nhsd-a-digiblocks ${digiblock1PositionClass} ${digiblock1ColorClass}">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 550 550"><g><g transform="translate(222, 224)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(328.5, 367.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g><g transform="translate(151, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon></g><g transform="translate(80, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g></g><g><g transform="translate(186.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g><g transform="translate(186.5, 285.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(222, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(9, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(257.5, 449.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon></g></g><g><g transform="translate(186.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(399.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(222, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g></g><g><g transform="translate(328.5, 162.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon></g><g transform="translate(399.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(115.5, 162.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(186.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(328.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(257.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon></g></g><g><g transform="translate(328.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon></g><g transform="translate(257.5, 285.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(44.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(151, 265)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g></g><g><g transform="translate(435, 142)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g></g><g><g transform="translate(328.5, 39.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(222, 19)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(257.5, 80.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon></g></g></svg>
                </div>
            </#if>

            <#if digiblock2PositionClass?has_content>
                <div class="nhsd-a-digiblocks ${digiblock2PositionClass} ${digiblock1ColorClass}">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 550 550"><g><g transform="translate(222, 224)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(328.5, 367.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g><g transform="translate(151, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon></g><g transform="translate(80, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g></g><g><g transform="translate(186.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g><g transform="translate(186.5, 285.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(222, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(9, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(257.5, 449.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon></g></g><g><g transform="translate(186.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(399.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(222, 306)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon></g></g><g><g transform="translate(328.5, 162.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon></g><g transform="translate(399.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(115.5, 162.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(186.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(328.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(257.5, 326.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon></g></g><g><g transform="translate(328.5, 244.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon></g><g transform="translate(257.5, 285.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g><g transform="translate(44.5, 203.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon></g><g transform="translate(151, 265)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g></g><g><g transform="translate(435, 142)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon></g></g><g><g transform="translate(328.5, 39.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon></g><g transform="translate(222, 19)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon></g><g transform="translate(257.5, 80.5)"><polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon><polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon><polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon></g></g></svg>
                </div>
            </#if>
        </div>
    </#if>
</#macro>
