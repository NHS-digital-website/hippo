<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/articleSections.ftl">

<article>
  <div class="article-wrapper">
    <div class="local-header">
      <h1 class="local-header__title">${document.title}</h1>
    </div>

    <div class="grid-row">
      <div class="column column--one-third article-block article-block--sidebar"  role="complementary">
        <div class="article-section-nav">
          <h2 class="article-section-nav__title">Page contents</h2>
          <hr>
          <nav role="navigation">
            <ol class="article-section-nav__list">
              <li><a href="#section-summary">Summary</a></li>
              <#if document.sections?has_content>
              <#list document.sections as section>
                <li><a href="#section-${section?index+1}">${section.title}</a></li>
              </#list>
              </#if>

              <#if childPages?has_content>
                <li><a href="#section-child-pages">Further information</a></li>
              </#if>
            </ol>
          </nav>
        </div>
      </div>

      <div class="column column--two-thirds article-block article-block--main" role="main">
        <#-- BEGIN mandatory 'summary section' -->
        <div class="grid-row">
          <div class="column">
            <section id="section-summary" class="article-section article-section--summary">
              <h2>Summary</h2>
              <p>${document.summary}</p>
            </section>
          </div>
        </div>
        <#-- END mandatory 'summary section' -->

        <#-- BEGIN optional list of 'top tasks section' -->
        <#if document.toptasks?has_content>
        <div class="grid-row">
          <div class="column">
            <section class="article-section article-section--top-tasks">
              <div class="callout callout--attention">
                <h2>Top Tasks</h2>

                <#list document.toptasks as toptask>
                  <@hst.html hippohtml=toptask/>
                </#list>
              </div>
            </section>
          </div>
        </div>
        </#if>
        <#-- END optional list of 'top tasks section' -->

        <#-- BEGIN optional 'Introduction section' -->
        <#if document.introduction??>
        <div class="grid-row">
          <div class="column">
            <div class="article-section article-section--introduction">
              <@hst.html hippohtml=document.introduction/>
            </div>
          </div>
        </div>
        </#if>
        <#-- END optional 'Introduction section' -->

        <@articleSections document.sections></@articleSections>

        <#-- BEGIN optional 'Further information section' -->
        <#if childPages?has_content>
        <div class="grid-row">
          <div class="column">
            <section class="article-section article-section--child-pages" id="section-child-pages">
              <h2>Further information</h2>
              <ol class="list list-reset">
                <#list childPages as childPage>
                <@hst.link var="link" hippobean=childPage />
                  <li>
                    <article>
                      <h2><a href="${link}">${childPage.title}</a></h2>
                      <p>${childPage.shortsummary}</p>
                    </article>
                  </li>
                </#list>
              </ol>
            </section>
          </div>
        </div>
        </#if>
        <#-- END optional 'Further information section' -->
      </div>
    </div>
  </div>
</article>
