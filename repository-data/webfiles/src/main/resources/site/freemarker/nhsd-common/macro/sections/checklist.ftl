<#ftl output_format="HTML">

<#macro checklist section>

    <div class="nhsd-m-checklist nhsd-!t-margin-bottom-6">
        <#if section.heading?has_content>
            <span class="nhsd-t-heading-s nhsd-!t-col-white nhsd-m-checklist__header" data-uipath="website.contentblock.checklist.heading">${section.heading}</span>
        </#if>

        <div class="nhsd-m-checklist__list-container <#if !section.heading?has_content>no-heading</#if>">

            <#if section.icon?trim == 'Bullet'>
            <ul class="nhsd-t-list nhsd-t-list--bullet nhsd-!t-margin-bottom-0">
            <#else>
            <ul class="nhsd-t-list nhsd-!t-margin-bottom-0">
            </#if>

                <#list section.listentries as entry>
                    <#if section.icon?trim == 'Bullet'>
                    <li>
                    <#else>
                    <li class="nhsd-m-checklist__icon-list">
                    </#if>

                        <span class="nhsd-m-checklist__icon" style="vertical-align:text-top;">
                            <#if section.icon?trim == 'Tick'>
                                <span class="nhsd-a-icon nhsd-a-icon--size-xs nhsd-a-icon--col-green">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                        <path d="M16,3.5L5.5,14L0,8.5L1.5,7l4,4l9-9L16,3.5z" />
                                    </svg>
                                </span>
                            <#elseif section.icon?trim == 'Cross'>
                                <span class="nhsd-a-icon nhsd-a-icon--size-xs nhsd-a-icon--col-red">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                        <polygon points="13.9,1 8,6.9 2.1,1 1,2.1 6.9,8 1,13.9 2.1,15 8,9.1 13.9,15 15,13.9 9.1,8 15,2.1 "/>
                                    </svg>
                                </span>
                            <#elseif section.icon?trim == 'Custom' && section.customicon?has_content>
                                <@hst.link hippobean=section.customicon.original fullyQualified=true var="leadImage" />
                                <span class="nhsd-a-icon nhsd-a-icon--size-xs">
                                    <#if leadImage?ends_with("svg")>
                                        <img src="${leadImage?replace("/binaries", "/svg-magic/binaries")}?colour=005eb8" alt="Custom image" />
                                    <#else>
                                        <img src="${leadImage}" alt="Custom image" />
                                    </#if>
                                </span>
                            </#if>
                        </span>

                        <span>
                            <@hst.html hippohtml=entry contentRewriter=stripTagsWithLinksContentRewriter/>
                        </span>
                    </li>
                </#list>
            </ul>
        </div>
    </div>
</#macro>
