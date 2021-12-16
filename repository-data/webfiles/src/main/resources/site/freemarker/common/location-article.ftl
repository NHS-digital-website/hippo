<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Location" -->

<#include "../include/imports.ftl">

<#include "macro/sections/codeSection.ftl">
<#include "macro/furtherInformationSection.ftl">
<#include "../publicationsystem/macro/structured-text.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/fileMetaAppendix.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/fileIconByMimeType.ftl">

<#assign hasLocalAddress = document.locAddress?? && document.locAddress?has_content/>
<#assign hasUrgentinformation = document.urgentinformation?? && document.urgentinformation.content?has_content/>
<#assign hasImageOfSite = document.imageofsite?? && document.imageofsite?has_content/>
<#assign hasOpeningHours = document.locopeninghours?? && document.locopeninghours?has_content/>
<#assign hasDirectionToSiteByPublicTransports = document.directionToSiteByPublicTransportation?? && document.directionToSiteByPublicTransportation?has_content/>
<#assign hasOnsiteCarsParking = document.onsitecarsparking?? && document.onsitecarsparking?has_content/>
<#assign hasCyclesParking = document.cyclesparking?? && document.cyclesparking?has_content/>
<#assign hasLocalTaxis = document.localtaxis?? && document.localtaxis?has_content/>
<#assign hasUniquePropertyRefrenceNumber = document.uniquePropertyReferenceNumber?? && document.uniquePropertyReferenceNumber?has_content />
<#assign hasDunsNumber = document.dunsnumber?? && document.dunsnumber?has_content />
<#assign hasOdsCode = document.odscode?? && document.odscode?has_content/>
<#assign hasOtherLocationData = hasUniquePropertyRefrenceNumber || hasDunsNumber || hasOdsCode />
<#assign hasDirectionToSiteByCar = document.directiontositebycars?? && document.directiontositebycars?has_content/>
<#assign hasTopLink = document.includeTopLink?? && document.includeTopLink/>
<#if hasLocalAddress >
<#assign hasGeocoordinates = document.locAddress.geocoordinates?? && document.locAddress.geocoordinates.content?has_content />
<#else>
<#assign hasGeocoordinates = hasLocalAddress />
</#if>

