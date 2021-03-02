<#ftl output_format="HTML">
<div class="nhsd-m-character-block-list">
    <ul>
        <#if pageable.totalPages gt 1>
            <#list pageable.pageNumbersArray as pageNr>
                <@hst.renderURL var="pageUrl">
                    <@hst.param name="page" value="${pageNr}"/>
                    <@hst.param name="pageSize" value="${pageable.pageSize}"/>
                </@hst.renderURL>
                <#if pageNr_index==0>
                    <#if pageable.previous>
                        <@hst.renderURL var="pageUrlPrevious">
                            <@hst.param name="page" value="${pageable.previousPage}"/>
                            <@hst.param name="pageSize" value="${pageable.pageSize}"/>
                        </@hst.renderURL>
                        <li>
                            <#outputformat "undefined"><a class="nhsd-a-character-block" href="${pageUrlPrevious}" title="Previous" aria-label="Back">
                                <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                        <path d="M7.5,15L1,8l6.5-7L9,2.5L4.8,7H15v2H4.8L9,13.5L7.5,15z"/>
                                    </svg>
                                </span>
                            </a></#outputformat>
                        </li>
                    </#if>
                </#if>
                <#if pageable.currentPage == pageNr>
                    <li>
                        <a class="nhsd-a-character-block nhsd-a-character-block--active">
                            ${pageNr}
                        </a>
                    </li>
                <#else >
                    <li>
                        <#outputformat "undefined"><a class="nhsd-a-character-block" href="${pageUrl}" title="Page ${pageNr}"></#outputformat>
                            ${pageNr}
                        </a>
                    </li>
                </#if>
                <#if !pageNr_has_next>
                    <#if pageable.next>
                        <@hst.renderURL var="pageUrlNext">
                            <@hst.param name="page" value="${pageable.nextPage}"/>
                            <@hst.param name="pageSize" value="${pageable.pageSize}"/>
                        </@hst.renderURL>
                        <li>
                            <#outputformat "undefined"><a class="nhsd-a-character-block" href="${pageUrlNext}" title="Next" aria-label="Next">
                                <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                        <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                    </svg>
                                </span>
                            </a></#outputformat>
                        </li>
                    </#if>
                </#if>
            </#list>
        </#if>
    </ul>
</div>
