<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/siteHeader.ftl">

<#include "../app-layout-head.ftl">

<body class="debugs">
    <#include "../cookie-banner.ftl"/>
    
    <#include "../scripts/live-engage-chat.ftl"/>
    
    <nav class="static-ui-menu">
        <div class="grid-wrapper grid-wrapper--wide">
            <ul class="list list--reset list--inline">
                <li>
                    <a href="/static-ui/home">Home page</a>
                </li>
                <li>
                    <a href="/static-ui/service">Service document</a>
                </li>
                <li>
                    <a href="/static-ui/standard">General document</a>
                </li>
                <li>
                    <a href="/static-ui/hub">Hub page</a>
                </li>
                <li>
                    <a href="/static-ui/list">List page</a>
                </li>
                <li>
                    <a href="/static-ui/filtered-list">Filtered list page</a>
                </li>
                <li>
                    <a href="/static-ui/icons">Icons</a>
                </li>
            </ul>
        </div>
        <hr>
    </nav>

    <main role="main">
        <@hst.include ref="main"/>
    </main>

    <#include "../site-footer.ftl"/>

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
