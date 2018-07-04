Ext.namespace('Example');

Example.DocumentTypeCharacteristicPlugin =
                             Ext.extend(Hippo.Targeting.CharacteristicPlugin, {

    constructor: function(config) {
        this.documentTypeMap = new Hippo.Targeting.Map(config.documentTypes,
                                                      'type', 'name');
        Ext.apply(config, {
            visitorCharacteristic: {
                documentTypeMap: this.documentTypeMap,
                xtype: 'Example.DocumentTypeCharacteristic'
            },
            editor: {
                documentTypes: config.documentTypes,
                resources: config.resources,
                xtype: 'Example.DocumentTypeTargetGroupEditor'
            },
            renderer: this.renderDocumentTypeNames,
            scope: this
        });
        Example.DocumentTypeCharacteristicPlugin.superclass.constructor.call(
                                                                 this, config);
    },

    renderDocumentTypeNames: function(properties) {
        var result = [];

        Ext.each(properties, function(property) {
            var type = property.name,
                name = this.documentTypeMap.getValue(type);
            if (!Ext.isEmpty(name)) {
                result.push(name);
            }
        }, this);

        return result.join(', ');
    }

});

Example.DocumentTypeCharacteristic =
                   Ext.extend(Hippo.Targeting.TermsFrequencyCharacteristic, {

    constructor: function(config) {
        Example.DocumentTypeCharacteristic.superclass.constructor.call(
                                                                this, config);
        this.documentTypeMap = config.documentTypeMap;
    },

    getTermName: function(term) {
        return this.documentTypeMap.getValue(term);
    }

});
Ext.reg('Example.DocumentTypeCharacteristic',
         Example.DocumentTypeCharacteristic);

Example.DocumentTypeTargetGroupEditor =
                   Ext.extend(Hippo.Targeting.TargetGroupCheckboxGroup, {

    constructor: function(config) {
        var checkboxes = [];

        Ext.each(config.documentTypes, function(documentType) {
            checkboxes.push({
                boxLabel: documentType.name,
                name: documentType.type
            });
        });

        Example.DocumentTypeTargetGroupEditor.superclass.constructor.call(
                                                      this, Ext.apply(config, {
            allowBlank: config.allowBlank || false,
            blankText:
                 config.resources['error-select-at-least-one-document-type'],
            columns: config.columns || 2,
            items: checkboxes,
            vertical: true
        }));
    }

});
Ext.reg('Example.DocumentTypeTargetGroupEditor',
         Example.DocumentTypeTargetGroupEditor);
