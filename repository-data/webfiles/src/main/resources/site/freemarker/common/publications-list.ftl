<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#setting date_format="dd MMMM yyyy">

<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />

<#-- @ftlvariable name="item" type="uk.nhs.digital.ps.beans.Publication" -->
<#if pageable?? && pageable.items?has_content>
    <div class="article-section__box article-section__box--latest-publications">
        <div class="grid-row">
            <div class="column">
                <h3>Latest publications</h3>
            </div>
        </div>
        <ol class="grid-row list-of-articles list-of-articles--3-columns">
            <#list pageable.items as item>
            <li class="column column--one-third">
                <article>
                    <#if hstRequestContext.preview>
                    <div style="position:relative">
                        <@hst.cmseditlink hippobean=item />
                    </div>
                    </#if>
                    <h2><a href="<@hst.link hippobean=item/>">${item.title}</a></h2>
                    <#assign nomimaldate = item.getNominalPublicationDate()/>
                    <#if !nomimaldate.isRestricted()>
                    <#assign pubdate = nomimaldate.dayOfMonth + " " + nomimaldate.month +" "+ nomimaldate.year?c/>
                        <time datetime="${pubdate?date?iso_utc}">${pubdate?date}</time>
                    </#if>
                </article>
            </li>
            </#list>
        </ol>
        <div class="grid-row">
            <div class="column">
                <a href="#" class="link">View the latest publications</a>
            </div>
        </div>
    </div>
</#if>
