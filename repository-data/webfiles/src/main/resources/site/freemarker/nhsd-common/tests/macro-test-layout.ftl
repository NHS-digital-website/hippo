<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<!DOCTYPE html>
<html lang="en" class="nhsd-no-js">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <@hst.setBundle basename="design-system"/>
        <@fmt.message key="design-system.url" var="designSystemUrl" />
        <#assign toolkitVersion = 'v0.153.0' />
        <link rel="stylesheet" href="${designSystemUrl}/cdn/${toolkitVersion}/stylesheets/nhsd-frontend.css" media="screen" type="text/css"/>
    </head>
    <body>
        <@hst.include ref= "main"/>
    </body>
</html>

