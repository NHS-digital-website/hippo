<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">
<#-- Note Page must implement uk.nhs.digital.pagination.Paginated -->
<#macro chapterNav page currentPageTitlePrefix="">
    <#assign chapterPagination = page.paginate() />
    <#if chapterPagination??>
        <div class="nhsd-m-chapter-navigation nhsd-!t-bg-pale-grey-80-tint nhsd-!t-display-sticky-bottom nhsd-!t-display-sticky-offset-none">
            <div class="nhsd-m-chapter-navigation__chapter-content">
                <#if chapterPagination.previous??>
                    <div class="nhsd-m-chapter-navigation__previous-chapter">
                        <@hst.link hippobean=chapterPagination.previous.linkedBean var="prevLink">
                            <#if earlyAccessKey?has_content><@hst.param name="key" value="${earlyAccessKey}"/></#if>
                        </@hst.link>
                        <@hst.link hippobean=chapterPagination.previous.linkedBean var="relatedSubjectLink"/>
                        <a class="nhsd-m-chapter-navigation__link"
                           href="${prevLink}"
                           onClick="${getOnClickMethodCall(document.class.name, relatedSubjectLink)}">
                            <div class="nhsd-m-chapter-navigation__link-content">
                                <span class="nhsd-a-icon nhsd-a-icon--size-xs">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"><path d="M7.5,15L1,8l6.5-7L9,2.5L4.8,7H15v2H4.8L9,13.5L7.5,15z"/></svg>
                                </span>
                                <span><span class="nhsd-m-chapter-navigation__link-label">${chapterPagination.previous.title}</span></span>
                            </div>
                        </a>
                    </div>
                </#if>

                <div class="nhsd-m-chapter-navigation__current-chapter">
                    <div>
                        <div><span class="nhsd-!t-font-weight-bold">Current Chapter -</span> ${currentPageTitlePrefix}${page.title}</div>
                        <div>
                            <a class="nhsd-a-link" href="#chapter-index">View all</a>
                        </div>
                    </div>
                </div>

                <#if chapterPagination.next??>
                    <div class="nhsd-m-chapter-navigation__next-chapter">
                        <@hst.link hippobean=chapterPagination.next.linkedBean var="nextLink">
                            <#if earlyAccessKey?has_content><@hst.param name="key" value="${earlyAccessKey}"/></#if>
                        </@hst.link>
                        <@hst.link hippobean=chapterPagination.next.linkedBean var="relatedSubjectLink"/>
                        <a class="nhsd-m-chapter-navigation__link"
                           href="${nextLink}"
                           onClick="${getOnClickMethodCall(document.class.name, relatedSubjectLink)}">
                            <div class="nhsd-m-chapter-navigation__link-content">
                                <span><span class="nhsd-m-chapter-navigation__link-label">${chapterPagination.next.title}</span></span>
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
