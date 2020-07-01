const ROW_CLASS = 'summing-row';

// Set the callback function
var tableSumRow = {
    exec: function(editor) {

        let selectedElement = editor.getSelection().getStartElement();
        if (selectedElement && selectedElement.getName() !== 'tr') {
            selectedElement = selectedElement.getAscendant('tr');
        }

        if (selectedElement && selectedElement.getName() === 'tr') {
            selectedElement.$.classList.toggle(ROW_CLASS)
        }
    }
}

CKEDITOR.plugins.add( 'table-sum-row', {
    icons: 'table-sum-row',
    init: function(editor) {
        editor.addCommand('tableSumRow', tableSumRow);
        editor.ui.addButton("tableSumRow", {
            label: 'Table Sum Row',
            icon: this.path + 'icons/sum.png',
            command: 'tableSumRow'
        });

        editor.on( 'selectionChange', function( evt ) {
            var sumRowButton = this.getCommand( 'tableSumRow' );
            if (
                evt.data.path.lastElement.is( 'tr' )
                || evt.data.path.lastElement.getAscendant('tr')
            ) {
                sumRowButton.enable();
            } else {
                sumRowButton.disable();
            }
        });
    }
});
