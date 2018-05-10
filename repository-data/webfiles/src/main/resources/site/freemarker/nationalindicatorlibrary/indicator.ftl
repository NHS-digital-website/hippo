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

                    <div class="detail-list-grid">
                        <div class="grid-row">
                            <div class="column column--one-half column--reset">
                                <dl class="detail-list" data-uipath="publishedBy">
                                    <dt class="detail-list__key"><@fmt.message key="headers.publishedBy" /></dt>
                                    <dd class="detail-list__value">${indicator.publishedBy}</dd>
                                </dl>
                            </div>

                            <div class="column column--one-half column--reset">                                
                                <dl class="detail-list" data-uipath="contactAuthor">
                                    <dt class="detail-list__key"><@fmt.message key="headers.contactAuthor" /></dt>
                                    <dd class="detail-list__value">
                                        <#if indicator.topbar.contactAuthor.contactAuthorEmail?has_content>
                                            <a href="mailto:${indicator.topbar.contactAuthor.contactAuthorEmail}">${indicator.topbar.contactAuthor.contactAuthorName}</a>
                                        <#else>
                                            ${indicator.topbar.contactAuthor.contactAuthorName}
                                        </#if>
                                    </dd>
                                </dl>
                            </div>
                        </div>

                        <div class="grid-row">
                            <div class="column column--one-half column--reset">
                                <dl class="detail-list" data-uipath="assuranceDate">
                                    <dt class="detail-list__key"><@fmt.message key="headers.assuranceDate"/></dt>
                                    <dd class="detail-list__value"><@formatDate date=indicator.assuranceDate.time/></dd>
                                </dl>
                            </div>

                            <div class="column column--one-half column--reset">
                                <dl class="detail-list" data-uipath="reportingLevel">
                                    <dt class="detail-list__key"><@fmt.message key="headers.reportingLevel"/></dt>
                                    <dd class="detail-list__value">${indicator.reportingLevel}</dd>
                                </dl>
                            </div>
                        </div>

                        <div class="grid-row">
                            <div class="column column--one-half column--reset">
                                <dl class="detail-list" data-uipath="reportingPeriod">
                                    <dt class="detail-list__key"><@fmt.message key="headers.reportingPeriod"/></dt>
                                    <dd class="detail-list__value">${indicator.topbar.reportingPeriod}</dd>
                                </dl>
                            </div>

                            <div class="column column--one-half column--reset">
                                <dl class="detail-list" data-uipath="reviewDate">
                                    <dt class="detail-list__key"><@fmt.message key="headers.reviewDate"/></dt>
                                    <dd class="detail-list__value"><@formatDate date=indicator.topbar.reviewDate.time/></dd>
                                </dl>
                            </div>
                        </div>

                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list" data-uipath="basedOn">
                                    <dt class="detail-list__key"><@fmt.message key="headers.basedOn"/></dt>
                                    <dd class="detail-list__value">${indicator.topbar.basedOn}</dd>
                                </dl>
                            </div>
                        </div>

                        <#if indicator.geographicCoverage?has_content>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key" id="geographic-coverage" data-uipath="geographic-coverage"><@fmt.message key="headers.geographicCoverage"/></dt>
                                    <dd class="detail-list__value" data-uipath="ps.indicator.geographic-coverage">
                                        <#list indicator.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>

