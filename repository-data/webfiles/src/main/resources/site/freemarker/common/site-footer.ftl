<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<@hst.setBundle basename="emails"/>

<footer class="site-footer white-links" id="footer">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="grid-row">
            <div class="column column--reset">

                <div class="site-footer__section site-footer__section--top">
                    <div class="grid-wrapper grid-wrapper--collapse">
                        <div class="grid-row">
                            <div class="column column--three-quarters column--left">
                                <nav class="site-footer__menu site-footer__menu--1">
                                    <h2 class="list__header">About NHS Digital</h2>
                                    <ul class="list list--reset list--close">
                                        <li><a href="/about-nhs-digital">About us</a></li>
                                        <li><a href="/our-strategy">Our strategy</a></li>
                                        <li><a href="/annual-report-and-accounts">Annual report and accounts</a></li>
                                        <li><a href="/our-business-plan">Our business plan</a></li>
                                        <li><a href="/press-releases">Press releases</a></li>
                                        <li><a href="/how-we-look-after-your-information">How we look after your information</a></li>
                                    </ul>
                                </nav>
                            </div>

                            <div class="column column--one-quarter column--right">
                                <nav class="site-footer__menu site-footer__menu--3">
                                    <ul class="list list--reset list--close">
                                        <li><a href="/about-nhs-digital/contact-us">Contact us</a></li>
                                        <li><a href="/press-office">Press office</a></li>
                                        <li><a href="/freedom-of-information">Freedom of information</a></li>
                                        <li><a href="/careers">Careers</a></li>
                                        <li class="last-item"><a href="/forms/tell-us-what-you-think-of-our-website" title="Provide website feedback">Tell us what you think of our website</a></li>
                                    </ul>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="site-footer__section site-footer__section--bottom">
                    <div class="grid-wrapper grid-wrapper--collapse">
                        <div class="grid-row">
                            <div class="column column--left">
                                <nav class="site-footer__menu site-footer__menu--4">
                                    <ul class="list list--inline list--reset">
                                        <li><a href="/about-nhs-digital/privacy-and-cookies">Privacy and cookies</a></li>
                                        <li><a href="/about-nhs-digital/terms-and-conditions">Terms and conditions</a></li>
                                        <li><a href="/about-nhs-digital/accessibility">Accessibility</a></li>
                                        <li><a href="javascript:Cookiebot.renew()">Cookie consent</a></li>
                                    </ul>
                                </nav>

                                <div class="site-footer__menu site-footer__menu--5">
                                    <ul class="list list--inline list--reset">
                                        <li>
                                            <a href="https://www.linkedin.com/company/nhs-digital" onClick="${getOnClickMethodCall('Footer', 'https://www.linkedin.com/company/nhs-digital')}" onKeyUp="return vjsu.onKeyUp(event)">
                                                <img src="<@hst.webfile  path="images/icon-linkedin.png"/>" alt="LinkedIn logo" class="image-icon" /><span>LinkedIn</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="https://twitter.com/NHSDigital" onClick="${getOnClickMethodCall('Footer', 'https://twitter.com/NHSDigital')}" onKeyUp="return vjsu.onKeyUp(event)">
                                                <img src="<@hst.webfile  path="images/icon-twitter.png"/>" alt="Twitter logo" class="image-icon"/><span>Twitter</span>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</footer>
