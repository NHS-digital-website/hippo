/**
 * Add a Document Type Characteristic UI Plugin
 * https://documentation.bloomreach.com/trails/relevance-trail/3-add-a-document-type-characteristic.html
 */

Ext.namespace('Relevance');

Relevance.DocumentTypeCharacteristicPlugin =
                             Ext.extend(Hippo.Targeting.CharacteristicPlugin, {

    constructor: function(config) {
        this.documentTypeMap = new Hippo.Targeting.Map(config.documentTypes,
                                                      'type', 'name');
        Ext.apply(config, {
            visitorCharacteristic: {
                documentTypeMap: this.documentTypeMap,
                xtype: 'Relevance.DocumentTypeCharacteristic'
            },
            editor: {
                documentTypes: config.documentTypes,
                resources: config.resources,
                xtype: 'Relevance.DocumentTypeTargetGroupEditor'
            },
            renderer: this.renderDocumentTypeNames,
            scope: this
        });
        Relevance.DocumentTypeCharacteristicPlugin.superclass.constructor.call(
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

Relevance.DocumentTypeCharacteristic =
                   Ext.extend(Hippo.Targeting.TermsFrequencyCharacteristic, {

    constructor: function(config) {
        Relevance.DocumentTypeCharacteristic.superclass.constructor.call(
                                                                this, config);
        this.documentTypeMap = config.documentTypeMap;
    },

    getTermName: function(term) {
        return this.documentTypeMap.getValue(term);
    }

});
Ext.reg('Relevance.DocumentTypeCharacteristic',
         Relevance.DocumentTypeCharacteristic);

Relevance.DocumentTypeTargetGroupEditor =
                   Ext.extend(Hippo.Targeting.TargetGroupCheckboxGroup, {

    constructor: function(config) {
        var checkboxes = [];

        Ext.each(config.documentTypes, function(documentType) {
            checkboxes.push({
                boxLabel: documentType.name,
                name: documentType.type
            });
        });

        Relevance.DocumentTypeTargetGroupEditor.superclass.constructor.call(
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
Ext.reg('Relevance.DocumentTypeTargetGroupEditor',
         Relevance.DocumentTypeTargetGroupEditor);
