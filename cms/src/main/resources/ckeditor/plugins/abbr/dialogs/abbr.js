/*
 * abbr/dialogs/abbr.js
 */
CKEDITOR.dialog.add( 'abbrDialog', function( editor ) {
    return {
        title: 'Abbreviation Properties',
        minWidth: 400,
        minHeight: 200,

        contents: [
            {
                id: 'tab-basic',
                label: 'Basic Settings',
                elements: [
                    {
                        type: 'text',
                        id: 'abbr',
                        label: 'Abbreviation',
                        validate: CKEDITOR.dialog.validate.notEmpty( "Abbreviation field cannot be empty." ),

                        setup: function( dialog ) {
                            var element = dialog.element;
                            this.setValue( element.getText() );
                        },

                        commit: function( dialog ) {
                            var element = dialog.element;
                            element.setText( this.getValue() );
                        }
                    },
                    {
                        type: 'text',
                        id: 'title',
                        label: 'Title',
                        requiredContent: 'abbr[title]',
                        validate: CKEDITOR.dialog.validate.notEmpty( "Title field cannot be empty." ),

                        setup: function( dialog ) {
                            var element = dialog.element;
                            this.setValue( element.getAttribute( "title" ) );
                        },

                        commit: function( dialog ) {
                            var element = dialog.element;
                            element.setAttribute( "title", this.getValue() );
                        }
                    },
                    {
                        type: 'checkbox',
                        id: 'readletters',
                        label: 'Read letter-by-letter? (e.g. O-D-S)',
                        requiredContent: 'abbr[class]',
                        'default': 'checked',
                        onClick: function() {
                          
                        },
                        setup: function( dialog ) {
                            var element = dialog.element;
                            this.setValue( element.getAttribute( "class" ) );
                        },
                        commit: function( dialog ) {
                            var element = dialog.element;
                            if (this.getValue() == true) {
                              element.setAttribute( 'class', 'speak-letter-by-letter' );
                            } else {
                              element.setAttribute( 'class', 'speak-normal' );
                            }
                        }
                    }
                ]
            },

            {
                id: 'tab-adv',
                label: 'Advanced Settings',
                requiredContent: 'abbr[id]',
                elements: [
                    {
                        type: 'text',
                        id: 'id',
                        label: 'Id',

                        setup: function( dialog ) {
                            var element = dialog.element;
                            this.setValue( element.getAttribute( "id" ) );
                        },

                        commit: function ( dialog ) {
                            var element = dialog.element;
                            var id = this.getValue();
                            if ( id )
                                element.setAttribute( 'id', id );
                            else if ( !this.insertMode )
                                element.removeAttribute( 'id' );
                        }
                    }
                ]
            }
        ],

        onShow: function() {
            var selection = editor.getSelection();
            var element = selection.getStartElement();

            if ( element ){
                element = element.getAscendant( 'abbr', true );
            }

            if ( !element || element.getName() != 'abbr' ) {
                element = editor.document.createElement( 'abbr' );

                //preassign selected text into 'abbr' text value
                this.setValueOf('tab-basic', 'abbr', selection.getSelectedText() || '' );

                this.insertMode = true;
            }
            else {
                this.insertMode = false;
            }

            this.element = element;
            if ( !this.insertMode )
                this.setupContent( this );
        },

        onOk: function() {
            this.commitContent( this );

            if ( this.insertMode )
                editor.insertElement( this.element );
        }
    };
});
