<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "macro/hubBox.ftl">
<#include "macro/stickyGroupBlockHeader.ftl">
<#include "macro/documentHeader.ftl">

<@hst.setBundle basename="rb.doctype.news-hub"/>

<#assign overridePageTitle><@fmt.message key="headers.page-title" /></#assign>
<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign monthNames = monthsOfTheYear()?reverse />

<#--Group the news articles by earliest start month  -->
<#assign newsGroupHash = {} />
<#list pageable.items as item>
    <@fmt.formatDate value=item.publisheddatetime.time type="Date" pattern="MMMM" timeZone="${getTimeZone()}" var="key" />
    <#assign newsGroupHash = newsGroupHash + {  key : (newsGroupHash[key]![]) + [ item ] } />
</#list>

<article class="article article--news-hub">

    <#assign header_title><@fmt.message key="headers.page-title" /></#assign>
    <#assign header_summary><@fmt.message key="texts.intro" /></#assign>
    <#assign header_icon = 'images/icon-article.png' />
    <#assign document = "simulating_doc" />

    <@documentHeader document 'news' header_icon header_title header_summary></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">

          <div class="column column--one-quarter page-block page-block--main">
            <div class="column column--two-thirds page-block page-block--main">
              <div class="news-latest-news-title">Search news articles</div>
              <div class="search-strip__margin">


                <#-- import Mark.js -->
                <script>

                  function delay(callback, ms) {
                    var timer = 0;
                    return function() {
                      var context = this, args = arguments;
                      clearTimeout(timer);
                      timer = setTimeout(function () {
                        callback.apply(context, args);
                      }, ms || 0);
                    };
                  }

                  function replaceAll(str, find, replace) {
                      return str.replace(new RegExp(find, 'gi'), replace);
                  }

                  function replaceFoundString(inputText, newString) {
                      var queryString = document.getElementById("query_news").value;
                      var innerHTML = inputText.innerHTML;
                      innerHTML = replaceAll(innerHTML, queryString, "<span class='highlight'>"+queryString+"</span>");
                      inputText.innerHTML = innerHTML;
                  }

                  function highlightSearchTerm() {
                    return function() {
                        var shortSummaries = document.getElementsByClassName("hub-box__text");
                        Array.prototype.forEach.call(shortSummaries, replaceFoundString);
                        var titles = document.getElementsByClassName("hub-box__title-a");
                        Array.prototype.forEach.call(titles, replaceFoundString);
                    }
                  }
                  function onType() {
                    return function() {
                      delay(function (e) {
                      var queryString = document.getElementById("query_news").value;
                      $.get("?query="+queryString, function (data) {
                        var response_html = $.parseHTML(data);
                        $("#search-results-list").html(
                          $(data).find("#search-results-list").length > 0 
                            ? $(data).find("#search-results-list")
                            : "<div>No results to display</div>"
                        );
                        highlightSearchTerm()();
                      } );
                      }, 100)(); //delay in ms
                    }
                  }

                  $("#query_news").keyup(onType());

                </script>

                <#assign searchLink = "" />
                <#assign searchId = "query_news" />
                <#assign buttonLabel = "Filter" />
                <#include "search-strip.ftl">
              </div>
            </div>
          </div>

        <#if pageable?? && pageable.items?has_content>
                <div class="column column--three-quarters page-block page-block--main">

                    <div class="article-section article-section--letter-group">

                        <div class="grid-row">
                            <div class="column column--reset">
                                <div class="hub-box-list" id="search-results-list">
                                    <#list pageable.items as item>
                                        <#assign newsData = { "title": item.title, "text": item.shortsummary} />

                                        <@hst.link hippobean=item var="newsLink" />
                                        <@fmt.formatDate value=item.publisheddatetime.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" var="date" />

                                        <#assign newsData += { "link": newsLink, "date": date } />

                                        <@hubBox newsData></@hubBox>
                                    </#list>
                                </div>
                            </div>
                        </div>
                    </div>


                    <#if pageable.totalPages gt 1>
                        <div class="article-section no-border">
                            <#include "../include/pagination.ftl">
                        </div>
                    </#if>
                </div>

                <div class="column column--one-third">
                    <div class="article-section">
                        <div class="grid-row">
                            <div class="column column--reset">
                                <@hst.include ref="component-rightpane"/>
                            </div>
                        </div>
                    </div>
                </div>
        <#else>
                <div class="column column--two-thirds page-block page-block--main">
                    <p><@fmt.message key="texts.no-news" /></p>
                </div>
        </#if>
      </div>

    </div>

</article>
