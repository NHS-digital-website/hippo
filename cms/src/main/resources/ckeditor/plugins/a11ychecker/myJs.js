function () {
    alert('my js!');
    "use strict";

    CKEDITOR.stylesSet.add('mystyle', [
        {
            element: 'h1',
            name: 'Blue Fancy Title',
            styles: {
                color: 'blue'
            },
            attributes: {
                class: 'fancy'
            }
        }
    ]);
}());
