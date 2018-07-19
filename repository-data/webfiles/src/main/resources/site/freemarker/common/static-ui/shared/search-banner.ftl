<#ftl output_format="HTML">

<div class="search-banner" aria-label="Search form">
  <div class="grid-wrapper grid-wrapper--collapse">
    <p class="search-banner__headline">NHS Digital provides technology, information and services, which help the NHS deliver better care</p>
    <form role="search" method="get" action="" class="search-banner__form">
        <div>
            <label for="query" class="visually-hidden">Search</label>
            <input type="text" name="query" id="query" class="search-banner__input" placeholder="What are you looking for today?" value="${query!""}" title="Search">
        </div>
        <div>
            <button class="search-banner__button">Search</button>
        </div>
    </form>
  </div>
</div>