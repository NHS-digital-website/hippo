<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro pageIndex index>
    <#if index?has_content>
        <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
            <div class="article-section-nav">
                <h2 class="article-section-nav__title">Page contents</h2>
                <hr>
                <nav role="navigation">
                    <ol class="article-section-nav__list">
                        <#list index as item>
                            <li><a href="#${item}" title="${item}">${item}</a></li>
                        </#list>
                    </ol>
                </nav>
            </div>
        </div>
    </#if>
</#macro>
