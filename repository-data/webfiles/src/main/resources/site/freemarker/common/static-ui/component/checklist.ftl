<#ftl output_format="HTML">

<#include "../../../include/imports.ftl">

<@hst.webfile var="checklistWidgetTickIcon" path="images/icon/Icon-tick-v3_v2.svg" />
<@hst.webfile var="checklistWidgetCrossIcon" path="images/icon/Icon-cross-v2_v2.svg" />
<@hst.webfile var="checklistWidgetShieldIcon" path="images/icon/shield-with-tick.svg" />

<@hst.webfile var="checklistTickIcon" path="images/icon-tick.png" />
<@hst.webfile var="checklistCrossIcon" path="images/icon-cross.png" />

<div class="grid-row">
    <div class="column column--two-thirds">
        <div class="article-section article-section--summary">
            <h1>Checklists</h1>
            <div class="article-section--summary">
                <p>Use checklists to display itemised lists with visual markers to signify state of each item.</p>
            </div>
        </div>   

        <div class="article-section">
            <h2>Checklist widget</h2>

            <div>
                <div class="check-list-widget check-list-widget--with-heading">
                    <h3 class="check-list-widget__label" id="-bullet-list" data-uipath="website.contentblock.checklist.heading">"Bullet" list</h3>
                    <ul class="check-list-widget__list check-list-widget--tick">
                        <li>
                            <span><p>This is an example list entry</p></span>
                        </li>
                        <li>
                            <span><p>This is an example list entry with a <a href="/about-nhs-digital/corporate-information-and-documents/nhs-digital-style-guidelines/features-of-the-nhs-digital-website-cms/check-lists">link</a></p></span>
                        </li>
                    </ul>
                </div>

                <div>
                    <h4>Code example</h4>
                    <pre><code>
&lt;ul class=&quot;check-list-widget__list check-list-widget--tick&quot;&gt;
    &lt;li&gt;
        &lt;span&gt;&lt;p&gt;This is an example list entry&lt;/p&gt;&lt;/span&gt;
    &lt;/li&gt;
    &lt;li&gt;
        &lt;span&gt;&lt;p&gt;This is an example list entry with a &lt;a href=&quot;/about-nhs-digital/corporate-information-and-documents/nhs-digital-style-guidelines/features-of-the-nhs-digital-website-cms/check-lists&quot;&gt;link&lt;/a&gt;&lt;/p&gt;&lt;/span&gt;
    &lt;/li&gt;
&lt;/ul&gt;
                    </code></pre>
                </div>
            </div>

            <div>
                <div class="check-list-widget check-list-widget--with-heading">
                    <h3 class="check-list-widget__label" id="-tick-list" data-uipath="website.contentblock.checklist.heading">"Tick" list</h3>
                    <ul class="check-list-widget__list check-list-widget--tick check-list-widget--tick--no-bullet">
                        <li>
                            <img aria-hidden="true" class="check-list-widget-icon" src="${checklistWidgetTickIcon}" alt="Tick Image">
                            <span><p>This is an example list entry</p></span>
                        </li>
                        <li>
                            <img aria-hidden="true" class="check-list-widget-icon" src="${checklistWidgetTickIcon}" alt="Tick Image">
                            <span><p>This is an example list entry with a <a href="/about-nhs-digital/corporate-information-and-documents/nhs-digital-style-guidelines/features-of-the-nhs-digital-website-cms/check-lists">link</a></p></span>
                        </li>
                    </ul>
                </div>
                <div>
                    <h4>Code example</h4>
                    <pre><code>
&lt;ul class=&quot;check-list-widget__list check-list-widget--tick check-list-widget--tick--no-bullet&quot;&gt;
    &lt;li&gt;
        &lt;img aria-hidden=&quot;true&quot; class=&quot;check-list-widget-icon&quot; src=&quot;${checklistWidgetTickIcon}&quot; alt=&quot;Tick Image&quot;&gt;
        &lt;span&gt;&lt;p&gt;This is an example list entry&lt;/p&gt;&lt;/span&gt;
    &lt;/li&gt;
    &lt;li&gt;
        &lt;img aria-hidden=&quot;true&quot; class=&quot;check-list-widget-icon&quot; src=&quot;${checklistWidgetTickIcon}&quot; alt=&quot;Tick Image&quot;&gt;
        &lt;span&gt;&lt;p&gt;This is an example list entry with a &lt;a href=&quot;/about-nhs-digital/corporate-information-and-documents/nhs-digital-style-guidelines/features-of-the-nhs-digital-website-cms/check-lists&quot;&gt;link&lt;/a&gt;&lt;/p&gt;&lt;/span&gt;
    &lt;/li&gt;
