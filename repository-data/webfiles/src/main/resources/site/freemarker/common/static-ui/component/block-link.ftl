<#ftl output_format="HTML">
<#include "../../macro/component/icon.ftl">

<div class="grid-row">
    <div class="column column--two-thirds">
        <div class="article-section article-section--summary">
            <h1>Block link</h1>
            <div class="article-section--summary">
                <p>Use the block link component to create a large clickable link.</p>
            </div>
        </div>
        <div class="article-section article-section--summary">
            <a href="#" class="block-link" itemprop="contentUrl">
                <div class="block-link__header">
                    <@icon name="spreadsheet" size="2x" />
                </div>
                <div class="block-link__body">
                    <span class="block-link__title" itemprop="name">Recorded Dementia Diagnoses, December 2018: Summary</span>
                    <span class="block-link__meta regular">[xls, <span class="fileSize">size: <span itemprop="contentSize">645.3 kB</span></span>]</span>
                </div>
            </a>

            <a href="#" class="block-link" itemprop="contentUrl">
                <div class="block-link__header">
                    <@icon name="pdf" size="2x" />
                </div>
                <div class="block-link__body">
                    <span class="block-link__title" itemprop="name">Dementia 65+ Diagnosis Rate Indicator 12 Month Time Series, by NHS Organisation of Responsibility, December 2018</span>
                    <span class="block-link__meta regular">[pdf, <span class="fileSize">size: <span itemprop="contentSize">645.3 kB</span></span>]</span>
                </div>
            </a>

            <a href="#" class="block-link" itemprop="contentUrl">
                <div class="block-link__header">
                    <@icon name="csv" size="2x" />
                </div>
                <div class="block-link__body">
                    <span class="block-link__title" itemprop="name">Dementia 65+ Diagnosis Rate Indicator 12 Month Time Series, by NHS Organisation of Responsibility, this one goes on a bit longer than the others December 2019</span>
                    <span class="block-link__meta regular">[csv, <span class="fileSize">size: <span itemprop="contentSize">645.3 kB</span></span>]</span>
                </div>
            </a>

        </div>
    </div>
</div>


