<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<@hst.include ref="banner"/>

<div class="nhsd-t-grid nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-7 nhsd-api-catalogue">

    <div class="nhsd-t-row">
        <div class="nhsd-t-col-12 nhsd-!t-margin-bottom-5">
            <@hst.include ref='alphabet'/>
        </div>
    </div>

    <div class="nhsd-t-row">
        <div class="nhsd-t-col-l-3 nhsd-t-col-m-12">
            <@hst.include ref='facets'/>
        </div>
        <div class="nhsd-t-col-l-9 nhsd-t-col-m-12">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col-l-8 nhsd-!t-padding-left-0 nhsd-!t-padding-bottom-1 nhsd-!t-padding-top-l-4">
                    <@hst.include ref='search'/>
                </div>

                <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xs" />

                <div class="nhsd-t-row nhsd-!t-padding-top-1">
                    <div class="nhsd-t-col-l-12 nhsd-!t-padding-left-0">
                        <@hst.include ref='results'/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>