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

    main.main-content {
        padding: 6px 0.833rem;
    }

    #api-title, #api-info, #link-overview {
        display: none;
    }

    nav.nav-bar {
        padding: 6px 0.833rem;
    }

    @media only screen and (min-width: 1024px) {
        nav.nav-bar {
            position: sticky;
            top: 1rem;
            max-height: 95vh;
        }
    }

    @media only screen and (max-width: 1023px) {
        nav.nav-bar {
            width: 100%;
            display: flex;
        }
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

    .nav-bar-tag-and-paths.expanded .nav-bar-paths-under-tag {
        max-height: unset !important;
    }

    #auth,
    .section-gap--read-mode,
    .section-gap--focused-mode {
        padding: 0;
    }

    .section-gap--focused-mode {
        padding: 0;
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

    .m-markdown p,
    .m-markdown span,
    .m-markdown ul,
    .m-markdown ol {
        margin: 0 0 0.8333333333rem;
        line-height: 1.6;
    }

    @media (max-width:63.99em) {
        .m-markdown p,
        .m-markdown span,
        .m-markdown ul,
        .m-markdown ol,
        .m-markdown li,
         a {
            line-height: 1.4;
        }
    }

    .m-markdown-small p,
    .m-markdown-small span,
    .m-markdown-small ul,
    .m-markdown-small ol {
        margin: 0 0 0.8333333333rem;
        line-height: 1.4;
    }

    .m-markdown p:not(:first-child),
    .m-markdown-small p:not(:first-child) {
        margin-block-start: unset;
    }

    .m-markdown ul,
    .m-markdown ol,
    .m-markdown-small ul,
    .m-markdown-small ol {
        padding-inline-start: 20px !important;
        padding-left: 0;
    }

    .m-markdown li:not(:last-of-type):not(.nhsd-m-table__mobile-list li),
    .m-markdown-small li:not(:last-of-type):not(.nhsd-m-table__mobile-list li) {
        margin: 0 0 0.1333rem;
    }

    .m-markdown li {
       padding-left: 0.5rem;
       line-height: 1.6;
    }

    .m-markdown-small li {
       padding-left: 0.5rem;
       line-height: 1.4;
    }

    .tree {
        line-height: 1.4;
    }

    .param-name,
    .param-type {
        line-height: 1.4;
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

    .param-type {
        font-weight: 600;
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

    /* Buttons */
    .m-btn,
    .toolbar-btn {
        display: inline-flex;
        position: relative;
        align-items: center;
        justify-content: center;
        max-width: 15.55rem;
        padding: .5555555556rem 1.1111111111rem !important;
        border: 2px solid #005bbb;
        border-radius: 1.22rem;
        outline: 0;
        box-shadow: 0 0 0 .167rem transparent;
        font-size: .78rem;
        font-weight: 600;
        line-height: 1.11;
        text-align: center;
        text-decoration: none;
        cursor: pointer;
        -webkit-appearance: none;
        appearance: none;
        -webkit-user-select: none;
        user-select: none;
        transition-property: background-color,box-shadow,border-color;
        transition-duration: .15s;
    }

    .m-btn {
        color: #005bbb;
        background: #ffffff;
    }

    .m-btn:after,
    .toolbar-btn:after {
        content: "";
        display: block;
        position: absolute;
        top: 50%;
        left: 50%;
        width: 2.5rem;
        height: 2.5rem;
        transform: translateX(-50%) translateY(-50%);
        border-radius: 100%;
    }

    .m-btn:focus,
    .m-btn:hover,
    .toolbar-btn:focus,
    .toolbar-btn:hover {
        box-shadow: 0 0 0 0.167rem #fae100;
    }

    .m-btn:focus,
    .m-btn:hover {
        border-color: #231f20;
        color: #005bbb;
        background: #ffffff;
    }

    .m-btn:active,
    .toolbar-btn:active {
        transition-property: none;
        box-shadow: 0 0 0 0.167rem transparent;
        transform: translateY(0.111rem);
    }

    .m-btn:active {
        border-color: #005bbb;
        background: #ffffff;
    }

    .m-btn.primary,
    .toolbar-btn {
        background-color: var(--primary-color);
        color: var(--primary-color-invert);
    }

    .m-btn.primary:focus,
    .m-btn.primary:hover,
    .toolbar-btn:focus,
    .toolbar-btn:hover {
        border-color: #003087;
        color: #ffffff;
        background:#003087;
    }

    .m-btn.primary:active,
    .toolbar-btn:active {
        border-color: #005bbb;
        color: #ffffff;
        background: #005bbb;
    }

    .m-btn.thin-border {
        border-width: 2px;
    }

    /* Schema */
    .tr {
        border-top: 1px solid #d5dade;
        padding: 0.555556rem;
    }

    .tree > .tr {
        border-top: unset;
        padding: unset;
    }

    .inside-bracket.object,
    .inside-bracket.array {
        border-left: 1px solid #d5dade;
    }

    /* prevent schema descriptions from cutting off */
    .key-descr {
        overflow-x: auto;
    }

    .key-descr::-webkit-scrollbar {
        width: 8px;
        height: 8px;
    }
    .key-descr::-webkit-scrollbar-track {
        background:transparent;
    }
    .key-descr::-webkit-scrollbar-thumb {
        background-color: var(--border-color);
    }

    .schema-multiline-toggle {
        display: none;
    }

    /* Slider - to finish */
    input[type="checkbox"]:checked {
        background-color: var(--blue);
        border-color: var(--blue);
    }

    input[type="checkbox"]:checked::after {
        border: 1px solid var(--blue);
    }

    /* Code - to finish */
    .m-markdown-small code, .m-markdown code {
        word-break: break-word;
    }

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
      padding-bottom: 2px;
      border: 0;
      border-bottom: 1px solid var(--primary-colour, #005bbb);
      color: var(--primary-colour, #005bbb);
      line-height: 1.6;
      transition-property: background-color,color;
      transition-duration: .15s;
      display: inline !important;
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
        max-width: unset;
    }

    .m-markdown table,
    .m-markdown tbody {
        line-height: 1.6;
    }

    .m-markdown-small table,
    .m-markdown-small tbody {
        line-height: 1.4;
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

    .m-table td,
    .m-table th {
        padding: .5555555556rem;
    }

    .m-markdown td,
    .m-markdown th {
        line-height: 1.6;
    }

    .m-markdown-small td,
    .m-markdown-small th,
    .m-table td,
    .m-table th {
        line-height: 1.4;
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

    .nhsd-m-table__mobile-list ul {
        padding-inline-start: unset !important;
    }

    .nhsd-m-table__mobile-list {
        margin-top: 15px;
    }
`;
