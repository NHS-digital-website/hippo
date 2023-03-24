import { css } from 'lit';

/*
This file is reserved for any custom css that developers want to add to
customize their theme. Simply add your css to this file and yarn build.
*/

export default css`
    #the-main-body {
        overflow: unset;
        flex-flow: wrap;
    }

    #api-title, #api-info, #link-overview {
        display: none;
    }

    .nav-bar-expand-all, .nav-bar-collapse-all {
        display: none;
    }

    .nav-bar-section.operations {
        justify-content: flex-start;
        margin-left: 4px
    }

    .nav-bar-section-title {
        margin-left: -10px
    }

    .nav-bar-h2 {
        margin-left: 0;
    }

    .nav-bar-paths-under-tag {
        max-height: unset !important;
    }

    /* CSS var overrides */
    :host {
        --font-size-regular: 1rem;
        --font-size-small: .888rem;
        --font-size-mono: 1rem;
        --font-mono: monospace;


        --red: #da291c;
        --light-red: #f8d4d2;
        --pink: #ae2573;
        --green: #009639;
        --light-green: #78be20;
        --blue: #005bbb;
        --light-blue: #41b6e6;
        --orange: #fa9200;
        --yellow: #fae100;
        --light-yellow: #fef9cc;

        --fg: #3f525f;
        --fg2: #4e5a62;
        --fg3: #4e5a62;
        --light-fg: #4e5a62;
    }

    @media (max-width:63.99em) {
        :host {
            --font-size-regular: .888rem;
            --font-size-small: .778rem;
            --font-size-mono: .888rem;
        }
    }

    /* .m-markdown-small?? other elements code, pre */
    .m-markdown p,
    .m-markdown span,
    .m-markdown ul,
    .m-markdown ol {
        margin: 0 0 0.8333333333rem;
        line-height: 1.6;
    }

    .m-markdown p:not(:first-child) {
        margin-block-start: unset;
    }

    .m-markdown ul,
    .m-markdown ol {
        padding-inline-start: 20px !important;
        padding-left: 0;
    }

    .m-markdown li:not(:last-of-type):not(.nhsd-m-table__mobile-list li) {
        margin: 0 0 0.1333rem;
    }

    .m-markdown li {
       padding-left: 0.5rem;
       line-height: 1.6;
    }

    h1, h2, h3, h4, h5, h6 {
        font-weight: 600;
        color: #231f20;
        margin: 0 0 1.1111111111rem;
        padding-top: unset;
    }

    h1, h2 {
        font-size: 2rem;
        letter-spacing: -.0622rem;
        line-height: 1.1666;
    }

    h3 {
        font-size: 1.6669rem;
        letter-spacing: -.063rem;
        line-height: 1.20048;
    }

    h4 {
        font-size: 1.444rem;
        letter-spacing: -.0277rem;
        line-height: 1.19267;
    }

    h5 {
        font-size: 1.222rem;
        letter-spacing: -.0277rem;
        line-height: 1.3125;
    }

    h6 {
        font-size: 1rem;
        letter-spacing: -.0277rem;
        line-height: 1.3125;
    }

    @media (max-width:63.99em) {
        h1, h2 {
            font-size: 1.669rem;
            letter-spacing: -.01666rem;
            line-height: 1.1337;
        }

        h3 {
            font-size: 1.444rem;
            letter-spacing: -.01666rem;
            line-height: 1.19267;
        }

        h4 {
            font-size: 1.222rem;
            letter-spacing: -.01666rem;
            line-height: 1.22749;
        }

        h5 {
            font-size: 1rem;
            letter-spacing: -.01666rem;
            line-height: 1.3125;
        }

        h6 {
            font-size: .888rem;
            letter-spacing: -.01666rem;
            line-height: 1.3125;
        }
    }

    .method-fg.options,
    .method-fg.head,
    .method-fg.patch {
        color: #827717;
    }

    pre.example-panel {
        overflow: scroll;
    }

    tr.apikey div {
        max-height: none !important;
    }

    tr.apikey p {
        margin-bottom: 24px;
    }

    div.oauth-flow button {
        margin-top: 5px;
    }

    [class="toolbar-btn"] {
        position: absolute !important;
    }

    /* Slider - to finish */
    input[type="checkbox"]:checked {
        background-color: var(--blue);
        border-color: var(--blue);
    }

    input[type="checkbox"]:checked::after {
        border: 1px solid var(--blue);
    }

    /* line height is not exactly the same as NHSD */

    /* example-panel xml example no longer horizontal scrolls due to global line-height */

    /* NHSD links - inactive links are now weird when try this API deactivated, with smaller text link line seems to e too low */
    a {
      font-family: "Frutiger W01","Arial",sans-serif;
      position: relative;
      text-decoration: none;
    }

    a,
    a::after,
    a::before {
      box-sizing: border-box;
    }

    a,
    a:visited {
      padding-bottom: 2px;  /* maybe change to margin but first see if can fix underlying issue */
      border: 0;
      border-bottom: 1px solid var(--primary-colour, #005bbb);
      color: var(--primary-colour, #005bbb);
      transition-property: background-color,color;
      transition-duration: .15s;
    }

    a:focus,
    a:hover {
      padding-bottom: 0;
      border-bottom: 3px solid var(--black, #231f20);
      outline: 0;
      color: var(--black, #231f20);
      background-color: var(--yellow, #fae100);
    }

    a:active {
      border-bottom: 3px solid transparent;
      color: var(--primary-colour, #005bbb);
      background-color: transparent;
    }

    /* NHSD tables */
    .m-markdown-small table,
    .m-markdown table,
    .m-markdown-small tbody,
    .m-markdown tbody {
        overflow-x: auto;
        width: 100%;
        border-collapse: collapse;
        backface-visibility: visible;
        margin: 0 0 0.8333333333rem;
        border-spacing: unset;
        border: unset;
        border-radius: unset;
        font-size: var(--font-size-regular);
        line-height: 1.6;
        max-width: unset;
    }

    .m-markdown-small thead, .m-markdown thead {
        color: #231f20;
        font-weight: 600;
    }

    .m-markdown-small thead tr:first-child td,
    .m-markdown-small thead tr:first-child th,
    .m-markdown thead tr:first-child td,
    .m-markdown thead tr:first-child th {
        border: 0;
    }

    .m-markdown-small td,
    .m-markdown-small th,
    .m-markdown td,
    .m-markdown th {
        padding: .5555555556rem;
        text-align: left;
        line-height: 1.6;
        background-color: unset;
        vertical-align: inherit;
    }

    .m-markdown-small tbody tr:first-child td,
    .m-markdown-small tbody tr:first-child th,
    .m-markdown tbody tr:first-child td,
    .m-markdown tbody tr:first-child th {
        border-width: 2px;
    }

    .m-markdown-small tbody tr td,
    .m-markdown-small tbody tr th,
    .m-markdown tbody tr td,
    .m-markdown tbody tr th {
        border-top: 1px solid #d5dade;
        vertical-align: top;
    }

    .m-markdown-small td,
    .m-markdown-small th,
    .m-markdown-small td,
    .m-markdown-small th {
        padding: .5555555556rem;
        text-align: left;
    }

    .nhsd-m-table__mobile-list ul {
        padding-inline-start: unset !important;
    }

    .nhsd-m-table__mobile-list {
        margin-top: 15px;
    }
`;
