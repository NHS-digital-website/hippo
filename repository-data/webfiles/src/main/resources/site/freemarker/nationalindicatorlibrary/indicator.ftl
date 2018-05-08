<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() />
<#assign interpHasContent=indicator.details.interpretationGuidelines.content?has_content>
<#assign caveatsHasContent=indicator.details.caveats.content?has_content>

<@hst.setBundle basename="nationalindicatorlibrary.headers"/>
<article class="article article--indicator">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" data-uipath="ps.document.content" aria-label="Document Header">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <h1 class="local-header__title" data-uipath="document.title">${indicator.title}</h1>

                    <#if indicator.assuredStatus>
                    <h2 class="article-header__subtitle" data-uipath="assuredStatus">Independently assured by Information Governance Board (IGB)</h2>
                    </#if>

                    <hr class="hr hr--short hr--light">

                    <div class="article-header__detail-lines article-header__detail-lines--mega">
                        <div class="grid-row">
                            <div class="column column--one-half column--reset">
                                <div class="article-header__detail-line">
                                    <dl class="article-header__detail-list" data-uipath="publishedBy">
                                        <dt class="article-header__detail-list-key"><@fmt.message key="headers.publishedBy" /></dt>
                                        <dd class="article-header__detail-list-value">${indicator.publishedBy}</dd>
                                    </dl>
                                </div>

                                <div class="article-header__detail-line">
                                    <dl class="article-header__detail-list" data-uipath="assuranceDate">
                                        <dt class="article-header__detail-list-key"><@fmt.message key="headers.assuranceDate"/></dt>
                                        <dd class="article-header__detail-list-value"><@formatDate date=indicator.assuranceDate.time/></dd>
                                    </dl>
                                </div>

                                <div class="article-header__detail-line">
                                    <dl class="article-header__detail-list" data-uipath="reportingPeriod">
                                        <dt class="article-header__detail-list-key"><@fmt.message key="headers.reportingPeriod"/></dt>
                                        <dd class="article-header__detail-list-value">${indicator.topbar.reportingPeriod}</dd>
                                    </dl>
                                </div>
                            </div>

                            <div class="column column--one-half column--reset">
                                <div class="article-header__detail-line">
                                    <dl class="article-header__detail-list" data-uipath="contactAuthor">
                                        <dt class="article-header__detail-list-key"><@fmt.message key="headers.contactAuthor" /></dt>
                                        <dd class="article-header__detail-list-value"><a href="mailto:${indicator.topbar.contactAuthor.contactAuthorEmail}"> ${indicator.topbar.contactAuthor.contactAuthorName}</a></dd>
                                    </dl>
                                </div>
                                <div class="article-header__detail-line">
                                    <dl class="article-header__detail-list" data-uipath="reportingLevel">
                                        <dt class="article-header__detail-list-key"><@fmt.message key="headers.reportingLevel"/></dt>
                                        <dd class="article-header__detail-list-value">${indicator.reportingLevel}</dd>
                                    </dl>
                                </div>
                                <div class="article-header__detail-line">
                                    <dl class="article-header__detail-list" data-uipath="reviewDate">
                                        <dt class="article-header__detail-list-key"><@fmt.message key="headers.reviewDate"/></dt>
                                        <dd class="article-header__detail-list-value"><@formatDate date=indicator.topbar.reviewDate.time/></dd>
                                    </dl>
                                </div>
                            </div>
                        </div>

                        <div class="grid-row">
                            <div class="column column--reset">
                                <div class="article-header__detail-line">
                                    <dl class="article-header__detail-list" data-uipath="basedOn">
                                        <dt class="article-header__detail-list-key"><@fmt.message key="headers.basedOn"/></dt>
                                        <dd class="article-header__detail-list-value">${indicator.topbar.basedOn}</dd>
                                    </dl>
                                </div>
                            </div>
                        </div>

                        <#if legacyPublication.geographicCoverage?has_content>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <div class="article-header__detail-line">
                                    <dl class="article-header__detail-list">
                                        <dt class="article-header__detail-list-key" id="geographic-coverage" data-uipath="geographic-coverage"><@fmt.message key="headers.geographicCoverage"/></dt>
                                        <dd class="article-header__detail-list-value" data-uipath="ps.indicator.geographic-coverage">
                                            <#list indicator.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                                        </dd>
                                    </dl>
                                </div>
                            </div>
                        </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    CONTENT
</article>