<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <h1 class="local-header__title" data-uipath="document.title" itemprop="name">${document.name}</h1>
                    <hr class="hr hr--short hr--light">

                    <div class="article-header__subtitle" data-uipath="article.description"><span itemprop="name">${document.description}</span></div>

                    <div class="detail-list-grid">
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <#if hasLocalAddress>
                                    <dt class="detail-list__key">Address:</dt>
                                    <dd class="detail-list__value" data-uipath="document.locAddress">
                                        <#if document.locAddress.buildinglocation?has_content >
                                        <span itemprop="location">${document.locAddress.buildinglocation}</span>,</#if> <span itemprop="number">${document.locAddress.buildingname}</span><#if document.locAddress.street?has_content >
                                        <span itemprop="street">${document.locAddress.street}</span></#if><#if document.locAddress.area?has_content>, <span itemprop="area">${document.locAddress.area}</span></#if><#if document.locAddress.city?has_content>, <span itemprop="city">${document.locAddress.city}</span></#if><#if document.locAddress.county?has_content>, <span itemprop="county">${document.locAddress.county}</span></#if>, <span itemprop="country">${document.locAddress.country}</span>, <span itemprop="postcode">${document.locAddress.postalCode}</span>&nbsp;&nbsp;
                                        <#if document.locAddress.mapLink?has_content>
                                        <a href="${document.locAddress.mapLink}"
                                            title="Open StreetMap link in new window" target="_blank"><span itemprop="subjectof" itemscope itemtype="https://schema.org/webapi">Open in streetmap</span>
                                        </a>
                                        </#if>
                                    </#if>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        <#if document.telephone?has_content >
                        <#assign telNumber = document.telephone?remove_beginning("0")/>
                        <#assign telephNumber = telNumber?ensure_starts_with("+44") />
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key">Contact number: </dt>
                                    <dd class="detail-list__value" data-uipath="document.threewordskey">
                                     <a href="tel:${telephNumber}"
                                         title="telephone number"><span>${document.telephone}</span>
                                     </a>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>
                        <#if document.emailaddress?has_content >
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key">Email address: </dt>
                                    <dd class="detail-list__value" data-uipath="document.threewordskey">
                                     <a href="mailto:${document.emailaddress}"
                                         title="email address"><span>${document.emailaddress}</span>
                                     </a>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>
                        <#if document.threewordskey?has_content >
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key">What3words location: </dt>
                                    <dd class="detail-list__value" data-uipath="document.threewordskey">
                                     <a href=https://what3words.com/${document.threewordskey}
                                         title="Open what3word link in new window" target="_blank"><span>${document.threewordskey}</span>
                                     </a>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>
                        <#if document.pluscode?has_content >
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key">Plus codes location: </dt>
                                    <dd class="detail-list__value" data-uipath="document.pluscode">
                                     <a href="https://plus.codes/${document.pluscode}"
                                         title="Open pluscode link in new window" target="_blank"><span>${document.pluscode}</span>
                                     </a>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>
                        <#if hasGeocoordinates>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key">Geocordinates: </dt>
                                    <dd class="geocoordinates" data-uipath="document.locAddress.geocoordinates">
                                    <@hst.html hippohtml=document.locAddress.geocoordinates contentRewriter=gaContentRewriter/>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>
                        <#if document.locationWebsite?? && document.locationWebsite.link?has_content >
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key">Location website: </dt>
                                    <dd class="detail-list__value" data-uipath="document.pluscode">
                                     <a href="${document.locationWebsite.link}"
                                         title="Open location website in new window" target="_blank"><span itemprop="subjectof" itemscope itemtype="https://schema.org/webapi">${document.locationWebsite.link}</span>
                                     </a>
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
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-third page-block--sticky-nav page-block--sidebar article-section-nav-outer-wrapper">
                <!-- start sticky-nav -->
                <div id="sticky-nav">
                    <#assign links = [] />
                    <#if hasOpeningHours >
                        <#assign links += [{ "url": "#" + "Openinghours", "title": "Opening hours" }] />
                    </#if>
                    <#if hasDirectionToSiteByPublicTransports >
                        <#list document.directionToSiteByPublicTransportation as dirToSiteByPublicTrans >
                        <#assign links += [{ "url": "#" + "${dirToSiteByPublicTrans.publictransportType?lower_case}", "title": "Directions by ${dirToSiteByPublicTrans.publictransportType?lower_case}" }] />
                        </#list>
                    </#if>
                    <#if hasLocalTaxis>
                        <#assign links += [{ "url": "#" + "taxi", "title": "Taxi" }] />
                    </#if>
                    <#if hasDirectionToSiteByCar>
                        <#assign links += [{ "url": "#" + "directionsbycar", "title": "Directions by car" }] />
                    </#if>
                    <#if hasOnsiteCarsParking>
                        <#assign links += [{ "url": "#" + "carparking", "title": "Car parking" }] />
                    </#if>
                    <#if hasCyclesParking>
                        <#assign links += [{ "url": "#" + "cycleparking", "title": "Cycle parking" }] />
                    </#if>
                    <#if hasOtherLocationData>
                        <#assign links += [{ "url": "#" + "otherlocationdata", "title": "Other location data" }] />
                    </#if>
                    <@stickyNavSections getStickySectionNavLinks({"includeTopLink": hasTopLink, "sections": links})></@stickyNavSections>
                </div>
                <!-- end sticky-nav -->
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <#if hasUrgentinformation >
                    <div class="article-section">
                        <div id="Theoffice" class=" article-section article-section--highlighted">
                            <div class="emphasis-box emphasis-box-important">
                                <div data-uipath="website.contentblock.urgentinformation" class="emphasis-box__content">
                                  <@hst.html hippohtml=document.urgentinformation contentRewriter=gaContentRewriter/>
                                </div>
                            </div>
                        </div>
                    </div>
                </#if>
                <#if hasImageOfSite >
                <div class="rich-text-content">
                    <@hst.link hippobean=document.imageofsite fullyQualified=true var="iconImage" />
                    <img src="${iconImage}" alt="${document.imageOfSiteAltText}" />
                </div>
                </#if>
                <#if hasOpeningHours>
                    <div class="article-section">
                        <div id="Openinghours" class="article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.openinghours.title">Opening hours</h2>
                            <div class="loc-list-grid">
                            <#list document.locopeninghours as opningHours >
                            <#if opningHours.monday?has_content>
                            <div class="grid-row">
                                <div class="column column--reset">
                                    <dl class="loc-list">
                                        <dt class="loc-list__key">Monday</dt>
                                        <dd class="loc-list__value" >
                                         ${opningHours.monday}
                                        </dd>
                                     </dl>
                                 </div>
                            </div>
                            </#if>
                             <#if opningHours.tuesday?has_content>
                                 <div class="grid-row">
                                     <div class="column column--reset">
                                         <dl class="loc-list">
                                             <dt class="loc-list__key">Tuesday</dt>
                                             <dd class="loc-list__value" >
                                              ${opningHours.tuesday}
                                             </dd>
                                          </dl>
                                      </div>
                                 </div>
                                 </#if>
                                  <#if opningHours.wednesday?has_content>
                                  <div class="grid-row">
                                      <div class="column column--reset">
                                          <dl class="loc-list">
                                              <dt class="loc-list__key">Wednesday</dt>
                                              <dd class="loc-list__value" >
                                               ${opningHours.wednesday}
                                              </dd>
                                           </dl>
                                       </div>
                                  </div>
                                  </#if>
                                   <#if opningHours.thursday?has_content>
                                   <div class="grid-row">
                                       <div class="column column--reset">
                                           <dl class="loc-list">
                                               <dt class="loc-list__key">Thursday</dt>
                                               <dd class="loc-list__value" >
                                                ${opningHours.thursday}
                                               </dd>
                                            </dl>
                                        </div>
                                   </div>
                                   </#if>
                                    <#if opningHours.friday?has_content>
                                    <div class="grid-row">
                                        <div class="column column--reset">
                                            <dl class="loc-list">
                                                <dt class="loc-list__key">Friday</dt>
                                                <dd class="loc-list__value" >
                                                 ${opningHours.friday}
                                                </dd>
                                             </dl>
                                         </div>
                                    </div>
                                    </#if>
                                     <#if opningHours.saturday?has_content>
                                     <div class="grid-row">
                                         <div class="column column--reset">
                                             <dl class="loc-list">
                                                 <dt class="loc-list__key">Saturday</dt>
                                                 <dd class="loc-list__value" >
                                                  ${opningHours.saturday}
                                                 </dd>
                                              </dl>
                                          </div>
                                     </div>
                                     </#if>
                                      <#if opningHours.sunday?has_content>
                                      <div class="grid-row">
                                          <div class="column column--reset">
                                              <dl class="loc-list">
                                                  <dt class="loc-list__key">Sunday</dt>
                                                  <dd class="loc-list__value" >
                                                   ${opningHours.sunday}
                                                  </dd>
                                               </dl>
                                           </div>
                                      </div>
                                     </#if>
                              </#list>
                             </div>
                         </div>
                     </div>
                </#if>
                <#if hasDirectionToSiteByPublicTransports>
                    <#list document.directionToSiteByPublicTransportation as dirToSiteByPublicTrans >
                    <div class="article-section">
                        <div id="${dirToSiteByPublicTrans.publictransportType?lower_case}" class=" article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.publictransport.title">Directions by ${dirToSiteByPublicTrans.publictransportType?lower_case}</h2>
                            <div data-uipath="website.contentblock.bypublictransporttext" class="rich-text-content">
                              <@hst.html hippohtml=dirToSiteByPublicTrans.bypublictransporttext contentRewriter=gaContentRewriter/>
                            </div>
                            <#list dirToSiteByPublicTrans.publicTransportStations as publictransportstation >
                            <div>
                              <#if publictransportstation.name?has_content >
                              <span class="station-name">${publictransportstation.name}</span></#if><#if publictransportstation.distance?has_content ><span class="distance"> (${publictransportstation.distance})</span><h3></#if>
                            </div>
                            <div>
                            <#assign hasWalkingTime = publictransportstation.walkingtime?has_content/>
                            <#assign hasDrivingTime = publictransportstation.drivingtime?has_content/>
                             <#if hasWalkingTime>
                              <span>${publictransportstation.walkingtime} walking</span></#if><#if hasWalkingTime && hasDrivingTime>, </#if><#if hasDrivingTime><span>${publictransportstation.drivingtime} driving</span></#if>
                            </div>
                            <#if publictransportstation.geocoordinates?? && publictransportstation.geocoordinates.content?has_content>
                            <div class="rich-text-content">
                                <@hst.html hippohtml=publictransportstation.geocoordinates contentRewriter=gaContentRewriter/>
                            </div>
                            </#if>
                            <#if publictransportstation.byPublicTransportPicture?has_content>
                            <div class="rich-text-content">
                                <@hst.link hippobean=publictransportstation.byPublicTransportPicture fullyQualified=true var="iconImagePubTrans" />
                                <img src="${iconImagePubTrans}" alt="${publictransportstation.byPublicTransportPictureAltText}" />
                            </div>
                            </#if>
                            <#if publictransportstation.byPublicTransportVideo?has_content>
                            <div data-uipath="website.contentblock.publictransport.video" class="rich-text-content">
                                <@hst.html hippohtml=publictransportstation.byPublicTransportVideo contentRewriter=gaContentRewriter/>
                            </div>
                            </#if>
                            </#list>
                        </div>
                    </div>
                    </#list>
                </#if>
                <#if hasLocalTaxis>
                    <div class="article-section">
                        <div id="taxi" class=" article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.taxi.title">Taxi</h2>
                            <#list document.localtaxis as locTaxi >
                            <div>
                                <#if locTaxi.taxicompanyname?has_content>
                                <span class="station-name">${locTaxi.taxicompanyname}</span></br>
                                </#if>
                                <#if locTaxi.taxitelephonenumber?has_content>
                                <span>Phone: ${locTaxi.taxitelephonenumber}</span>
                                </#if>
                            </div>
                            </#list>
                        </div>
                    </div>
                </#if>
                <#if hasDirectionToSiteByCar >
                    <div class="article-section">
                        <div id="directionsbycar" class=" article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.directiontositebycars.title">Directions by car</h2>
                            <div>
                                <#if document.directiontositebycars.bycartext?? && document.directiontositebycars.bycartext.content?has_content>
                                <div data-uipath="website.directiontositebycars.bycartext" class="rich-text-content">
                                    <@hst.html hippohtml=document.directiontositebycars.bycartext contentRewriter=gaContentRewriter/>
                                </div>
                                </#if>
                                <#if document.directiontositebycars.bycarpicture?has_content>
                                <div class="rich-text-content">
                                    <@hst.link hippobean=document.directiontositebycars.bycarpicture fullyQualified=true var="iconImagecarpark" />
                                    <img src="${iconImagecarpark}" alt="${document.directiontositebycars.byCarPictureAltText}" />
                                </div>
                                </#if>
                                <#if document.directiontositebycars.bycarvideo?? && document.directiontositebycars.bycarvideo.content?has_content>
                                <div data-uipath="website.directiontositebycars.bycartext" class="rich-text-content">
                                    <@hst.html hippohtml=document.directiontositebycars.bycarvideo contentRewriter=gaContentRewriter/>
                                </div>
                                </#if>
                            </div>
                        </div>
                    </div>
                </#if>
                <#if hasOnsiteCarsParking>
                    <div class="article-section">
                        <div id="carparking" class=" article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.theoffice.title">Car parking</h2>
                            <#if document.onsitecarsparking.name?has_content >
                            <#if document.onsitecarsparking.websiteLink?? && document.onsitecarsparking.websiteLink.link?has_content >
                            <#assign hrefValue = document.onsitecarsparking.websiteLink.link/>
                            <#else>
                            <#assign hrefValue = ""/>
                            </#if>
                            <a href="${hrefValue}"
                                title="Open car parking website in new window" target="_blank"><span class="station-name" itemprop="location">${document.onsitecarsparking.name}</span>
                            </a>
                            </#if>
                            <#if document.onsitecarsparking.distancefromsite?has_content>
                            &nbsp;<span class="distance">(${document.onsitecarsparking.distancefromsite})</span>
                            </#if>
                            <#if document.onsitecarsparking.address?? && document.onsitecarsparking.address.content?has_content>
                            <div data-uipath="website.contentblock.section.content" class="rich-text-content">
                                <@hst.html hippohtml=document.onsitecarsparking.address contentRewriter=gaContentRewriter/>
                            </div>
                            </#if>
                            <#if document.onsitecarsparking.detailsofparking?has_content>
                            <div>
                            <span itemprop="detailsofparking">${document.onsitecarsparking.detailsofparking}</span>
                            </div>
                            </#if>
                            <#if document.onsitecarsparking.costs?has_content>
                            <div>
                            <span itemprop="costs">Parking cost: ${document.onsitecarsparking.costs}</span>
                            <div>
                            </#if>
                            <#if document.onsitecarsparking.numberofspaces?has_content>
                            <span itemprop="numberofspaces">Number of spaces: ${document.onsitecarsparking.numberofspaces}</span></br>
                            </#if>
                            <#if document.onsitecarsparking.geocoordinates?? && document.onsitecarsparking.geocoordinates.content?has_content>
                            <div>
                                <span class="car-label">Geocordinates: </span><span class="car-geocoordinates"><@hst.html hippohtml=document.onsitecarsparking.geocoordinates contentRewriter=gaContentRewriter/></div>
                            </div>
                            </#if>
                            </div>
                        </div>
                    </div>
                </#if>
                <#if hasCyclesParking>
                    <div class="article-section">
                        <div id="cycleparking" class=" article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.cycleparking.title">Cycle parking</h2>
                            <#if document.cyclesparking.details?has_content>
                            <span itemprop="details">${document.cyclesparking.details}</span></br>
                            </#if>
                            <#if document.cyclesparking.numberofspaces?has_content>
                            <span itemprop="number">Number of spaces: ${document.cyclesparking.numberofspaces}</span></br>
                            </#if>
                        </div>
                    </div>
                </#if>
                <#if hasOtherLocationData>
                    <div class="article-section">
                        <div id="otherlocationdata" class=" article-section article-section--highlighted">
                            <h2 data-uipath="website.contentblock.theoffice.title">Other location data</h2>
                            <#if hasUniquePropertyRefrenceNumber>
                            <span itemprop="number">Unique Property Reference Number: ${document.uniquePropertyReferenceNumber}</span></br>
                            </#if>
                            <#if hasDunsNumber>
                            <span itemprop="number">D-U-N-S number: ${document.dunsnumber}</span></br>
                            </#if>
                            <#if hasOdsCode>
                            <span itemprop="number">ODS code: ${document.odscode}</span>
                            </#if>
                        </div>
                    </div>
                </#if>
            </div>
          </div>
      </div>
</article>
