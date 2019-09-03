function slugify(string) {
  const a = 'àáäâãåăæąçćčđďèéěėëêęğǵḧìíïîįłḿǹńňñòóöôœøṕŕřßşśšșťțùúüûǘůűūųẃẍÿýźžż·/_,:;'
  const b = 'aaaaaaaaacccddeeeeeeegghiiiiilmnnnnooooooprrsssssttuuuuuuuuuwxyyzzz------'
  const p = new RegExp(a.split('').join('|'), 'g')

  return string.toString().toLowerCase()
    .replace(/\s+/g, '-') // Replace spaces with -
    .replace(p, c => b.charAt(a.indexOf(c))) // Replace special characters
    .replace(/&/g, '-and-') // Replace & with 'and'
    .replace(/[^\w\-]+/g, '') // Remove all non-word characters
    .replace(/\-\-+/g, '-') // Replace multiple - with single -
    .replace(/^-+/, '') // Trim - from start of text
    .replace(/-+$/, '') // Trim - from end of text
}

CKEDITOR.dialog.add('definedtermsDialog', function(editor) {
    return {

        title: 'Defined Terms',
        minWidth: 300,
        minHeight: 150,

        contents: [

            {
                id: 'tab-id',
                label: 'Add new term',

                elements: [ {
                    type: 'select',
                    id: 'term',
                    label: 'Choose a term',
                    items: [['-', '-']],

                    setup: function(element) {
                          element.setAttribute("class", 'definedterm-' + slugify(this.getValue()));
                          element.setHtml(this.getValue());
                          this.setValue(element.getHtml());
                        },

                    commit: function(element) {
                      element.setAttribute("class", 'definedterm-' + slugify(this.getValue()));
                      element.setHtml(this.getValue());
                    }
                }, ]
            }
        ],


        onShow: function() {

            var definedterms = '';
            var terms = [ 'must', 'should', 'could', 'will not', 'experimental', 'discovery', 'alpha', 'beta', 'live', "release candidate" ];

            var selectedText = editor.getSelection().getSelectedText().toString();
            for (var i = 0; i < terms.length; i++) {
                var selected = '';
                if (selectedText.trim().toLowerCase() == terms[i].toLowerCase()) {
                  selected = " selected";
                }
                definedterms += "<option value=\"" + terms[i] + "\""+selected+">" + terms[i] + "</option>";
            }
            var dropdownElement = CKEDITOR.dialog.getCurrent().getContentElement('tab-id', 'term').getInputElement();
            dropdownElement.setHtml(definedterms);


            var element = editor.getSelection().getStartElement();

            if (element) {
                element = element.getAscendant('span', true);
            }

            if (!element || element.getName() != 'span') {
                element = editor.document.createElement('span');
                this.insertMode = true;
            } else
                this.insertMode = false;

            this.element = element;
            if (!this.insertMode)
                this.setupContent(this.element);
        },

        onOk: function() {

            var dialog = this;
            var definedterms = this.element;

            this.commitContent(definedterms);

            if (this.insertMode)
                editor.insertElement(definedterms);
        }
    };
});