<div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
    <div class="grid-row">
        <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
            <div class="article-section-nav">
                <h2 class="article-section-nav__title">Page contents</h2>
                <hr>
                <nav role="navigation">
                    <ol class="article-section-nav__list">
                        <li><a href="#section-purpose"><@fmt.message key="headers.purpose"/></a></li>
                        <li><a href="#section-definition"><@fmt.message key="headers.definition"/></a></li>
                        <li><a href="#section-methodology"><@fmt.message key="headers.methodology"/></a></li>
                        
                        <#if caveatsHasContent>
                        <li><a href="#section-caveats"><@fmt.message key="headers.caveats"/></a></li>
                        </#if>
                        <#if interpHasContent>
                        <li><a href="#section-interpretations"><@fmt.message key="headers.interpretationGuidelines"/></a></li>
                        </#if>
                        <#if indicator.attachments?has_content>
                        <li><a href="#section-resources">Resources</a></li>
                        </#if>
                    </ol>
                </nav>
            </div>
        </div>

        <div class="column column--two-thirds page-block page-block--main">
            <div id="section-purpose" data-uipath="purpose" class="article-section">
                <h2><@fmt.message key="headers.purpose"/></h2>
                <div class="rich-text-content">
                    <#outputformat "undefined">${indicator.details.purpose.content}</#outputformat>
                </div>
            </div>

            <div id="section-definition" data-uipath="definition" class="article-section">
                <h2><@fmt.message key="headers.definition"/></h2>
                <div class="rich-text-content">
                    <#outputformat "undefined">${indicator.details.definition.content}</#outputformat>
                </div>
            </div>

            <#assign hasDataSourceContent = indicator.details.methodology.dataSource.content?has_content />
            <#assign hasNumeratorContent = indicator.details.methodology.numerator.content?has_content />
            <#assign hasDenominatorContent = indicator.details.methodology.denominator.content?has_content />
            <#assign hasCalculationContent = indicator.details.methodology.calculation.content?has_content />
            <#assign showCalculationDetails = hasDataSourceContent || hasNumeratorContent || hasDenominatorContent || hasCalculationContent />

            <#if showCalculationDetails>
            <div class="article-section" id="section-methodology">
                <details data-uipath="methodology" title="methodology">
                    <summary><span class="pointer" style="font-size:16px; text-decoration:underline;">How this indicator is calculated</span></summary></br>
                    <div class="panel panel--grey">
                        <div class="panel__content">
                            <#if hasDataSourceContent>
                            <h3><@fmt.message key="headers.dataSource"/></h3>
                            <span data-uipath="data-source"><#outputformat "undefined">${indicator.details.methodology.dataSource.content}</#outputformat></span>
                            </#if>

                            <#if hasNumeratorContent>
                            <h3><@fmt.message key="headers.numerator"/></h3>
                            <span data-uipath="numerator"><#outputformat "undefined">${indicator.details.methodology.numerator.content}</#outputformat></span>
                            </#if>

                            <#if hasDenominatorContent>
                            <h3><@fmt.message key="headers.denominator"/></h3>
                            <span data-uipath="denominator"><#outputformat "undefined">${indicator.details.methodology.denominator.content}</#outputformat></span>
                            </#if>

                            <#if hasCalculationContent>
                            <h3><@fmt.message key="headers.calculation"/></h3>
                            <span data-uipath="calculation"><#outputformat "undefined">${indicator.details.methodology.calculation.content}</#outputformat></span>
                            </#if>
                        </div>
                    </div>
                </details>
            </div>
            </#if>

            <#if caveatsHasContent>
            <div id="section-caveats" data-uipath="caveats" class="article-section">
                <h2><@fmt.message key="headers.caveats"/></h2>
                <#outputformat "undefined">${indicator.details.caveats.content}</#outputformat>
            </div>
            </#if>

            <#if interpHasContent>
            <div id="section-interpretations" data-uipath="interpretations" class="article-section">
                <h2><@fmt.message key="headers.interpretationGuidelines"/></h2>
                <#outputformat "undefined">${indicator.details.interpretationGuidelines.content}</#outputformat>
            </div>
            </#if>

            <div data-uipath="taxonomy" class="article-section">
                <h2><@fmt.message key="headers.categories"/></h2>
                <ul class="list list--inline list--reset list--pills">
                <#list indicator.taxonomyList?keys as key>
                    <li>
                        <span class="pill" data-uipath="ps.document.taxonomy-${key}"><a title="Search for ${indicator.taxonomyList[key]}" href="${searchLink}/category/${key}/">${indicator.taxonomyList[key]}</a></span>
                    </li>
                </#list>    
                </ul>
            </div>

            <#if indicator.hasAttachments()>
            <div data-uipath="section-resources" class="article-section">
                <h2><@fmt.message key="headers.resources"/></h2>
                <ul data-uipath="nil.indicator.resources" class="list list--reset">
                    <#if indicator.details.qualityStatementUrl?has_content>
                    <li class="attachment">
                        <a href="${indicator.details.qualityStatementUrl}" target="_blank" onClick="logGoogleAnalyticsEvent('View quality statement','Indicator','${indicator.details.iapCode}');"><@fmt.message key="headers.qualityStatement"/></a>
                    </li>
                    </#if>

                    <#if indicator.details.technicalSpecificationUrl?has_content>
                    <li class="attachment">
                        <a href="${indicator.details.technicalSpecificationUrl}" target="_blank" onClick="logGoogleAnalyticsEvent('View technical specification','Indicator','${indicator.details.iapCode}');"><@fmt.message key="headers.technicalSpecification"/></a>
                    </li>
                    </#if>

                    <#list indicator.attachments as attachment>
                    <li class="attachment">
                        <@externalstorageLink attachment.resource; url>
                        <a title="${attachment.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','Indicator','${attachment.resource.filename}');">${attachment.text}</a>
                        </@externalstorageLink>
                        <span class="fileSize">[<@formatFileSize bytesCount=attachment.resource.length/>]</span>
                    </li>
                    </#list>
                </ul>
            </div>
            </#if>
        </div>
    </div>
</article>

<script>
    (function(){
        var oldBrowser = window.addEventListener ? false : true;
        var vjsUtils = window.vanillaJSUtils;

        function isMathMLNativelySupported(){
            var hasMathML = false;
            if (document.createElement) {
                var div = document.createElement("div");
                div.style.position = "absolute";
                div.style.top = div.style.left = 0;
                div.style.visibility = "hidden"; div.style.width = div.style.height = "auto";
                div.style.fontFamily = "serif"; div.style.lineheight = "normal";
                div.innerHTML = "<math><mfrac><mi>xx</mi><mi>yy</mi></mfrac></math>";
                document.body.appendChild(div);
                hasMathML = div.offsetHeight > div.offsetWidth;
            }
            return hasMathML;
        }

        function setupMathjaxIfNeeded(){
            if(isMathMLNativelySupported()) {
                return true;
            }
            
            var scriptMathJax = document.createElement("script");
            scriptMathJax.type = "text/javascript";
            scriptMathJax.src  = "https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.1/MathJax.js?config=TeX-AMS-MML_HTMLorMML";
            document.getElementsByTagName("head")[0].appendChild(scriptMathJax);
            return false;
        }

        function init () {
            if (!oldBrowser) {
                document.addEventListener('DOMContentLoaded', setupMathjaxIfNeeded);
            } else {
                document.attachEvent("onreadystatechange", function() {
                    if (document.readyState === "complete") {
                        document.detachEvent("onreadystatechange", arguments.callee);
                        setupMathjaxIfNeeded();
                    }
                });
            }
        }

        init();
    }());
</script>
