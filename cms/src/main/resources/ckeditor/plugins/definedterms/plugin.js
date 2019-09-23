
CKEDITOR.plugins.add( 'definedterms', {

	icons: 'definedterms',

	init: function( editor ) {

    editor.addContentsCss( this.path + 'styles/definedterms.css' );

		editor.addCommand( 'definedterms', new CKEDITOR.dialogCommand( 'definedtermsDialog', {

			allowedContent: 'span[class]{*}',
			requiredContent: 'span[class]',

			contentForms: [
				'term',
			]
		} ) );


		editor.ui.addButton( 'DefinedTerms', {
			label: 'The term',
			command: 'definedterms',
			toolbar: 'insert'
		});

		if ( editor.contextMenu ) {

			editor.addMenuGroup( 'definedtermsGroup' );
			editor.addMenuItem( 'definedtermsItem', {
				label: 'The term',
				icon: this.path + 'icons/definedterms.png',
				command: 'definedterms',
				group: 'definedtermsGroup'
			});

			editor.contextMenu.addListener( function( element ) {
					return { definedtermsItem: CKEDITOR.TRISTATE_OFF };
			});
		}


		CKEDITOR.dialog.add( 'definedtermsDialog', this.path + 'dialogs/dialog.js' );
	}
});
