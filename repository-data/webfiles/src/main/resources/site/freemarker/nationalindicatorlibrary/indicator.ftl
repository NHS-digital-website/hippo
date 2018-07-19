<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/sectionNav.ftl">
<#include "../common/macro/fileMetaAppendix.ftl">

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<#assign document = { "title": indicator.title } />
<@metaTags></@metaTags>

<#assign caveatsHasContent = indicator.details.caveats.content?has_content />
<#assign interpHasContent = indicator.details.interpretationGuidelines.content?has_content />
<#assign hasAttachments = indicator.hasAttachments() />
<#assign hasDataSourceContent = indicator.details.methodology.dataSource.content?has_content />
<#assign hasNumeratorContent = indicator.details.methodology.numerator.content?has_content />
<#assign hasDenominatorContent = indicator.details.methodology.denominator.content?has_content />
<#assign hasCalculationContent = indicator.details.methodology.calculation.content?has_content />
<#assign showCalculationDetails = hasDataSourceContent || hasNumeratorContent || hasDenominatorContent || hasCalculationContent />

<#-- Define Article section headers, nav ids and titles -->
<@hst.setBundle basename="nationalindicatorlibrary.headers,nationalindicatorlibrary.labels"/>
<@fmt.message key="headers.purpose" var="purposeHeader" />
<@fmt.message key="headers.definition" var="definitionHeader" />
<#assign methodologyHeader = "Methodology" />
<@fmt.message key="headers.caveats" var="caveatsHeader" />
<@fmt.message key="headers.interpretationGuidelines" var="interpretationGuidelinesHeader" />
<@fmt.message key="headers.categories" var="categoriesHeader" />
<@fmt.message key="headers.resources" var="resourcesHeader" />

<#function getSectionNavLinks>
    <#assign links = [{ "url": "#" + slugify(purposeHeader), "title": purposeHeader }] />
    <#assign links += [{ "url": "#" + slugify(definitionHeader), "title": definitionHeader }] />
    <#assign links += [{ "url": "#" + slugify(methodologyHeader), "title": methodologyHeader }] />

    <#if caveatsHasContent>
        <#assign links += [{ "url": "#" + slugify(caveatsHeader), "title": caveatsHeader }] />
    </#if>

    <#if interpHasContent>
        <#assign links += [{ "url": "#" + slugify(interpretationGuidelinesHeader), "title": interpretationGuidelinesHeader }] />
    </#if>

    <#if hasAttachments>
        <#assign links += [{ "url": "#" + slugify(resourcesHeader), "title": resourcesHeader }] />
    </#if>

    <#return links />
</#function>

