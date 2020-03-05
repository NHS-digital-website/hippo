CKEDITOR.on('instanceReady', function(e) {
    // Remove the 'Merge' option (rowspan & colspan) from the context menu
    e.editor.removeMenuItem("tablecell_merge");
    e.editor.removeMenuItem("tablecell_merge_down");
    e.editor.removeMenuItem("tablecell_merge_right");

    // Prevents cut and paste of images so that image paths can be managed via the CMS
    e.editor.on('paste', function (ev) {
        ev.data.dataValue = ev.data.dataValue.replace(/<img[^>]*?>/gi, '');
    });
});

CKEDITOR.on('dialogDefinition', function (e) {
    if (e.data.name == "cellProperties") {
        e.data.definition.minHeight = 50;
        e.data.definition.getContents("info").elements[0] = {
            "type": "hbox",
            "widths": ["40%", "5%", "40%"],
            "children": [{
                "type": "vbox",
                "padding": 0,
                "children": [{
                    "type": "select",
                    "id": "hAlign",
                    "label": "Horizontal Alignment",
                    "default": "",
                    "items": [
                        ["<not set>", ""],
                        ["Left", "left"],
                        ["Center", "center"],
                        ["Right", "right"],
                        ["Justify", "justify"]
                    ]
                }]
            }, {
                "isSpacer": true,
                "type": "html",
                "html": "&nbsp;"
            }, {
                "type": "vbox",
                "padding": 0,
                "children": [{
                    "type": "select",
                    "id": "vAlign",
                    "label": "Vertical Alignment",
                    "default": "",
                    "items": [
                        ["<not set>", ""],
                        ["Top", "top"],
                        ["Middle", "middle"],
                        ["Bottom", "bottom"],
                        ["Baseline", "baseline"]
                    ]
                }]
            }]
        };
    }
});
