<#ftl output_format="HTML">

<#-- Note Page must implement uk.nhs.digital.pagination.Paginated -->
<#macro pagination page>
    <#assign pagenation = page.paginate() />
    <#if pagination??>
        <nav class="digital-pagination" role="navigation" aria-label="Pagination">
            <#if pagenation.previous??>
            <ul class="digital-pagination-list">
                <li class="digital-pagination-list-item-previous">
                    <a class="digital-pagination-link"
                       href="<@hst.link hippobean=pagenation.previous.linkedBean />">
                        <span class="digital-pagination-link-title"><img aria-hidden="true" alt="Left Arrow" src="<@hst.webfile path="/images/pagination/left-arrow.svg"/>"/> Previous</span>
                        <span class="digital-pagination-link-hidden"> : </span>
                        <span class="digital-pagination-link-page">${pagenation.previous.title}</span>
                    </a>
                </li>
                </#if>
                <#if pagenation.next??>
                    <li class="digital-pagination-list-item-next">
                        <a class="digital-pagination-link"
                           href="<@hst.link hippobean=pagenation.next.linkedBean />">
                            <span class="digital-pagination-link-title">Next <img aria-hidden="true" alt="Right Arrow" src="<@hst.webfile path="/images/pagination/right-arrow.svg"/>"/></span>
                            <span class="digital-pagination-link-hidden"> : </span>
                            <span class="digital-pagination-link-page">${pagenation.next.title}</span>
                        </a>
                    </li>
                </#if>
            </ul>
        </nav>
    </#if>
</#macro>
