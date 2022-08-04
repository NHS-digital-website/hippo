<#ftl output_format="HTML">

<#-- Note Page must implement uk.nhs.digital.pagination.Paginated -->
<#macro pagination page>
    <#assign pagination = page.paginate() />
    <#if (pagination)?has_content>
        <div class="nhsd-m-chapter-navigation nhsd-m-chapter-navigation--footer">
            <div class="nhsd-m-chapter-navigation__chapter-content">
                <#if (pagination.previous)?has_content>
                    <div class="nhsd-m-chapter-navigation__previous-chapter">
                        <@hst.link hippobean=pagination.previous.linkedBean var="prevLink">
                            <#if earlyAccessKey?has_content><@hst.param name="key" value="${earlyAccessKey}"/></#if>
                        </@hst.link>
                        <a class="nhsd-m-chapter-navigation__link"
                           href="${prevLink}"
                           onClick="${getOnClickMethodCall(document.class.name, prevLink)}"
                        >
                            <div class="nhsd-m-chapter-navigation__link-content">
                                <span class="nhsd-a-icon nhsd-a-icon--size-xs">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"><path d="M7.5,15L1,8l6.5-7L9,2.5L4.8,7H15v2H4.8L9,13.5L7.5,15z"/></svg>
                                </span>
                                <span>
                                    <span class="nhsd-!t-font-weight-bold">Previous</span><br />
                                    <span class="nhsd-m-chapter-navigation__link-label">${pagination.previous.title}</span>
                                </span>
                            </div>
                        </a>
                    </div>
                </#if>
                <#if (pagination.next)?has_content>
                    <div class="nhsd-m-chapter-navigation__next-chapter">
                        <@hst.link hippobean=pagination.next.linkedBean var="nextLink">
                            <#if earlyAccessKey?has_content><@hst.param name="key" value="${earlyAccessKey}"/></#if>
                        </@hst.link>
                        <a class="nhsd-m-chapter-navigation__link"
                           href="${nextLink}"
                           onClick="${getOnClickMethodCall(document.class.name, nextLink)}"
                        >
                            <div class="nhsd-m-chapter-navigation__link-content">
                                <span>
                                    <span class="nhsd-!t-font-weight-bold">Next</span><br />
                                    <span class="nhsd-m-chapter-navigation__link-label">${pagination.next.title}</span>
                                </span>
                                <span class="nhsd-a-icon nhsd-a-icon--size-xs">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/></svg>
                                </span>
                            </div>
                        </a>
                    </div>
                </#if>
            </div>
        </div>
    </#if>
</#macro>
