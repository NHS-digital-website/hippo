<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "../common/macro/component/actionLink.ftl">
<#include "../common/macro/svgIcons.ftl">

<div class="ndrs-home-header"></div>

<h3>HOME</h3>
<div class="nhsd-t-grid">
    <div class="nhsd-t-row ">
        <!-- first row outer grid -->
        <div class="nhsd-t-col-12 nhsd-!t-no-gutters nhsd-!t-padding-1">
            <@hst.include ref="main" /> <!-- empty for urgent use -->
        </div>
    </div>
    <div class="nhsd-t-row ">
        <!-- 2nd row outer grid -->
        <div class="nhsd-t-col-xl-8 nhsd-t-col-l-8 nhsd-t-col-m-8 nhsd-t-col-s-12 nhsd-t-col-xs-12 nhsd-!t-no-gutters">

            <div class="nhsd-t-grid nhsd-!t-no-gutters">
                <!-- inner table -->
                <div class="nhsd-t-row">
                    <!-- one row for each container -->
                    <div class="nhsd-t-col-12 nhsd-!t-no-gutters nhsd-!t-padding-1">
                        <@hst.include ref="homepagecontainer2" />
                    </div>
                    <div class="nhsd-t-col-12 nhsd-!t-no-gutters nhsd-!t-padding-1">
                        <@hst.include ref="homepagecontainer3" />
                    </div>
                </div>
            </div> <!-- end inner table -->

        </div> <!-- end first col -->

        <div class="nhsd-t-col-xl-4 nhsd-t-col-l-4 nhsd-t-col-m-4 nhsd-t-col-s-12 nhsd-t-col-xs-12 nhsd-!t-no-gutters nhsd-!t-padding-1">
            <@hst.include ref="homepagecontainer4" />
        </div>
    </div>
    <div class="nhsd-t-row ">
        <!-- 3rd row outer grid -->
        <div class="nhsd-t-col-12 nhsd-!t-no-gutters nhsd-!t-padding-1">
            <@hst.include ref="homepagecontainer5" />
        </div>
    </div>

</div> <!-- end outer grid -->


