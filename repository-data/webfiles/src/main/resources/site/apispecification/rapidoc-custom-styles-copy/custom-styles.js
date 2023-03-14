import { css } from 'lit';

/*
This file is reserved for any custom css that developers want to add to
customize their theme. Simply add your css to this file and yarn build.
*/

export default css`
    /* CSS var overrides */ 
    :host {
        --font-size-regular: 1rem;
        --font-size-small: .888rem;

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
    }
    
    @media (max-width:63.99em) {
        :host {
            --font-size-regular: .888rem;
            --font-size-small: .778rem;
        }
    }
    
    /* General typography overrides */
    .m-markdown {
        line-height: 1.6;
    }
    
    .m-markdown-small {
        line-height: 1.4375;;
    }
    
    @media (max-width:63.99em) {
        .m-markdown {
            line-height: 1.4;
        }
        
        .m-markdown-small {
            line-height: 1.389;
        }
    }

    /* NHSD links - inactive links are now weird when try this API deactivated */ 
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
`;
