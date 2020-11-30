<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/globalHeader.ftl">

<!DOCTYPE html>
<html lang="en">
    <#include "./nhsd-homepage-head.ftl">
<body>
    <div class="nhsd-t-grid nhsd-t-grid--full-width">
        <@globalHeader false/>

        <@hst.include ref= "main"/>

        <@hst.include ref= "footer"/>
    </div>
</body>
</html>

