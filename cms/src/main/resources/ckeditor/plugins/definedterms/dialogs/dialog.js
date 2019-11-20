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
                requiredContent: 'span[class]',
                allowedContent: 'span[class]{*}',

                elements: [ {
                    type: 'select',
                    id: 'term',
                    label: 'Choose a term',
                    items: [['-', '-']],

                    setup: function(element, terms) {
                          element.setAttribute("class", 'definedterm-' + slugify(this.getValue()));
                          if(this.getValue() in terms) {
                            colorrgb = terms[this.getValue()];
                            if (! colorrgb.startsWith("#")) { colorrgb = "#" + colorrgb; }
                            //validate whether color is in hex format #xxxxxx
                            //using regular expression
                            if (colorrgb.search(/#[0-9a-f]{6}$/i) != -1) {
                              element.setStyle("color", colorrgb);
                              element.setStyle("border-color", colorrgb);
                            } else {
                              element.removeStyle("color");
                              element.removeStyle("border-color");
                            }
                          }
                          element.setHtml(this.getValue());
                          this.setValue(element.getHtml());
                        },

                    commit: function(element, terms) {
                      this.setup(element, terms);
                    }
                }, ]
            }
        ],


        onShow: function() {

            var definedterms = '';
            var terms = { 'must': '', 'should': '', 'could': '', 'will not':'', 'experimental':'', 'discovery':'', 'beta':'', 'live':'', 'release candidate':'' };
            var selectedText = editor.getSelection().getSelectedText().toString();

            // assign 'this' to separate var in order to pass it to Ajax request
            var thisPass = this;

            var origin = location.origin; //for localhost-based origin
            if (origin.search(/cms-[dev,uat,tst,training]/) != -1) {
              origin = origin.replace("cms-", "");
            } else if (origin.search("cms.digital.nhs.uk") != -1 || origin.search("cms-nhs.hosting.onehippo.com") != -1){
              origin = "https://digital.nhs.uk";
            }

            $.ajax({
                type: "GET",
                url: origin+"/restapi/definedterms",
                dataType: "json",
                timeout: 3000,
                success: function(json)
                    {
                        json.items.forEach(function(items){ items.terms.forEach(function(jsonObject){
                            term = jsonObject.term;  
                            colorrgb = jsonObject.colorrgb;
                            if (! (term in terms) || (term in terms && colorrgb != '')) {
                              terms[term] = colorrgb.trim().toLowerCase();
                            }
                        })});

                        for (var key in terms) {
                            var selected = '';
                            if (selectedText.trim().toLowerCase() == key.toLowerCase()) {
                              selected = " selected";
                            }
                            definedterms += "<option value=\"" + key + "\""+selected+">" + key + "</option>";
                        }
                        var dropdownElement = CKEDITOR.dialog.getCurrent().getContentElement('tab-id', 'term').getInputElement();
                        dropdownElement.setHtml(definedterms);

                        var element = editor.getSelection().getStartElement();

                        if (element) {
                            element = element.getAscendant('span', true);
                        }

                        if (!element || element.getName() != 'span') {
                            element = editor.document.createElement('span');
                            thisPass.insertMode = true;
                        } else {
                            thisPass.insertMode = false;
                        }

                        thisPass.element = element;
                        thisPass.terms = terms;
                        if (!thisPass.insertMode)
                            thisPass.setupContent(thisPass.element, thisPass.terms);
                    }
            });

        },

        onOk: function() {

            var definedterms = this.element;
            var terms = this.terms;

            this.commitContent(definedterms, terms);

            if (this.insertMode)
                editor.insertElement(definedterms);
        }
    };
});
