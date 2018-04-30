<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() />
<#assign interpHasContent=indicator.details.interpretationGuidelines.content?has_content>
<#assign caveatsHasContent=indicator.details.caveats.content?has_content>

<@hst.setBundle basename="nationalindicatorlibrary.headers"/>
<section data-uipath="ps.document.content">
  <section class="document-header push-double--bottom">
      <div class="document-header__inner">
        <h1 class="layout-5-6 push--bottom" data-uipath="document.title">${indicator.title}</h1>

        <#if indicator.assuredStatus><h2 data-uipath="assuredStatus">Independently assured by Information Governance Board (IGB)</h2></#if>

        <div class="layout">
            <div class="layout__item layout-1-2">
                <p class="push-half--bottom" data-uipath="publishedBy"><strong><@fmt.message key="headers.publishedBy"/></strong>: ${indicator.publishedBy}</p>
                <p class="push-half--bottom" data-uipath="assuranceDate"><strong><@fmt.message key="headers.assuranceDate"/></strong>: <@formatDate date=indicator.assuranceDate.time/></p>
                <p class="push-half--bottom" data-uipath="reportingPeriod"><strong><@fmt.message key="headers.reportingPeriod"/></strong>: ${indicator.topbar.reportingPeriod}</p>
                <p class="push-half--bottom" data-uipath="basedOn"><strong><@fmt.message key="headers.basedOn"/></strong>: ${indicator.topbar.basedOn}</p>
            </div><!--
            --><div class="layout__item layout-1-2">
                <p class="push-half--bottom" data-uipath="contactAuthor"><strong><@fmt.message key="headers.contactAuthor"/></strong>: <a href="mailto:${indicator.topbar.contactAuthor.contactAuthorEmail}"> ${indicator.topbar.contactAuthor.contactAuthorName}</a></p>
                <p class="push-half--bottom" data-uipath="reportingLevel"><strong><@fmt.message key="headers.reportingLevel"/></strong>: ${indicator.reportingLevel}</p>
                <p class="push-half--bottom" data-uipath="reviewDate"><strong><@fmt.message key="headers.reviewDate"/></strong>: <@formatDate date=indicator.topbar.reviewDate.time/></p>
                <#if indicator.geographicCoverage?has_content>
                    <div class="flex__item">
                        <div class="media">
                            <div class="media__icon media__icon--geographic-coverage"></div>
                            <dl class="media__body">
                                <dt data-uipath="geographic-coverage"><@fmt.message key="headers.geographicCoverage"/></dt>
                                <dd data-uipath="ps.indicator.geographic-coverage">
                                    <#list indicator.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                                </dd>
                            </dl>
                        </div>
                    </div>
                </#if>
            </div>
        </div>
      </div>
    </section>

    <section data-uipath="contents-menu" class="jumpto">
        <div class="jumpto__inner">
            <div class="jumpto__inner__nav">

                <p class="page-contents">Page Contents</p>
                <div class="jumpto__inner__nav__divider"></div>
                <ul>
                    <li><a href="#purpose"><@fmt.message key="headers.purpose"/></a></li>
                    <li><a href="#definition"><@fmt.message key="headers.definition"/></a></li>
                    <li><a href="#methodology"><@fmt.message key="headers.methodology"/></a></li>
                    <#if caveatsHasContent>
                        <li><a href="#caveats"><@fmt.message key="headers.caveats"/></a></li>
                    </#if>
                    <#if interpHasContent>
                        <li><a href="#interpretations"><@fmt.message key="headers.interpretationGuidelines"/></a></li>
                    </#if>
                    <#if indicator.attachments?has_content>
                        <li><a href="#resources">Resources</a></li>
                    </#if>
                </ul>
            </div>
        </div>
    </section>

    <section class="document-content jumpto-offset-left">

        <section id="purpose" data-uipath="purpose" class="push-double--bottom">
            <h2><@fmt.message key="headers.purpose"/></h2>
            <#outputformat "undefined">${indicator.details.purpose.content}</#outputformat>
        </section>

        <section id="definition" data-uipath="definition" class="push-double--bottom">
            <h2><@fmt.message key="headers.definition"/></h2>
            <#outputformat "undefined">${indicator.details.definition.content}</#outputformat>
        </section>


        <details id="methodology" data-uipath="methodology" class="push-double--bottom" title="methodology">
            <summary><span class="pointer" style="font-size:16px;text-decoration:underline;">How this indicator is calculated</span></summary></br>
            <div class="panel panel--grey">
                <#if indicator.details.methodology.dataSource.content?has_content>
                    <h3><strong><@fmt.message key="headers.dataSource"/></strong></h3>
                    <span data-uipath="data-source"><#outputformat "undefined">${indicator.details.methodology.dataSource.content}</#outputformat></span>
                </#if>

                <#if indicator.details.methodology.numerator.content?has_content>
                    <h3><strong><@fmt.message key="headers.numerator"/></strong></h3>
                    <span data-uipath="numerator"><#outputformat "undefined">${indicator.details.methodology.numerator.content}</#outputformat></span>
                </#if>

                <#if indicator.details.methodology.denominator.content?has_content>
                    <h3><strong><@fmt.message key="headers.denominator"/></strong></h3>
                    <span data-uipath="denominator"><#outputformat "undefined">${indicator.details.methodology.denominator.content}</#outputformat></span>
                </#if>

                <#if indicator.details.methodology.calculation.content?has_content>
                    <h3><strong><@fmt.message key="headers.calculation"/></strong></h3>
                    <span data-uipath="calculation"><#outputformat "undefined">${indicator.details.methodology.calculation.content}</#outputformat></span>
                </#if>
            </div>
        </details>

        <#if caveatsHasContent>
            <section id="caveats" data-uipath="caveats" class="push-double--bottom">
                <h2><strong><@fmt.message key="headers.caveats"/></strong></h2>
                <#outputformat "undefined">${indicator.details.caveats.content}</#outputformat>
            </section>
        </#if>

        <#if interpHasContent>
            <section id="interpretations" data-uipath="interpretations" class="push-double--bottom">
                <h2><strong><@fmt.message key="headers.interpretationGuidelines"/></strong></h2>
                <#outputformat "undefined">${indicator.details.interpretationGuidelines.content}</#outputformat>
            </section>
        </#if>

        <section data-uipath="taxonomy" class="push-double--bottom">
            <h2><strong><@fmt.message key="headers.categories"/></strong></h2>

            <#list indicator.taxonomyList?keys as key>
                <p class="filter-list__item__link" data-uipath="ps.document.taxonomy-${key}"><a title="Search for ${indicator.taxonomyList[key]}" href="${searchLink}/category/${key}/">${indicator.taxonomyList[key]}</a></p>
            </#list>
        </section>


        <#if indicator.hasAttachments()>
            <section data-uipath="resources" class="push-double--bottom">
                <h2><strong><@fmt.message key="headers.resources"/></strong></h2>
                        <ul data-uipath="nil.indicator.resources">
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
                                    <span class="fileSize">(<@formatFileSize bytesCount=attachment.resource.length/>)</span>
                                </li>
                            </#list>
                        </ul>
            </section>
        </#if>
    </section>
