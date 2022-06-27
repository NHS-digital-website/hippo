<#ftl output_format="HTML">
<div class="pagination">
    <ul class="pagination-list">
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
                        <li class="pagination-list__item pagination-list__item--first">
                            <#outputformat "undefined"><a href="${pageUrlFirst}" title="First"><span class="next">First</span></a></#outputformat>
                        </li>
                        <@hst.renderURL var="pageUrlPrevious">
                            <@hst.param name="page" value="${pageable.previousPage}"/>
                            <@hst.param name="pageSize" value="${pageable.pageSize}"/>
                        </@hst.renderURL>
                        <li class="pagination-list__item pagination-list__item--previous">
                            <#outputformat "undefined"><a href="${pageUrlPrevious}" title="Previous"><span class="prev">Previous</span></a></#outputformat>
                        </li>
                    </#if>
                </#if>
                <#if pageable.currentPage == pageNr>
                    <li class="pagination-list__item pagination-list__item--current"><span class="">${pageNr}</span></li>
                <#else >
                    <li class="pagination-list__item">
                        <#outputformat "undefined"><a href="${pageUrl}" title="Page ${pageNr}">${pageNr}</a></#outputformat>
                    </li>
                </#if>
                <#if !pageNr_has_next>
                    <#if pageable.next>
                        <@hst.renderURL var="pageUrlNext">
                            <@hst.param name="page" value="${pageable.nextPage}"/>
                            <@hst.param name="pageSize" value="${pageable.pageSize}"/>
                        </@hst.renderURL>
                        <li class="pagination-list__item pagination-list__item--next">
                            <#outputformat "undefined"><a href="${pageUrlNext}" title="Next"><span class="next">Next</span></a></#outputformat>
                        </li>
                    </#if>
                </#if>
            </#list>
        </#if>
    </ul>
</div>