<article class="article article--indicator">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" data-uipath="ps.document.content" aria-label="Document Header">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <h1 class="local-header__title" data-uipath="document.title">${indicator.title}</h1>

                    <#if indicator.assuredStatus>
                    <h2 class="article-header__subtitle" data-uipath="assuredStatus"><@fmt.message key="labels.assured" /></h2>
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
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <@sectionNav getSectionNavLinks()></@sectionNav>
                </div>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <div id="${slugify(purposeHeader)}" data-uipath="purpose" class="article-section">
                    <h2>${purposeHeader}</h2>
                    <div class="rich-text-content">
                        <#outputformat "undefined">${indicator.details.purpose.content}</#outputformat>
                    </div>
                </div>

                <div id="${slugify(definitionHeader)}" data-uipath="definition" class="article-section">
                    <h2>${definitionHeader}</h2>
                    <div class="rich-text-content">
                        <#outputformat "undefined">${indicator.details.definition.content}</#outputformat>
                    </div>
                </div>

                <#if showCalculationDetails>
                <div class="article-section" id="${slugify(methodologyHeader)}">
                    <details data-uipath="methodology" title="${methodologyHeader}">
                        <summary><span class="pointer" style="font-size:16px; text-decoration:underline;">How this indicator is calculated</span></summary><br>
                        <div class="panel panel--grey">
                            <div class="panel__content">
                                <#if hasDataSourceContent>
                                <h3><@fmt.message key="headers.dataSource"/></h3>
                                <div data-uipath="data-source"><#outputformat "undefined">${indicator.details.methodology.dataSource.content}</#outputformat></div>
                                </#if>

                                <#if hasNumeratorContent>
                                <h3><@fmt.message key="headers.numerator"/></h3>
                                <div data-uipath="numerator"><#outputformat "undefined">${indicator.details.methodology.numerator.content}</#outputformat></div>
                                </#if>

                                <#if hasDenominatorContent>
                                <h3><@fmt.message key="headers.denominator"/></h3>
                                <div data-uipath="denominator"><#outputformat "undefined">${indicator.details.methodology.denominator.content}</#outputformat></div>
                                </#if>

                                <#if hasCalculationContent>
                                <h3><@fmt.message key="headers.calculation"/></h3>
                                <div data-uipath="calculation"><#outputformat "undefined">${indicator.details.methodology.calculation.content}</#outputformat></div>
                                </#if>
                            </div>
                        </div>
                    </details>
                </div>
                </#if>

                <#if caveatsHasContent>
                <div id="${slugify(caveatsHeader)}" data-uipath="caveats" class="article-section">
                    <h2>${caveatsHeader}</h2>
                    <#outputformat "undefined">${indicator.details.caveats.content}</#outputformat>
                </div>
                </#if>

                <#if interpHasContent>
                <div id="${slugify(interpretationGuidelinesHeader)}" data-uipath="interpretations" class="article-section">
                    <h2>${interpretationGuidelinesHeader}</h2>
                    <#outputformat "undefined">${indicator.details.interpretationGuidelines.content}</#outputformat>
                </div>
                </#if>

                <div data-uipath="taxonomy" class="article-section" id="${slugify(categoriesHeader)}">
                    <h2>${categoriesHeader}</h2>
                    <ul class="list list--inline list--reset list--pills">
                    <#list indicator.taxonomyList?keys as key>
                        <li>
                            <span class="pill" data-uipath="ps.document.taxonomy-${key}"><a title="Search for ${indicator.taxonomyList[key]}" href="${searchLink}/category/${key}/">${indicator.taxonomyList[key]}</a></span>
                        </li>
                    </#list>
                    </ul>
                </div>

                <#if hasAttachments>
                <div data-uipath="section-resources" class="article-section" id="${slugify(resourcesHeader)}">
                    <h2>${resourcesHeader}</h2>
                    <ul data-uipath="nil.indicator.resources" class="list list--reset">
                        <#if indicator.details.qualityStatementUrl?has_content>
                        <li class="attachment">
                            <a href="${indicator.details.qualityStatementUrl}" target="_blank" onClick="logGoogleAnalyticsEvent('View quality statement','Indicator','${indicator.details.iapCode}');" onKeyUp="return vjsu.onKeyUp(event)"><@fmt.message key="headers.qualityStatement"/></a>
                        </li>
                        </#if>

                        <#if indicator.details.technicalSpecificationUrl?has_content>
                        <li class="attachment">
                            <a href="${indicator.details.technicalSpecificationUrl}" target="_blank" onClick="logGoogleAnalyticsEvent('View technical specification','Indicator','${indicator.details.iapCode}');" onKeyUp="return vjsu.onKeyUp(event)"><@fmt.message key="headers.technicalSpecification"/></a>
                        </li>
                        </#if>

                        <#list indicator.attachments as attachment>
                        <li class="attachment">
                            <@externalstorageLink attachment.resource; url>
                            <a title="${attachment.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','Indicator','${attachment.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)">${attachment.text}</a>
                            </@externalstorageLink>
                            <@fileMetaAppendix attachment.resource.length, attachment.resource.mimeType></@fileMetaAppendix>
                        </li>
                        </#list>
                    </ul>
                </div>
                </#if>
            </div>
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
