(function () {
    "use strict";

    CKEDITOR.stylesSet.add('nhsdStyle', [
        {
            element: 'p',
            name: 'Normal',
        },{
            element: 'h2',
            name: 'Heading 2',
        },{
            element: 'h3',
            name: 'Heading 3',
        },{
            element: 'h4',
            name: 'Heading 4',
        },{
            element: 'code',
            name: 'In line code',
            attributes: {
                class: 'codeinline'
            }
        }
    ]);
}());
