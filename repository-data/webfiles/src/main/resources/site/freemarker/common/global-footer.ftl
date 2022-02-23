<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#assign ga=JspTaglibs ["http://www.onehippo.org/jsp/google-analytics"] >
<footer id="footer">
    <div class="nhsd-o-global-footer">
        <div class="nhsd-t-grid">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col-12 nhsd-!t-no-gutters">
                    <div class="nhsd-t-grid">
                        <div class="nhsd-t-row">
                            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-t-col-l-3 nhsd-!t-padding-0"><@hst.include ref="footer-col-1"/></div>
                            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-t-col-l-3 nhsd-!t-padding-0"><@hst.include ref="footer-col-2"/></div>
                            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-t-col-l-3 nhsd-!t-padding-0"><@hst.include ref="footer-col-3"/></div>
                            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-t-col-l-3 nhsd-!t-padding-0"><@hst.include ref="footer-col-4"/></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#if !hstRequest.requestContext.cmsRequest??>
        <@ga.accountId/>
        <@hst.link var="googleAnalytics" path="/resources/google-analytics.js"/>
        <script data-cookieconsent="statistics" src="${googleAnalytics}"></script>

        <#-- Google Tag Manager (noscript) -->
        <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-W6GJCR9" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
        <#-- End Google Tag Manager (noscript) -->
    </#if>
</footer>

