<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro pageIndex index>

<#if index?has_content>
    <div class="article-section-nav">
        <h2 class="article-section-nav__title">Page contents</h2>
        <hr>
        <nav role="navigation">
            <ol class="article-section-nav__list">
                <li><a href="#">Back to top</a></li>
                <#list index as item>
                    <li><a href="#${item}">${item}</a></li>
                </#list>
            </ol>
        </nav>
    </div>
</#if>

</#macro>