&lt;/ul&gt;
                    </code></pre>
                </div>
            </div>

            <div>
                <div class="check-list-widget check-list-widget--with-heading">
                    <h3 class="check-list-widget__label" id="-cross-list" data-uipath="website.contentblock.checklist.heading">"Cross" list</h3>

                    <ul class="check-list-widget__list check-list-widget--tick check-list-widget--tick--no-bullet">
                        <li>
                            <img aria-hidden="true" class="check-list-widget-icon" src="${checklistWidgetCrossIcon}" alt="Cross Image">
                            <span><p>This is an example list entry</p></span>
                        </li>
                        <li>
                            <img aria-hidden="true" class="check-list-widget-icon" src="${checklistWidgetCrossIcon}" alt="Cross Image">
                            <span><p>This is an example list entry with a <a href="/about-nhs-digital/corporate-information-and-documents/nhs-digital-style-guidelines/features-of-the-nhs-digital-website-cms/check-lists">link</a></p></span>
                        </li>
                    </ul>
                </div>

                <div>
                    <h4>Code example</h4>
                    <pre><code>
&lt;ul class=&quot;check-list-widget__list check-list-widget--tick check-list-widget--tick--no-bullet&quot;&gt;
    &lt;li&gt;
        &lt;img aria-hidden=&quot;true&quot; class=&quot;check-list-widget-icon&quot; src=&quot;${checklistWidgetCrossIcon}&quot; alt=&quot;Cross Image&quot;&gt;
        &lt;span&gt;&lt;p&gt;This is an example list entry&lt;/p&gt;&lt;/span&gt;
    &lt;/li&gt;
    &lt;li&gt;
        &lt;img aria-hidden=&quot;true&quot; class=&quot;check-list-widget-icon&quot; src=&quot;${checklistWidgetCrossIcon}&quot; alt=&quot;Cross Image&quot;&gt;
        &lt;span&gt;&lt;p&gt;This is an example list entry with a &lt;a href=&quot;/about-nhs-digital/corporate-information-and-documents/nhs-digital-style-guidelines/features-of-the-nhs-digital-website-cms/check-lists&quot;&gt;link&lt;/a&gt;&lt;/p&gt;&lt;/span&gt;
    &lt;/li&gt;
&lt;/ul&gt;
                </code></pre>
                </div>
            </div>

            <div>
                <div class="check-list-widget check-list-widget--with-heading">
                    <h3 class="check-list-widget__label" id="-custom-list" data-uipath="website.contentblock.checklist.heading">"Custom" list</h3>
                    <ul class="check-list-widget__list check-list-widget--tick check-list-widget--tick--no-bullet">
                        <li>
                            <img aria-hidden="true" src="${checklistWidgetShieldIcon}" alt="Custom image" class="check-list-widget-icon">
                            <span><p>This is an example list entry</p></span>
                        </li>
                        <li>
                            <img aria-hidden="true" src="${checklistWidgetShieldIcon}" alt="Custom image" class="check-list-widget-icon">
                            <span><p>This is an example list entry with a <a href="/about-nhs-digital/corporate-information-and-documents/nhs-digital-style-guidelines/features-of-the-nhs-digital-website-cms/check-lists">link</a></p></span>
                        </li>
                    </ul>
                </div>

                <div>
                    <h4>Code example</h4>
                    <pre><code>
