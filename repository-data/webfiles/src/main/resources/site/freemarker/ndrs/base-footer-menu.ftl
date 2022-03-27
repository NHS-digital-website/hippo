<#ftl output_format="HTML">

<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] >
<#assign getRemoteSvg="uk.nhs.digital.freemarker.svg.SvgFromUrl"?new() />

<#-- @ftlvariable name="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->
<#-- @ftlvariable name="socialMediaLinks" type="java.util.Map<String, SocialMediaLink>" -->

<#include "../include/imports.ftl">

<#if menu??>
    <footer class="nhsd-o-global-footer" id="footer">
        <div class="nhsd-t-grid">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col-12">
                    <div class="nhsd-t-grid nhsd-t-grid--nested">
                        <div class="nhsd-t-row">
                            <#if menu.siteMenuItems??>
                                <#list menu.siteMenuItems as item>
                                    <#if !item.hstLink?? && !item.externalLink??>
                                        <#if !item?is_first>
                                            </ul></nav>
                                        </#if>
                                        <nav class="nhsd-t-col-xs-6 nhsd-t-col-s-6 nhsd-t-col-l-3" aria-labelledby="footer-heading-${item.name?lower_case?replace(" ", "-")}">
                                        <div class="nhsd-t-body-s nhsd-!t-font-weight-bold nhsd-!t-margin-bottom-1" id="footer-heading-${item.name?lower_case?replace(" ", "-")}">${item.name}</div>
                                        <ul class="nhsd-t-list nhsd-t-list--links">
                                        <#elseif item.hstLink?? && socialMediaLinks[item.hstLink.path]??>
                                        <#assign socialMediaLink = socialMediaLinks[item.hstLink.path] />
                                        <li class="nhsd-t-body-s">
                                            <span class="nhsd-a-icon nhsd-a-icon--size-l nhsd-a-icon--col-dark-grey">
                                                <#if socialMediaLink.icon == "other" >
                                                    <@hst.link hippobean=socialMediaLink.linkIcon var="iconLink" />
                                                    <img src="${iconLink}">
                                                <#else>
                                                    <#assign svg = getRemoteSvg(socialMediaLink.icon) />
                                                    ${svg.data?no_esc}
                                                </#if>
                                            </span>
                                            <a target="_blank" class="nhsd-a-link nhsd-a-link--col-dark-grey" href="${socialMediaLink.nhsDigitalUrl}" rel="external">
                                                ${item.name}
                                                <span class="nhsd-t-sr-only">(external link, opens in a new tab)</span>
                                            </a>
                                        </li>
                                    <#else>
                                        <#if item.hstLink??>
                                            <#assign href><@hst.link link=item.hstLink/></#assign>
                                        <#elseif item.externalLink??>
                                            <#assign href>${item.externalLink?replace("\"", "")}</#assign>
                                        </#if>
                                        <#if  item.selected || item.expanded>
                                            <li class="nhsd-t-body-s active">
                                                <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="${href}">${item.name}</a>
                                            </li>
                                        <#else>
                                            <li class="nhsd-t-body-s">
                                                <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="${href}" rel="external">${item.name}</a>
                                            </li>
                                        </#if>
                                    </#if>
                                    <#if item?is_last>
                                        </ul></nav>
                                    </#if>
                                </#list>
                            </#if>
                            <nav class="nhsd-t-col-xs-6 nhsd-t-col-s-6 nhsd-t-col-l-3" aria-labelledby="footer-heading-1680-5070">
                                <div class="nhsd-t-body-s nhsd-!t-font-weight-bold nhsd-!t-margin-bottom-4" id="footer-heading-1680-5070">Working Together</div>
                                <div>
                                    <span class="footer-logo-wrap" style="text-center;">
                                        <svg class="footer-logo ndrs-logo" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 70 40" width="100" height="60" aria-hidden="true" focusable="false">
                                            <path d="M39.2338 11.0517C38.0125 4.74524 32.6827 0 26.2904 0C19.898 0 14.5664 4.74524 13.3451 11.0517H39.2338Z" fill="#4AB0E5" />
                                            <path d="M47.079 11.0517C45.8577 4.74524 40.5261 0 34.1337 0C27.7414 0 22.4098 4.74524 21.1885 11.0517H47.079Z" fill="#55B687" />
                                            <path d="M39.2338 11.0515C38.2739 6.09334 34.7724 2.09882 30.2121 0.61792C25.6517 2.09882 22.1502 6.09334 21.1885 11.0515H39.2338Z" fill="#2CA9AB" />
                                            <path d="M3.29613 16.6578C3.43395 16.6578 3.5488 16.6654 3.64068 16.6807C3.73256 16.6884 3.81296 16.7114 3.88187 16.7496C3.95843 16.7803 4.03117 16.83 4.10008 16.8989C4.16899 16.9602 4.24556 17.0444 4.32978 17.1516L13.0468 28.2576C13.0162 27.9896 12.9933 27.7292 12.9779 27.4766C12.9703 27.2163 12.9665 26.9751 12.9665 26.753V16.6578H15.6884V33.265H14.092C13.847 33.265 13.6441 33.2267 13.4833 33.1501C13.3225 33.0736 13.1655 32.9358 13.0124 32.7367L4.32978 21.6767C4.35275 21.9217 4.36806 22.1667 4.37572 22.4117C4.39103 22.6491 4.39869 22.8673 4.39869 23.0664V33.265H1.67676V16.6578H3.29613Z" fill="#343434" />
                                            <path d="M33.7854 24.9614C33.7854 26.1788 33.5825 27.2966 33.1767 28.315C32.7709 29.3333 32.2005 30.21 31.4655 30.945C30.7304 31.6801 29.8461 32.2505 28.8124 32.6563C27.7788 33.0621 26.6341 33.265 25.3784 33.265H19.0388V16.6578H25.3784C26.6341 16.6578 27.7788 16.8645 28.8124 17.2779C29.8461 17.6837 30.7304 18.2542 31.4655 18.9892C32.2005 19.7166 32.7709 20.5894 33.1767 21.6078C33.5825 22.6261 33.7854 23.744 33.7854 24.9614ZM30.6271 24.9614C30.6271 24.0502 30.5046 23.2348 30.2595 22.5151C30.0145 21.7877 29.6662 21.1752 29.2144 20.6775C28.7627 20.1722 28.2114 19.7855 27.5606 19.5175C26.9174 19.2495 26.19 19.1155 25.3784 19.1155H22.1512V30.8072H25.3784C26.19 30.8072 26.9174 30.6732 27.5606 30.4052C28.2114 30.1373 28.7627 29.7544 29.2144 29.2567C29.6662 28.7514 30.0145 28.1389 30.2595 27.4192C30.5046 26.6918 30.6271 25.8725 30.6271 24.9614Z" fill="#343434" />
                                            <path d="M39.3917 26.776V33.265H36.3022V16.6578H41.3671C42.5003 16.6578 43.4688 16.7764 44.2728 17.0138C45.0844 17.2435 45.7467 17.5689 46.2597 17.99C46.7803 18.4111 47.1593 18.9165 47.3967 19.506C47.6417 20.0879 47.7642 20.7311 47.7642 21.4355C47.7642 21.9944 47.68 22.5227 47.5115 23.0204C47.3507 23.5181 47.1134 23.9698 46.7995 24.3756C46.4932 24.7814 46.1104 25.1375 45.651 25.4437C45.1992 25.75 44.6824 25.995 44.1005 26.1788C44.491 26.4008 44.8279 26.7186 45.1112 27.132L49.2687 33.265H46.4894C46.2214 33.265 45.9917 33.2114 45.8003 33.1042C45.6165 32.997 45.4596 32.8439 45.3294 32.6448L41.838 27.3273C41.7078 27.1282 41.5623 26.9866 41.4015 26.9023C41.2484 26.8181 41.0187 26.776 40.7125 26.776H39.3917ZM39.3917 24.5594H41.3212C41.9031 24.5594 42.4084 24.4867 42.8372 24.3412C43.2736 24.1957 43.6296 23.9966 43.9053 23.744C44.1886 23.4836 44.3991 23.1774 44.5369 22.8252C44.6748 22.473 44.7437 22.0863 44.7437 21.6652C44.7437 20.823 44.4642 20.176 43.9053 19.7242C43.354 19.2725 42.5079 19.0466 41.3671 19.0466H39.3917V24.5594Z" fill="#343434" />
                                            <path d="M60.1513 19.6783C60.067 19.8467 59.9675 19.9654 59.8526 20.0343C59.7455 20.1032 59.6153 20.1377 59.4622 20.1377C59.309 20.1377 59.1367 20.0803 58.9453 19.9654C58.7539 19.8429 58.528 19.7089 58.2677 19.5634C58.0074 19.418 57.7011 19.2878 57.3489 19.173C57.0044 19.0505 56.5947 18.9892 56.12 18.9892C55.6913 18.9892 55.3161 19.0428 54.9945 19.15C54.6806 19.2495 54.4126 19.3912 54.1906 19.5749C53.9762 19.7587 53.8154 19.9807 53.7082 20.2411C53.601 20.4937 53.5474 20.7732 53.5474 21.0795C53.5474 21.47 53.6546 21.7954 53.869 22.0557C54.091 22.316 54.382 22.5381 54.7418 22.7218C55.1017 22.9056 55.5113 23.0702 55.9707 23.2157C56.4301 23.3611 56.8972 23.5181 57.3719 23.6865C57.8543 23.8473 58.3251 24.0388 58.7845 24.2608C59.2439 24.4752 59.6536 24.7508 60.0134 25.0877C60.3733 25.4169 60.6604 25.8227 60.8748 26.3051C61.0968 26.7875 61.2079 27.3732 61.2079 28.0623C61.2079 28.8127 61.0777 29.5171 60.8174 30.1755C60.5647 30.8264 60.1895 31.3968 59.6918 31.8868C59.2018 32.3692 58.6008 32.752 57.8887 33.0353C57.1767 33.3109 56.3612 33.4487 55.4424 33.4487C54.9141 33.4487 54.3935 33.3952 53.8805 33.288C53.3675 33.1884 52.8736 33.0429 52.3989 32.8515C51.9319 32.6601 51.4916 32.4304 51.0781 32.1624C50.6723 31.8945 50.3087 31.5958 49.9871 31.2666L50.8829 29.7851C50.9671 29.6779 51.0667 29.5898 51.1815 29.5209C51.304 29.4443 51.438 29.406 51.5835 29.406C51.7749 29.406 51.9816 29.4864 52.2037 29.6472C52.4257 29.8004 52.686 29.9726 52.9846 30.1641C53.2909 30.3555 53.6469 30.5316 54.0527 30.6924C54.4662 30.8455 54.9601 30.9221 55.5343 30.9221C56.4148 30.9221 57.0963 30.7153 57.5786 30.3019C58.061 29.8808 58.3022 29.2797 58.3022 28.4987C58.3022 28.0623 58.1912 27.7063 57.9691 27.4306C57.7547 27.155 57.4676 26.9253 57.1077 26.7415C56.7479 26.5501 56.3382 26.3893 55.8788 26.2592C55.4195 26.129 54.9524 25.9874 54.4777 25.8342C54.003 25.6811 53.5359 25.4973 53.0765 25.283C52.6171 25.0686 52.2075 24.7891 51.8476 24.4446C51.4878 24.1 51.1968 23.6712 50.9748 23.1582C50.7604 22.6376 50.6532 21.9983 50.6532 21.2403C50.6532 20.6354 50.7719 20.0458 51.0092 19.4716C51.2542 18.8973 51.6065 18.3882 52.0658 17.9441C52.5329 17.5 53.1033 17.144 53.7771 16.876C54.4509 16.608 55.2204 16.474 56.0856 16.474C57.0656 16.474 57.9691 16.6271 58.796 16.9334C59.6229 17.2397 60.3274 17.6684 60.9093 18.2197L60.1513 19.6783Z" fill="#343434" />
                                        </svg>
                                    </span>
                                    <span class="footer-logo-wrap">
                                        <svg class="footer-logo nhsd-logo" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 80 60" width="80" height="60" aria-hidden="true" focusable="false">
                                            <rect fill="#005BBB" x="0" width="80" height="32.1" />
                                            <path fill="#FFFFFF" d="M76.9,3.8L75.2,9c-1.3-0.6-3.2-1.2-5.7-1.2c-2.8,0-5,0.4-5,2.5c0,3.7,10.1,2.3,10.1,10.2c0,7.1-6.7,9-12.7,9c-2.7,0-5.8-0.6-8-1.3l1.6-5.3c1.4,0.9,4.1,1.5,6.4,1.5c2.2,0,5.5-0.4,5.5-3.1c0-4.2-10.1-2.6-10.1-9.9c0-6.7,5.9-8.7,11.6-8.7C72.1,2.7,75.1,3,76.9,3.8" />
                                            <polygon fill="#FFFFFF" points="56.9,3.1 51.5,29 44.5,29 46.8,17.9 38.6,17.9 36.3,29 29.3,29 34.7,3.1 41.7,3.1 39.6,13 47.8,13 49.9,3.1   " />
                                            <polygon fill="#FFFFFF" points="32.1,3.1 26.6,29 18,29 12.5,11.1 12.5,11.1 8.8,29 2.3,29 7.8,3.1 16.5,3.1 21.8,21 21.9,21 25.6,3.1  " />
                                            <rect x="63.5" y="38.5" fill="#231F20" width="3.1" height="16" />
                                            <path fill="#231F20" d="M51.6,43.1c1.3-0.6,2.7-0.9,4.1-0.9c3.6,0,5.1,1.5,5.1,5v1.7c0,1.2,0,2.1,0,3c0,0.9,0.1,1.7,0.1,2.6h-2.7c-0.1-0.6-0.1-1.2-0.1-1.8h0c-0.7,1.3-2.2,2-3.6,2c-2.1,0-4.1-1.3-4.1-3.5c0-1.8,0.8-3,2-3.6c1.2-0.6,2.7-0.7,4-0.7H58c0-1.9-0.8-2.5-2.7-2.5c-1.3,0-2.7,0.5-3.7,1.3V43.1z M55.2,52.3c0.8,0,1.7-0.4,2.2-1.1c0.5-0.7,0.6-1.5,0.6-2.4h-1.3c-1.4,0-3.4,0.2-3.4,2C53.3,51.9,54.1,52.3,55.2,52.3" />
                                            <path fill="#231F20" d="M42.7,45.2h-2.3v-2.2h2.3v-2.7l3.1-1v3.6h2.7v2.2h-2.7v5.3c0,1,0.3,1.9,1.4,1.9c0.5,0,1.1-0.1,1.4-0.3l0.1,2.4c-0.7,0.2-1.5,0.3-2.3,0.3c-2.4,0-3.7-1.5-3.7-3.8L42.7,45.2z" />
                                            <path fill="#231F20" d="M35.2,38.5h3.1v2.7h-3.1V38.5z M35.2,42.9h3.1v11.6h-3.1V42.9z" />
                                            <path fill="#231F20" d="M32.6,42.9v10.7c0,3.2-1.3,6-6.2,6c-1.4,0-2.8-0.3-4.1-0.8l0.3-2.6c0.9,0.5,2.5,1,3.4,1c3.4,0,3.6-2.3,3.6-4.3h0c-0.6,1-1.9,1.9-3.7,1.9c-3.5,0-4.8-2.8-4.8-6c0-2.8,1.5-6.1,5-6.1
                                                    c1.6,0,2.8,0.5,3.6,1.9h0v-1.6L32.6,42.9z M29.5,48.7c0-2-0.8-3.7-2.6-3.7c-2.1,0-2.8,1.9-2.8,3.7c0,1.6,0.8,3.6,2.6,3.6C28.7,52.3,29.5,50.7,29.5,48.7" />
                                            <path fill="#231F20" d="M15.9,38.5H19v2.7h-3.1V38.5z M15.9,42.9H19v11.6h-3.1V42.9z" />
                                            <path fill="#231F20" d="M0,38.5h4.4c4.9,0,9.2,1.7,9.2,8s-4.3,8-9.2,8H0V38.5z M3.2,52H5c2.7,0,5.3-2,5.3-5.5C10.3,43,7.7,41,5,41H3.2V52z" />
                                        </svg>
                                    </span>
                                    <span class="footer-logo-wrap"></span>
                                    <li style="font-size:12px;list-style:none;"><span class="footer-copy-txt" >Copyright © 2022 All rights reserved</span></li>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <@hst.cmseditmenu menu=menu/>
    </footer>
</#if>
