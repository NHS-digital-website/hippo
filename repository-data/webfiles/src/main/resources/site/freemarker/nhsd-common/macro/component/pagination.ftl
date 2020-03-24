<#ftl output_format="HTML">

<#-- Note Page must implement uk.nhs.digital.pagination.Paginated -->
<#macro pagination page>
    <#assign pagination = page.paginate() />
    <#if (pagination)?has_content>
        <#if (pagination.previous)?has_content && (pagination.next)?has_content>
        <div class="nhsd-o-chapter-navigation nhsd-o-chapter-navigation--footer">
            <div class="nhsd-a-box nhsd-!t-padding-0">
        </#if>
                <#if (pagination.previous)?has_content>
                    <@hst.link hippobean=pagination.previous.linkedBean var="relatedSubjectLink"/>
                    <div class="<#if (pagination.next)?has_content>nhsd-o-chapter-navigation__previous-chapter</#if>">
                        <a class="nhsd-m-chapter"
                           href="<@hst.link hippobean=pagination.previous.linkedBean>
                                    <#if earlyAccessKey?has_content><@hst.param name="key" value="${earlyAccessKey}"/></#if>
                                 </@hst.link>"
                           onClick="${getOnClickMethodCall(document.class.name, relatedSubjectLink)}"
                           aria-label="Previous Chapter"
                        >
                            <div class="nhsd-m-chapter__content">
                                <span class="nhsd-m-chapter__icon">
                                    <span class="nhsd-a-icon nhsd-a-icon--size-xs">
                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                            <path d="M7.5,15L1,8l6.5-7L9,2.5L4.8,7H15v2H4.8L9,13.5L7.5,15z"/>
                                        </svg>
                                    </span>
                                </span>
                                <p class="nhsd-t-heading-xs nhsd-!t-margin-bottom-0">Previous Chapter</p>
                            </div>
                            <p class="nhsd-m-chapter__link nhsd-t-body-s">${pagination.previous.title}</p>
                        </a>
                    </div>
                </#if>
                <#if (pagination.next)?has_content>
                    <@hst.link hippobean=pagination.next.linkedBean var="relatedSubjectLinkNext"/>
                    <div class="<#if (pagination.previous)?has_content>nhsd-o-chapter-navigation__next-chapter</#if>">
                        <a class="nhsd-m-chapter nhsd-m-chapter--right"
                           href="<@hst.link hippobean=pagination.next.linkedBean>
                                    <#if earlyAccessKey?has_content><@hst.param name="key" value="${earlyAccessKey}"/></#if>
                                 </@hst.link>"
                           onClick="${getOnClickMethodCall(document.class.name, relatedSubjectLinkNext)}"
                           aria-label="Next Chapter"
                        >
                            <div class="nhsd-m-chapter__content">
                                <p class="nhsd-t-heading-xs nhsd-!t-margin-bottom-0">Next Chapter</p>
                                <span class="nhsd-m-chapter__icon">
                                    <span class="nhsd-a-icon nhsd-a-icon--size-xs">
                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                            <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                        </svg>
                                    </span>
                                </span>
                            </div>
                            <p class="nhsd-m-chapter__link nhsd-t-body-s">${pagination.next.title}</p>
                        </a>
                    </div>
                </#if>
        <#if (pagination.previous)?has_content && (pagination.next)?has_content>
            </div>
        </div>
        </#if>
    </#if>
</#macro>
