<#if pageable??>
    <div class="pagination-container">
        <ul class="pagination">
            <#if pageable.totalPages gt 1>
                <#list pageable.pageNumbersArray as pageNr>
                    <@hst.renderURL var="pageUrl">
                        <@hst.param name="page" value="${pageNr}"/>
                        <@hst.param name="pageSize" value="${pageable.pageSize}"/>
                    </@hst.renderURL>
                    <#if pageNr_index==0>
                        <#if pageable.previous>
                            <@hst.renderURL var="pageUrlFirst">
                                <@hst.param name="page" value="1"/>
                                <@hst.param name="pageSize" value="${pageable.pageSize}"/>
                            </@hst.renderURL>
                            <li><a href="${pageUrlFirst}" title="First"><span class="next">First</span></a></li>
                            <@hst.renderURL var="pageUrlPrevious">
                                <@hst.param name="page" value="${pageable.previousPage}"/>
                                <@hst.param name="pageSize" value="${pageable.pageSize}"/>
                            </@hst.renderURL>
                            <li><a href="${pageUrlPrevious}" title="Previous"><span class="prev">Previous</span></a></li>
                        </#if>
                    </#if>
                    <#if pageable.currentPage == pageNr>
                        <li><span class="current">${pageNr}</span></li>
                    <#else >
                        <li><a href="${pageUrl}" title="Page ${pageNr}">${pageNr}</a></li>
                    </#if>
                    <#if !pageNr_has_next>
                        <#if pageable.next>
                            <@hst.renderURL var="pageUrlNext">
                                <@hst.param name="page" value="${pageable.nextPage}"/>
                                <@hst.param name="pageSize" value="${pageable.pageSize}"/>
                            </@hst.renderURL>
                            <li><a href="${pageUrlNext}" title="Next"><span class="next">Next</span></a></li>
                            <@hst.renderURL var="pageUrlLast">
                                <@hst.param name="page" value="${pageable.totalPages}"/>
                                <@hst.param name="pageSize" value="${pageable.pageSize}"/>
                            </@hst.renderURL>
                            <li><a href="${pageUrlLast}" title="Last"><span class="next">Last</span></a></li>
                        </#if>
                    </#if>
                </#list>
            </#if>
        </ul>
    </div>
</#if>