</section>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/waypoints/1.1.6/waypoints.min.js"></script>

<script>

    $(function() {
        var $container = $('.jumpto');
        var $b = $('body');
        $.waypoints.settings.scrollThrottle = 0;
        $container.waypoint({
            handler: function(e, d) {
                $b.toggleClass('sticky', d === 'down');
                e.preventDefault();
            }
        });
    });

    function isMathMLNativelySupported(){
        var hasMathML = false;
        if (document.createElement) {
            var div = document.createElement("div");
            div.style.position = "absolute"; div.style.top = div.style.left = 0;
            div.style.visibility = "hidden"; div.style.width = div.style.height = "auto";
            div.style.fontFamily = "serif"; div.style.lineheight = "normal";
            div.innerHTML = "<math><mfrac><mi>xx</mi><mi>yy</mi></mfrac></math>";
            $("body").append(div);
            hasMathML = div.offsetHeight > div.offsetWidth;
        }
        return hasMathML;
    }

    function setupMathjaxIfNeeded(){
        if( isMathMLNativelySupported() )
        return true;
        var scriptMathJax = document.createElement("script");
        scriptMathJax.type = "text/javascript";
        scriptMathJax.src  = "http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML";
        $("head").append(scriptMathJax);
        return false;
    }

    $( document ).ready( function(){setupMathjaxIfNeeded();} );

</script>
