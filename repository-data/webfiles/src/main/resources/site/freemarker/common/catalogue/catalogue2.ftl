<#include "../../include/imports.ftl">


<div class="nhsd-t-grid">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-m-4">
            <@hst.include ref="filters" />
        </div>
        <div class="nhsd-t-col-s-8">
            Total results - ${totalAvailable}
            <ul>
                <#list pageable.items as document>
                    <li> ${document.title} </li>
                </#list>
            </ul>

        </div>
    </div>
</div>