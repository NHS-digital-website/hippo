/*
 * abbr/plugin.js
 */

CKEDITOR.plugins.add( 'abbr', {
    icons: 'abbr',
    init: function( editor ) {

        editor.addCommand( 'abbr', new CKEDITOR.dialogCommand( 'abbrDialog', {
          allowedContent: 'abbr[title, id, class]',
          requiredContent: 'abbr',
          contentForms: [
            'abbr',
            'acronym'
          ]
        }) );

        editor.ui.addButton( 'Abbr', {
            label: 'Insert Abbreviation',
            command: 'abbr',
            toolbar: 'insert'
        });

        if ( editor.contextMenu ) {
            editor.addMenuGroup( 'abbrGroup' );
            editor.addMenuItem( 'abbrItem', {
                label: 'Edit Abbreviation',
                icon: this.path + 'icons/abbr.png',
                command: 'abbr',
                group: 'abbrGroup'
            });

            editor.contextMenu.addListener( function( element ) {
                if ( element.getAscendant( 'abbr', true ) ) {
                    return { abbrItem: CKEDITOR.TRISTATE_OFF };
                }
            });
        }

        CKEDITOR.dialog.add( 'abbrDialog', this.path + 'dialogs/abbr.js' );
    }
});
