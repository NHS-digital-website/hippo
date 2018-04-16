<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro pageIndex index>
    <#if index?has_content>
        <div class="fixed">
            <ul>
                <li><a href="#">Back to top</a></li>
                <#list index as item>
                    <li><a href="#${item}">${item}</a></li>
                </#list>
            </ul>
        </div>
    </#if>
</#macro>