&lt;ul class=&quot;check-list-widget__list check-list-widget--tick check-list-widget--tick--no-bullet&quot;&gt;
    &lt;li&gt;
        &lt;img aria-hidden=&quot;true&quot; src=&quot;${checklistWidgetShieldIcon}&quot; alt=&quot;Custom image&quot; class=&quot;check-list-widget-icon&quot;&gt;
        &lt;span&gt;&lt;p&gt;This is an example list entry&lt;/p&gt;&lt;/span&gt;
    &lt;/li&gt;
    &lt;li&gt;
        &lt;img aria-hidden=&quot;true&quot; src=&quot;${checklistWidgetShieldIcon}&quot; alt=&quot;Custom image&quot; class=&quot;check-list-widget-icon&quot;&gt;
        &lt;span&gt;&lt;p&gt;This is an example list entry with a &lt;a href=&quot;/about-nhs-digital/corporate-information-and-documents/nhs-digital-style-guidelines/features-of-the-nhs-digital-website-cms/check-lists&quot;&gt;link&lt;/a&gt;&lt;/p&gt;&lt;/span&gt;
    &lt;/li&gt;
&lt;/ul&gt;
                    </code></pre>
                </div>
            </div>
        </div>

        <div class="article-section">
            <h2>Checklist</h2>
            
            <div>
                <h3>Checklist (with regular spacing)</h3>
                <ul class="checklist">
                    <li class="checklist__item">
                        <img aria-hidden="true" src="${checklistTickIcon}" alt="Green tick icon" class="checklist__icon checklist__icon--small" />
                        <span class="checklist__label">List item #1</span>
                    </li>
                    <li class="checklist__item">
                        <img aria-hidden="true" src="${checklistCrossIcon}" alt="Red cross icon" class="checklist__icon checklist__icon--small" />
                        <span class="checklist__label">List item #2</span>
                    </li>
                </ul>

                <div>
                    <h4>Code example</h4>
                    <pre><code>
&lt;ul class=&quot;checklist checklist&quot;&gt;
    &lt;li class=&quot;checklist__item&quot;&gt;
        &lt;img aria-hidden=&quot;true&quot; src=&quot;${checklistTickIcon}&quot; alt=&quot;Green tick icon&quot; class=&quot;checklist__icon checklist__icon--small&quot; /&gt;
        &lt;span class=&quot;checklist__label&quot;&gt;List item #1&lt;/span&gt;
    &lt;/li&gt;
    &lt;li class=&quot;checklist__item&quot;&gt;
        &lt;img aria-hidden=&quot;true&quot; src=&quot;${checklistCrossIcon}&quot; alt=&quot;Red cross icon&quot; class=&quot;checklist__icon checklist__icon--small&quot; /&gt;
        &lt;span class=&quot;checklist__label&quot;&gt;List item #2&lt;/span&gt;
    &lt;/li&gt;
&lt;/ul&gt;
                    </code></pre>
                </div>
            </div>

            <div>
                <h3>Checklist (with condensed spacing)</h3>
                <ul class="checklist checklist--condensed">
                    <li class="checklist__item">
                        <img aria-hidden="true" src="${checklistTickIcon}" alt="Green tick icon" class="checklist__icon checklist__icon--small" />
                        <span class="checklist__label">List item #1</span>
                    </li>
                    <li class="checklist__item">
                        <img aria-hidden="true" src="${checklistCrossIcon}" alt="Red cross icon" class="checklist__icon checklist__icon--small" />
                        <span class="checklist__label">List item #2</span>
                    </li>
                </ul>

                <div>
                    <h4>Code example</h4>
                    <pre><code>
&lt;ul class=&quot;checklist checklist--condensed&quot;&gt;
    &lt;li class=&quot;checklist__item&quot;&gt;
        &lt;img aria-hidden=&quot;true&quot; src=&quot;${checklistTickIcon}&quot; alt=&quot;Green tick icon&quot; class=&quot;checklist__icon checklist__icon--small&quot; /&gt;
        &lt;span class=&quot;checklist__label&quot;&gt;List item #1&lt;/span&gt;
    &lt;/li&gt;
    &lt;li class=&quot;checklist__item&quot;&gt;
        &lt;img aria-hidden=&quot;true&quot; src=&quot;${checklistCrossIcon}&quot; alt=&quot;Red cross icon&quot; class=&quot;checklist__icon checklist__icon--small&quot; /&gt;
        &lt;span class=&quot;checklist__label&quot;&gt;List item #2&lt;/span&gt;
    &lt;/li&gt;
&lt;/ul&gt;
                    </code></pre>
                </div>
            </div>
        </div>
    </div>
</div>
