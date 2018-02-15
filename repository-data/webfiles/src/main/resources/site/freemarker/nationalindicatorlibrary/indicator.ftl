<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#--  <#include "../common/macro/structured-text.ftl">  -->
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />
<#assign formatCoverageDates="uk.nhs.digital.ps.directives.CoverageDatesFormatterDirective"?new() />

<@hst.setBundle basename="nationalindicatorlibrary.headers"/>
<head>
  <title>NHS - Clinical Indicator - Indicator Page</title>
  <meta charset="UTF-8" />
  <meta name="title" content="NHS - National Indicator Library" />
  <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0" />
  <link rel="stylesheet" href="css/style.css" />
</head>
<body>
  <section class="document-header push-double--bottom">
      <div class="document-header__inner">
        <h1 class="layout-5-6 push--bottom">${indicator.title}</h1>

        <div class="document-header__divider">&nbsp;</div>

        <h2>Independently assured by IGB</h2>

        <div class="layout">
            <div class="layout__item layout-1-2">
                <p class="push-half--bottom"><strong><@fmt.message key="headers.publishedBy"/></strong> ${indicator.publishedBy}</p>
                <p class="push-half--bottom"><strong><@fmt.message key="headers.publishedDate"/></strong> ${indicator.publishedDate.time?string[dateFormat]}</p>
                <p class="push-half--bottom"><strong><@fmt.message key="headers.reportingPeriod"/></strong> ${indicator.reportingPeriod}</p>
                <p class="push-half--bottom"><strong><@fmt.message key="headers.basedOn"/></strong> ${indicator.basedOn}</p>
            </div><!--
            --><div class="layout__item layout-1-2">
                <p class="push-half--bottom"><strong><@fmt.message key="headers.contactAuthor"/></strong> <a href="mailto:${indicator.contactAuthor}"> ${indicator.contactAuthor}</a></p>
                <p class="push-half--bottom"><strong><@fmt.message key="headers.reportingLevel"/></strong> ${indicator.reportingLevel}</p>
                <p class="push-half--bottom"><strong><@fmt.message key="headers.reviewDate"/></strong> ${indicator.reviewDate.time?string[dateFormat]}</p>
            </div>
        </div>
      </div>
    </section> 

    <section class="jumpto">
        <div class="jumpto__inner">
            <div class="jumpto__inner__nav">

                <h4>Contents</h4>
                <div class="jumpto__inner__nav__divider">&nbsp;</div>

                <ul>
                    <li><a href="#purpose"><@fmt.message key="headers.purpose"/></a></li>
                    <li><a href="#definition"><@fmt.message key="headers.definition"/></a></li>
                    <li><a href="#methodology"><@fmt.message key="headers.methodology"/></a></li>
                    <li><a href="#interpretations"><@fmt.message key="headers.interpretationGuidelines"/></a></li>
                    <li><a href="#resources">Resources</a></li>
                </ul>
            </div>
        </div>
    </section>

    <section class="document-content jumpto-offset-left">

        <section id="purpose" class="push-double--bottom">
            <h2><@fmt.message key="headers.purpose"/></h2>
            <p>${indicator.purpose}</p>
        </section>

        <section id="definition" class="push-double--bottom">
            <h2><@fmt.message key="headers.definition"/></h2>
            <p>${indicator.definition}</p>
        </section>

        <section id="methodology" class="push-double--bottom">
            <h2><@fmt.message key="headers.methodology"/></h2>

            <h3><@fmt.message key="headers.dataSource"/></h3>
            <p>${indicator.dataSource}</p>

            <h3><@fmt.message key="headers.numerator"/></h3>
            <p>${indicator.numerator}</p>

            <h3><@fmt.message key="headers.denominator"/></h3>
            <p>${indicator.denominator}</p>

            <h3><@fmt.message key="headers.calculation"/></h3>
            <p>${indicator.calculation}</p>                        

            <h3><@fmt.message key="headers.caveats"/></h3>
            <p>${indicator.caveats}</p>

        </section>

        <section id="interpretations" class="push-double--bottom">
            <h3><@fmt.message key="headers.interpretationGuidelines"/></h3>
            <li>${indicator.interpretationGuidelines}</li>
        </section>

        <section id="resources" class="push-double--bottom">
            <h3>Resources</h3>
            <ul>
                <li><a href="#">Sample file</a></li>
                <li><a href="#">Sample file</a></li>
                <li><a href="#">Sample file</a></li>
                <li><a href="#">Sample file</a></li>
            </ul>
        </section>

        <section id="compare" class="push-double--bottom">
            <h2>Compare</h2>
            <table>
                <tr>
                    <td>Title</td>
                    <td>Publisher</td>
                    <td>Published Date</td>
                    <td>Assured Until</td>
                </tr>
                <tr>
                    <td><a href="#">Cancers diagnosed via emergency routes</a></td>
                    <td>PHE</td>
                    <td>10 June 2017</td>
                    <td>10 June 2020</td>
                </tr>
                <tr>
                    <td><a href="#">PHE</a></td>
                    <td>PHE</td>
                    <td>10 June 2017</td>
                    <td>10 June 2020</td>
                </tr>
                <tr>
                    <td><a href="#">Under 75 mortality rate from cancer</a></td>
                    <td>PHE</td>
                    <td>10 June 2017</td>
                    <td>10 June 2020</td>
                </tr>
            </table>
        </section>

        <section id="resources" class="push-double--bottom">
            <h2>5 comments</h2>
            <div class="panel panel--grey">
                <p>TBC</p>
            </div>
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

</script>



</body>

