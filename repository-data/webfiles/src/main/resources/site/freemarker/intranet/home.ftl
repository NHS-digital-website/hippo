<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "../common/macro/component/actionLink.ftl">
<#include "../common/macro/svgIcons.ftl">

<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#-- TODO - Include the meta tags and the head contributions-->
<#assign documentTitle = "Hello world!" />

<div class="intra-home-header">
    <div class="intra-home-header__inner">
        <div class="intra-home-header__inner-contents">
            <div class="intra-home-header__greeting">
                <h1><span class="intra-home-header__greeting-welcome">Welcome</span> <span>Christopher</span></h1>
            </div>
            <div class="intra-home-header__weather">
                <span class="intra-home-header__weather-text">Exeter, 16°C</span>
                <@svgIcon id="weather" className="intra-home-header__weather-icon" />
            </div>
        </div>
    </div>
</div>

<div class="intra-home-grid">
    <div class="intra-home-grid__left-col">
        <div class="intra-box">
            <h2 class="intra-box__title">Trending Articles</h2>

            <ul class="intra-home-article-grid">
                <li class="intra-home-article-grid-item">
                    <article class="intra-home-article-grid-article">
                        <div class="intra-home-article-grid-article__header">
                            <span class="intra-info-tag">News</span>
                            <img src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="Alt text" aria-hidden="true">
                        </div>
                        <div class="intra-home-article-grid-article__contents">
                            <span class="intra-comments-blob intra-comments-blob--bubble"><@svgIcon id="comments" /> 17</span>
                            <h1 class="intra-home-article-grid-article__title"><a href="#">Data that saves lives</a></h1>
                            <div class="intra-home-article-grid-article__summary">
                                <p>Lorem ipsum is a pseudo-Latin text used in design</p>    
                            </div>
                            <span class="intra-home-article-grid-article__date">27 November 2019</span>
                        </div>
                    </article>
                </li>

                <li class="intra-home-article-grid-item">
                    <article class="intra-home-article-grid-article">
                        <div class="intra-home-article-grid-article__header">
                            <span class="intra-info-tag">Announcement</span>
                            <img src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="Alt text" aria-hidden="true">
                        </div>
                        <div class="intra-home-article-grid-article__contents">
                            <span class="intra-comments-blob intra-comments-blob--bubble"><@svgIcon id="comments" /> 17</span>
                            <h1 class="intra-home-article-grid-article__title"><a href="#">Timesheet submissions</a></h1>
                            <div class="intra-home-article-grid-article__summary">
                                <p>Lorem ipsum is a pseudo-Latin text used in design</p>    
                            </div>
                            <span class="intra-home-article-grid-article__date">27 November 2019</span>
                        </div>
                    </article>
                </li>

                <li class="intra-home-article-grid-item">
                    <article class="intra-home-article-grid-article">
                        <div class="intra-home-article-grid-article__header">
                            <span class="intra-info-tag">News</span>
                            <img src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="Alt text" aria-hidden="true">
                        </div>
                        <div class="intra-home-article-grid-article__contents">
                            <span class="intra-comments-blob intra-comments-blob--bubble"><@svgIcon id="comments" /> 17</span>
                            <h1 class="intra-home-article-grid-article__title"><a href="#">A festival of genomics</a></h1>
                            <div class="intra-home-article-grid-article__summary">
                                <p>Lorem ipsum is a pseudo-Latin text used in design</p>    
                            </div>
                            <span class="intra-home-article-grid-article__date">27 November 2019</span>
                        </div>
                    </article>
                </li>

                <li class="intra-home-article-grid-item">
                    <article class="intra-home-article-grid-article">
                        <div class="intra-home-article-grid-article__header">
                            <span class="intra-info-tag">Announcement</span>
                            <img src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="Alt text" aria-hidden="true">
                        </div>
                        <div class="intra-home-article-grid-article__contents">
                            <span class="intra-comments-blob intra-comments-blob--bubble"><@svgIcon id="comments" /> 17</span>
                            <h1 class="intra-home-article-grid-article__title"><a href="#">Mandatory training</a></h1>
                            <div class="intra-home-article-grid-article__summary">
                                <p>Lorem ipsum is a pseudo-Latin text used in design</p>    
                            </div>
                            <span class="intra-home-article-grid-article__date">27 November 2019</span>
                        </div>
                    </article>
                </li>

                <li class="intra-home-article-grid-item">
                    <article class="intra-home-article-grid-article">
                        <div class="intra-home-article-grid-article__header">
                            <span class="intra-info-tag">Blog</span>
                            <img src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="Alt text" aria-hidden="true">
                        </div>
                        <div class="intra-home-article-grid-article__contents">
                            <h1 class="intra-home-article-grid-article__title"><a href="#">Lent has started</a></h1>
                            <div class="intra-home-article-grid-article__summary">
                                <p>Lorem ipsum is a pseudo-Latin text used in design</p>    
                            </div>
                            <span class="intra-home-article-grid-article__date">27 November 2019</span>
                        </div>
                    </article>
                </li>

                <li class="intra-home-article-grid-item">
                    <article class="intra-home-article-grid-article">
                        <div class="intra-home-article-grid-article__header">
                            <span class="intra-info-tag">News</span>
                            <img src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="Alt text" aria-hidden="true">
                        </div>
                        <div class="intra-home-article-grid-article__contents">
                            <span class="intra-comments-blob intra-comments-blob--bubble"><@svgIcon id="comments" /> 17</span>
                            <h1 class="intra-home-article-grid-article__title"><a href="#">Legislation for screening</a></h1>
                            <div class="intra-home-article-grid-article__summary">
                                <p>Lorem ipsum is a pseudo-Latin text used in design</p>    
                            </div>
                            <span class="intra-home-article-grid-article__date">27 November 2019</span>
                        </div>
                    </article>
                </li>
            </ul>

            <@actionLink title="View all news & announcements" link="#" />
        </div>

        <div class="intra-box">
            <h2 class="intra-box__title">Recommended People</h2>
            
            <ul class="intra-people-grid">
                <li class="intra-people-grid-item">
                    <div>
                        <img class="intra-people-grid-item__photo" src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="Alt text" aria-hidden="true">
                    </div>
                    <div>
                        <a href="#" class="intra-people-grid-item__name">Mark Richardson</a>
                    </div>
                    <div>
                        <span class="intra-info-tag intra-info-tag--light">Finance</span>
                    </div>
                    <div>
                        <span class="intra-people-grid-item__job-role">Infrastructure Architect</span>
                    </div>
                </li>
                <li class="intra-people-grid-item">
                    <div>
                        <img class="intra-people-grid-item__photo" src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="Alt text" aria-hidden="true">
                    </div>
                    <div>
                        <a href="#" class="intra-people-grid-item__name">Corinne Booth</a>
                    </div>
                    <div>
                        <span class="intra-info-tag intra-info-tag--light">Travel</span>
                    </div>
                    <div>
                        <span class="intra-people-grid-item__job-role">Infrastructure Architect</span>
                    </div>
                </li>
                <li class="intra-people-grid-item">
                    <div>
                        <img class="intra-people-grid-item__photo" src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="Alt text" aria-hidden="true">    
                    </div>
                    <div>
                        <a href="#" class="intra-people-grid-item__name">Richard Cooke</a>    
                    </div>
                    <div>
                        <span class="intra-info-tag intra-info-tag--light">HR</span>    
                    </div>    
                    <div>
                        <span class="intra-people-grid-item__job-role">Infrastructure Architect</span>
                    </div>
                </li>
                <li class="intra-people-grid-item">
                    <div>
                        <img class="intra-people-grid-item__photo" src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="Alt text" aria-hidden="true">
                    </div>
                    <div>
                        <a href="#" class="intra-people-grid-item__name">Hariet Clarke</a>
                    </div>
                    <div>
                        <span class="intra-info-tag intra-info-tag--light">Travel</span>
                    </div>
                    <div>
                        <span class="intra-people-grid-item__job-role">Infrastructure Architect</span>
                    </div>
                </li>
            </ul>

            <@actionLink title="View all People & Teams" link="#" />
        </div>

        <div class="intra-box">
            <h2 class="intra-box__title">Collaborate</h2>
            
            <ul class="intra-articleblock-group">
                <li>
                    <article class="intra-articleblock">
                        <h1 class="intra-articleblock__title">
                            <a href="#">Lent has started</a>
                            <span class="intra-comments-blob"><@svgIcon id="comments" /> 17</span>
                        </h1>
                        <div class="intra-articleblock__text rich-text-content">
                            <p>Reading the awful attitudes and comments of the NHS staff though (the clinician, who diagnosed Nicola and Becky’s ex-manager) made me...</p>
                        </div>

                        <div class="intra-articleblock__meta">
                            <span class="intra-articleblock__meta-name">Angeliki Antonarou</span>
                            <span class="intra-articleblock__meta-time">7 minutes ago</span>
                        </div>
                    </article>
                </li>
                <li>
                    <article class="intra-articleblock">
                        <h1 class="intra-articleblock__title">
                            <a href="#">Festival of Genomic</a>
                            <span class="intra-comments-blob"><@svgIcon id="comments" /> 17</span>
                        </h1>
                        <div class="intra-articleblock__text rich-text-content">
                            <p>There is no doubt that over the last five years have been the most successful insight in NHS England , and beyond that, amongst the very best to...</p>
                        </div>

                        <div class="intra-articleblock__meta">
                            <span class="intra-articleblock__meta-name">Harold Thompson</span>
                            <span class="intra-articleblock__meta-time">7 minutes ago</span>
                        </div>
                    </article>
                </li>
            </ul>

            <@actionLink title="View all Conversations" link="#" />
        </div>
    </div>
    <div class="intra-home-grid__right-col">
        <div class="intra-box intra-box--compact">
            <div class="intra-box intra-box--nested">
                <h2 class="intra-box__title">Tasks</h2>

                <ul class="intra-task-grid">
                    <li class="intra-task-grid-item">
                        <article class="intra-task-grid-task">
                            <a href="#" class="intra-task-grid-task__link">
                                <div class="intra-task-grid-task__icon">
                                    <@svgIcon id="briefcase" />
                                </div>
                                <h1 class="intra-task-grid-task__title">Book annual leave</h1>
                            </a>
                        </article>
                    </li>
                    <li class="intra-task-grid-item">
                        <article class="intra-task-grid-task">
                            <a href="#" class="intra-task-grid-task__link">
                                <div class="intra-task-grid-task__icon">
                                    <@svgIcon id="page" />
                                </div>
                                <h1 class="intra-task-grid-task__title">Submit timesheet</h1>
                            </a>
                        </article>
                    </li>

                    <li class="intra-task-grid-item">
                        <article class="intra-task-grid-task">
                            <a href="#" class="intra-task-grid-task__link">
                                <div class="intra-task-grid-task__icon">
                                    <@svgIcon id="house" />
                                </div>
                                <h1 class="intra-task-grid-task__title">Book a room</h1>
                            </a>
                        </article>
                    </li>

                    <li class="intra-task-grid-item">
                        <article class="intra-task-grid-task">
                            <a href="#" class="intra-task-grid-task__link">
                                <div class="intra-task-grid-task__icon">
                                    <@svgIcon id="lightbulb" />
                                </div>
                                <h1 class="intra-task-grid-task__title">Resolve it</h1>
                            </a>
                        </article>
                    </li>
                    
                    <li class="intra-task-grid-item">
                        <article class="intra-task-grid-task">
                            <a href="#" class="intra-task-grid-task__link">
                                <div class="intra-task-grid-task__icon">
                                    <@svgIcon id="connections" />
                                </div>
                                <h1 class="intra-task-grid-task__title">Book travel</h1>
                            </a>
                        </article>
                    </li>
                    
                    <li class="intra-task-grid-item">
                        <article class="intra-task-grid-task">
                            <a href="#" class="intra-task-grid-task__link">
                                <div class="intra-task-grid-task__icon">
                                    <@svgIcon id="personal" />
                                </div>
                                <h1 class="intra-task-grid-task__title">Mandatory training</h1>
                            </a>
                        </article>
                    </li>
                </ul>

                <@actionLink title="View all Tasks" link="#" />
            </div>

            <div class="intra-box intra-box--nested">
                <h2 class="intra-box__title">Training</h2>
                
                <ul class="intra-articleblock-group">
                    <li>
                        <article class="intra-articleblock">
                            <h1 class="intra-articleblock__title"><a href="#">Fire Warden Training</a></h1>
                            <div class="intra-articleblock__text rich-text-content">
                                <p>Training regulations and schedule has now changed. Please check the schedule for more information</p>
                            </div>
                        </article>
                    </li>
                    <li>
                        <article class="intra-articleblock">
                            <h1 class="intra-articleblock__title"><a href="#">Health & Safety Training</a></h1>
                            <div class="intra-articleblock__text rich-text-content">
                                <p>Training regulations and schedule has now changed. Please check the schedule for more information</p>
                            </div>
                        </article>
                    </li>
                    <li>
                        <article class="intra-articleblock">
                            <h1 class="intra-articleblock__title"><a href="#">KOSH Training</a></h1>
                            <div class="intra-articleblock__text rich-text-content">
                                <p>Training regulations and schedule has now changed. Please check the schedule for more information </p>
                            </div>
                        </article>
                    </li>
                </ul>

                <@actionLink title="View all Articles" link="#" />
            </div>

            <div class="intra-box intra-box--nested">
                <h2 class="intra-box__title">NHS Digital Jobs</h2>

                <ul class="intra-articleblock-group">
                    <li>
                        <article class="intra-articleblock">
                            <h1 class="intra-articleblock__title"><a href="#">Communications Officer</a></h1>
                            <span class="intra-articleblock__subtitle">Leeds</span>
                            <div class="intra-articleblock__text rich-text-content">
                                <p><b>Salary:</b> £40,729.91</p>
                                <p><b>Posted:</b> 28/01/2020</p>
                                <p><b>Job Type:</b> Permanent</p>
                            </div>
                        </article>
                    </li>
                    <li>
                        <article class="intra-articleblock">
                            <h1 class="intra-articleblock__title"><a href="#">Higher Information Analyst</a></h1>
                            <span class="intra-articleblock__subtitle">Leeds</span>
                            <div class="intra-articleblock__text rich-text-content">
                                <p><b>Salary:</b> £40,729.91</p>
                                <p><b>Posted:</b> 28/01/2020</p>
                                <p><b>Job Type:</b> Permanent</p>
                            </div>
                        </article>
                    </li>
                    <li>
                        <article class="intra-articleblock">
                            <h1 class="intra-articleblock__title"><a href="#">User Researcher</a></h1>
                            <span class="intra-articleblock__subtitle">Leeds</span>
                            <div class="intra-articleblock__text rich-text-content">
                                <p><b>Salary:</b> £40,729.91</p>
                                <p><b>Posted:</b> 28/01/2020</p>
                                <p><b>Job Type:</b> Permanent</p>
                            </div>
                        </article>
                    </li>
                </ul>

                <@actionLink title="View all Jobs" link="#" />
            </div>
        </div>
    </div>
</div>
