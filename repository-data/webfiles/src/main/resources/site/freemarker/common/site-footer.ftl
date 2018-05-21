<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<footer role="contentinfo" class="site-footer white-links" id="footer">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="grid-row">
            <div class="column column--reset">

                <div class="site-footer__section site-footer__section--top">
                    <div class="grid-wrapper grid-wrapper--collapse">
                        <div class="grid-row">
                            <div class="column column--one-half column--left">
                                <nav class="site-footer__menu site-footer__menu--1">
                                    <h3 class="list__header">About NHS Digital</h3>
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

                            <div class="column column--one-quarter">
                                <nav class="site-footer__menu site-footer__menu--2">
                                    <h3 class="list__header">Contact us</h3>
                                    <div class="list">
                                        <div class="list__item">
                                            <span>Telephone:</span><br>
                                            <a href="tel:004403003035678">0300 303 5678</a>
                                        </div>

                                        <div class="list__item">
                                            <span>Email:</span><br>
                                            <a href="mailto:enquiries@nhsdigital.nhs.uk">enquiries@nhsdigital.nhs.uk</a>
                                        </div>
                                    </div>
                                </nav>
                            </div>

                            <div class="column column--one-quarter column--right">
                                <nav class="site-footer__menu site-footer__menu--3">
                                    <ul class="list list--reset list--close">
                                        <li><a href="/press-office">Press office</a></li>
                                        <li><a href="/freedom-of-information">Freedom of information</a></li>
                                        <li><a href="/careers">Careers</a></li>
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
                                        <li><a href="/legal" title="Legal">Legal</a></li>
                                        <li><a href="/privacy-and-cookies" title="Privacy and cookies">Privacy and cookies</a></li>
                                        <li><a href="/terms-and-conditions" title="Terms and conditions">Terms and conditions</a></li>
                                        <li><a href="/accessibility" title="Accessibility help">Accessibility</a></li>
                                        <li><a href="javascript: Cookiebot.renew()" title="Cookie consent">Cookie consent</a></li>
                                    </ul>
                                </nav>

                                <div class="site-footer__menu site-footer__menu--5">
                                    <ul class="list list--inline list--reset">
                                        <li>
                                            <a href="https://www.linkedin.com/company/nhs-digital" onClick="${getOnClickMethodCall('Footer', 'https://www.linkedin.com/company/nhs-digital')}">
                                                <img src="<@hst.webfile  path="images/icon-linkedin.png"/>" alt="Linkedin logo" class="image-icon" /><span>LinkedIn</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="https://www.twitter.com/NHSDigital" onClick="${getOnClickMethodCall('Footer', 'https://www.twitter.com/NHSDigital')}">
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
