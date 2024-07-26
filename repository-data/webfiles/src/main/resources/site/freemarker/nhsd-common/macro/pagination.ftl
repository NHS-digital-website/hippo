<#ftl output_format="HTML">

<#macro pagination>
    <#if pageable.totalPages gt 1>
        <div class="nhsd-m-character-block-list">
            <nav>
                <ul>

                    <#if pageable.currentPage gt 1>
                        <@hst.renderURL var="pageUrl">
                            <@hst.param name="page" value="${pageable.currentPage - 1}"/>
                        </@hst.renderURL>

                        <li>
                            <#outputformat "undefined">
                                <a class="nhsd-a-character-block" href="${pageUrl}" aria-label="Back">
                                    <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                            <path d="M7.5,15L1,8l6.5-7L9,2.5L4.8,7H15v2H4.8L9,13.5L7.5,15z"/>
                                        </svg>
                                    </span>
                                </a>
                            </#outputformat>
                        </li>
                    </#if>

                    <#list pageable.pageNumbersArray as pageNr>
                        <@hst.renderURL var="pageUrl">
                            <@hst.param name="page" value="${pageNr}"/>
                        </@hst.renderURL>

                        <li>
                            <#outputformat "undefined"><a class="nhsd-a-character-block ${(pageable.currentPage == pageNr)?then("nhsd-a-character-block--active", "")}" href="${pageUrl}">${pageNr}</a></#outputformat>
                        </li>
                    </#list>

                    <#if pageable.next>
                        <@hst.renderURL var="pageUrl">
                            <@hst.param name="page" value="${pageable.currentPage + 1}"/>
                        </@hst.renderURL>

                        <li>
                            <#outputformat "undefined">
                                <a class="nhsd-a-character-block" href="${pageUrl}" aria-label="Next">
                                    <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                            <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                        </svg>
                                    </span>
                                </a>
                            </#outputformat>
                        </li>
                    </#if>
                </ul>
            </nav>
        </div>
    </#if>
</#macro>
