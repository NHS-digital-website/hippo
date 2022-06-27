<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">
<#-- Note Page must implement uk.nhs.digital.pagination.Paginated -->
<#macro chapterNav page currentPageTitlePrefix="">
    <#assign chapterPagination = page.paginate() />
    <#if chapterPagination??>
        <div class="nhsd-o-chapter-navigation nhsd-!t-bg-pale-grey-80-tint">
            <div class="nhsd-a-box">
                <#if chapterPagination.previous??>
                    <@hst.link hippobean=chapterPagination.previous.linkedBean var="relatedSubjectLink"/>
                    <div class="nhsd-o-chapter-navigation__previous-chapter">
                        <a class="nhsd-m-chapter"
                           href="<@hst.link hippobean=chapterPagination.previous.linkedBean>
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
                            <p class="nhsd-m-chapter__link nhsd-t-body-s">${chapterPagination.previous.title}</p>
                        </a>
                    </div>
                </#if>

                <div class="nhsd-o-chapter-navigation__current-chapter">
                    <p class="nhsd-t-heading-xs nhsd-!t-margin-bottom-0">Current Chapter</p>
                    <p class="nhsd-t-body-s nhsd-!t-margin-bottom-0">${currentPageTitlePrefix}${page.title}</p>
                    <div class="nhsd-!t-margin-bottom-0 nhsd-t-body-s">
                        <a class="nhsd-a-link" href="#chapter-index">View all
                        <span class="nhsd-t-sr-only"></span>
                        </a>
                    </div>
                </div>

                <#if chapterPagination.next??>
                    <@hst.link hippobean=chapterPagination.next.linkedBean var="relatedSubjectLink"/>
                    <div class="nhsd-o-chapter-navigation__next-chapter">
                        <a class="nhsd-m-chapter nhsd-m-chapter--right"
                           href="<@hst.link hippobean=chapterPagination.next.linkedBean>
                                <#if earlyAccessKey?has_content><@hst.param name="key" value="${earlyAccessKey}"/></#if>
                           </@hst.link>"
                           onClick="${getOnClickMethodCall(document.class.name, relatedSubjectLink)}"
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
                            <p class="nhsd-m-chapter__link nhsd-t-body-s">${chapterPagination.next.title}</p>
                        </a>
                    </div>
                </#if>
            </div>
            <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xxs" />
        </div>
    </#if>
</#macro>
