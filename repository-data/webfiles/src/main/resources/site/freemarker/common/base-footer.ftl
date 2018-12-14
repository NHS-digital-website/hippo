<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<@hst.setBundle basename="emails"/>

<div style="border-top: 1px solid #768692;">
    <div class="document-content">
        <p class="flush zeta">Have a question? Call us on 0300 303 5678 or contact <a href="mailto:<@fmt.message key="email.enquiries"/>" title="<@fmt.message key="email.enquiries.title" />"><@fmt.message key="email.enquiries"/></a></p>
        <p class="flush zeta push-double--bottom">Tell us what you think of the new website beta.</p>
    </div>
</div>

<footer class="footer" id="footer">
    <div class="footer__inner">
        <div class="layout layout--flush">
            <div class="layout__item layout-2-3">
                <ul class="footer__inner__list">
                    <@hst.link var="termslink" path="/about/terms-and-conditions"/>
                    <@hst.link var="accessibilitylink" path="/about/accessibility-help"/>
                    <@hst.link var="privacylink" path="/about/privacy-and-cookies"/>
                    <li class="footer__inner__list__item"><a href="${termslink}" class="footer__inner__list__item__link">Terms and conditions</a></li><!--
                    --><li class="footer__inner__list__item"><a href="${privacylink}" class="footer__inner__list__item__link">Privacy and cookies</a></li><!--
                    --><li class="footer__inner__list__item"><a href="${accessibilitylink}" class="footer__inner__list__item__link">Accessibility</a></li>
                </ul>
                <p class="flush">
                    © 2018 NHS Digital
                </p>
            </div><!--

            --><div class="layout__item layout-1-3">
                <ul class="footer__inner__sharelist">
                  <li class="footer__inner__sharelist__item">Follow us:</li>
                  <li class="footer__inner__sharelist__item">
                      <a href="https://twitter.com/NHSDigital" title="" class="footer__inner__sharelist__item__link" target="_blank">
                          <img src="<@hst.webfile  path="images/icon-twitter.png"/>" alt="Twitter logo" class="image-icon" width="30"/>
                      </a>
                  </li>
                  <li class="footer__inner__sharelist__item">
                      <a href="https://www.linkedin.com/company/nhs-digital" title="" class="footer__inner__sharelist__item__link" target="_blank">
                          <img src="<@hst.webfile  path="images/icon-linkedin.png"/>" alt="Linkedin logo" class="image-icon" width="30" />
                      </a>
                  </li>
                </ul>
            </div>
        </div>

    </div>
</footer>
