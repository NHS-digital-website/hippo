<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<div style="border-top: 1px solid #768692;">
    <div class="document-content">
        <p class="flush zeta">Have a question? Call us on 0300 303 5678 or contact <a href="mail:enquiries@nhsdigital.nhs.uk">enquiries@nhsdigital.nhs.uk</a></p>
        <p class="flush zeta push-double--bottom">Tell us what you think of the new website beta.</p>
    </div>
</div>

<footer class="footer">
    <div class="footer__inner">
        <div class="layout layout--flush">
            <div class="layout__item layout-2-3">
                <ul class="footer__inner__list">
                    <@hst.link var="termslink" path="/publications/about/terms-and-conditions"/>
                    <li class="footer__inner__list__item"><a href="${termslink}" class="footer__inner__list__item__link">Terms &amp; Conditions</a></li><!--
                    --><li class="footer__inner__list__item"><a href="#" class="footer__inner__list__item__link">Privacy and cookies</a></li><!--
                    --><li class="footer__inner__list__item"><a href="#" class="footer__inner__list__item__link">Accessibility help</a></li>
                </ul>
                <p class="flush">
                    © 2018 NHS Digital
                </p>
            </div><!--

            --><div class="layout__item layout-1-3">
                <ul class="footer__inner__sharelist">
                  <li class="footer__inner__sharelist__item">Follow us:</li>
                  <li class="footer__inner__sharelist__item">
                    <a href="https://twitter.com/NHSDigital" title="" class="footer__inner__sharelist__item__link">
                      <img src="https://digital.nhs.uk/media/297/Twitter-Icon/icon/Twitter_Logo_White_On_Blue" srcset="https://digital.nhs.uk/media/297/Twitter-Icon/icon@2x/Twitter_Logo_White_On_Blue 2x" alt="Twitter">
                    </a>
                  </li>
                  <li class="footer__inner__sharelist__item">
                    <a href="https://www.linkedin.com/company/nhs-digital" title="" class="footer__inner__sharelist__item__link">
                      <img src="https://digital.nhs.uk/media/296/LinkedIn-Icon/icon/In-2C-128px-R" srcset="https://digital.nhs.uk/media/296/LinkedIn-Icon/icon@2x/In-2C-128px-R 2x" alt="Icon for LinkedIn">
                    </a>
                  </li>
                </ul>
            </div>
        </div>

    </div>
</footer>
