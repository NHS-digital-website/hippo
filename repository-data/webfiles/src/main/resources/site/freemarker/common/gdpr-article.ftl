<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/articleSections.ftl">
<#include "macro/linkGrid.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="website.gdpr"/>

<article class="article article--gdpr">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--reset">
                <div class="article-header article-header--secondary">
                    <div class="grid-row">
                        <div class="column column--two-thirds column--reset">
                            <h1 data-uipath="document.title">${document.title}</h1>
                        </div>
                    </div>
                </div>

                <#-- BEGIN optional 'summary section' -->
                <#if document.summary?has_content>
                <div id="section-summary" class="article-section article-section--summary article-section--reset-top">
                    <div class="grid-row">
                        <div class="column column--two-thirds column--reset">
                            <h2>Summary</h2>
                            <p>${document.summary}</p>
                        </div>
                    </div>
                </div>
                </#if>
                <#-- END optional 'summary section' -->


                <div class="article-section">
                    <div class="rich-user-content">
                        <table class="table table-emphasised">
                            <tbody>
                                <tr>
                                    <td><@fmt.message key="labels.data-controller"/></td>
                                    <td>${document.datacontroller}</td>
                                </tr>
                                
                                <tr>
                                    <td><@fmt.message key="labels.how-info-is-used"/></td>
                                    <td>${document.howuseinformation}</td>
                                </tr>

                                <tr>
                                    <td><@fmt.message key="labels.sensitivity"/></td>
                                    <td>${document.sensitivity?then("Yes", "No")}</td>
                                </tr>

                                <#if document.whocanaccess?has_content>
                                <tr>
                                    <td><@fmt.message key="labels.who-has-access"/></td>
                                    <td>${document.whocanaccess}</td>
                                </tr>
                                </#if>
                                
                                <tr>
                                    <td><@fmt.message key="labels.transferred-outside-uk"/></td>
                                    <td>${document.outsideuk}</td>
                                </tr>

                                <#if document.timeretained?has_content>
                                <tr>
                                    <td><@fmt.message key="labels.how-long-is-it-kept"/></td>
                                    <td>${document.timeretained}</td>
                                </tr>
                                </#if>
                                
                                <#if document.lawfulbasis?has_content>
                                <tr>
                                    <td><@fmt.message key="labels.lawful-basis"/></td>
                                    <td>${lawfulbasis[document.lawfulbasis]}</td>
                                </tr>
                                </#if>
                                
                                <tr>
                                    <td><@fmt.message key="labels.your-rights"/></td>
                                    <td>
                                        <ul class="list list--reset list--with-icons">
                                            <#list rights?keys as key>
                                                <li>
                                                    <#if document.rights?seq_contains(key)>
                                                        <img src="<@hst.webfile path="images/icon-tick.png"/>" alt="Tick" class="image-icon" width="24"/>
                                                    <#else>
                                                        <img src="<@hst.webfile path="images/icon-cross.png"/>" alt="Tick" class="image-icon" width="24"/>
                                                    </#if>
                                                    <span>${rights[key]}<span>
                                                </li>                                                
                                            </#list>
                                        </ul>
                                    </td>
                                </tr>

                                <#if document.withdrawconsent?? && document.withdrawconsent.content?has_content>
                                <tr>
                                    <td><@fmt.message key="labels.withdrawing-consent" /></td>
                                    <td>
                                        <@hst.html hippohtml=document.withdrawconsent contentRewriter=gaContentRewriter/>
                                    </td>
                                </tr>
                                </#if>

                                <#if document.computerdecision?has_content>
                                <tr>
                                    <td><@fmt.message key="labels.decision-by-computers"/></td>
                                    <td>${document.computerdecision}</td>
                                </tr>
                                </#if>

                                <#if document.datasource?has_content>
                                <tr>
                                    <td><@fmt.message key="labels.data-source"/></td>
                                    <td>${document.datasource}</td>
                                </tr>
                                </#if>

                                <#if document.legallywhy?? && document.legallywhy.content?has_content>
                                <tr>
                                    <td><@fmt.message key="labels.why-we-can-use-legally"/></td>
                                    <td><@hst.html hippohtml=document.legallywhy contentRewriter=gaContentRewriter/></td>
                                </tr>
                                </#if>
                            </tbody>
                        </table>
                    </div>
                </div>
                
                <#-- [FTL-BEGIN] 'Links' section -->
                <#if document.blocks?? && document.blocks?size!=0>
                <div class="article-section">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="cta-list cta-list--grid">
                                <div class="grid-row">
                                    <div class="column column--reset">
                                        <h2 class="list-title"><@fmt.message key="labels.items-section-title"/></h2>
                                    </div>
                                </div>

                                <#-- [FTL-BEGIN] Link grid -->
                                <@linkGrid document.blocks></@linkGrid>
                                <#-- [FTL-END] Link grid -->
                            </div>
                        </div>
                    </div>
                </div>
                </#if>
                <#-- [FTL-END] 'Links' section -->
            </div>
        </div>
    </div>
</article>
