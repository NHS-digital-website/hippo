<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#setting date_format="dd MMMM yyyy">

<#-- @ftlvariable name="item" type="uk.nhs.digital.ps.beans.Publication" -->
<#if pageable?? && pageable.items?has_content>
    <div class="content-box content-box--secondary content-box--visible-sections content-box--latest-publications">
        <div class="grid-row">
            <div class="column">
                <h3>Latest publications</h3>
            </div>
        </div>

        <div class="grid-row">
            <ol class="list--reset cta-list cta-list--grid cta-list--fitted-grid">
                <#list pageable.items as item>
                <li class="column column--one-third ${item?counter?switch(1, "column--left", 2, "column--center", 3, "column--right")}">
                    <article class="cta">
                        <#if hstRequestContext.preview>
                        <div style="position:relative">
                            <@hst.cmseditlink hippobean=item />
                        </div>
                        </#if>
                        <h2 class="cta__title"><a href="<@hst.link hippobean=item/>">${item.title}</a></h2>
                        <#assign nomimaldate = item.getNominalPublicationDate()/>
                        <#if !nomimaldate.isRestricted()>
                        <#assign pubdate = nomimaldate.dayOfMonth + " " + nomimaldate.month +" "+ nomimaldate.year?c/>
                        <time class="cta__meta" datetime="${pubdate?date?iso_utc}">${pubdate?date}</time>
                        </#if>
                    </article>
                </li>
                </#list>
            </ol>
        </div>

        <div class="grid-row">
            <div class="column">
                <a href="search?sort=date&amp;area=data" class="link">View the latest publications</a>
            </div>
        </div>
    </div>
</#if>
