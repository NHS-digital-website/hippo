(function() {
    "use strict";
Ext.namespace('Relevance');

Relevance.PageViewsCollectorPlugin =
                        Ext.extend(Hippo.Targeting.CollectorPlugin, {

    constructor: function(config) {
        var editor;

        if (Ext.isEmpty(config.pageViews)) {
            editor = {
                message: config.resources['page-visits-empty'],
                xtype: 'Relevance.TargetingDataMessage'
            };
        } else {
            editor = {
                collector: config.collector,
                pageViews: config.pageViews,
                resources: config.resources,
                xtype: 'Relevance.PageViewsTargetingDataEditor'
            };
        }

        Relevance.PageViewsCollectorPlugin.superclass.constructor
                                       .call(this, Ext.apply(config, {
            editor: editor,
            renderer: this.renderGroups
        }));
    },

    renderGroups: function(value) {
        var pageViews = value ? value.pageViews: [];
        if (Ext.isEmpty(pageViews)) {
            return this.resources['no-page-vists'];
        }
        return pageViews.join(", ");
    }

});

Relevance.PageViewsTargetingDataEditor =
                Ext.extend(Hippo.Targeting.TargetingDataCheckboxGroup, {

    constructor: function(config) {
        var checkboxes = [];
        Ext.each(config.pageViews, function(pageView) {
            checkboxes.push({
                boxLabel: pageView.label,
                name: pageView.urls.join(', ')
            });
        });
        Relevance.PageViewsTargetingDataEditor.superclass
                            .constructor.call(this, Ext.apply(config, {
            columns: 2,
            items: checkboxes,
            vertical: true
        }));
    },

    convertDataToCheckedArray: function(data) {
        var checkedArray = this.createBooleanArray(this.checkboxNames
                                                               .length);

        if (!Ext.isEmpty(data.pageViews)) {

            Ext.each(data.pageViews, function(dataItem) {
                var index = this.checkboxNames.indexOf(dataItem);
                if (index >= 0) {
                    checkedArray[index] = true;
                }
            }, this);
        }

        return checkedArray;
    },

    convertCheckedBoxesToData: function(checkedBoxes) {
        var checkedIds = Ext.pluck(checkedBoxes, 'name');
        return {
            collectorId: this.collector,
            pageViews: checkedIds
        };
    }

});
Ext.reg('Relevance.PageViewsTargetingDataEditor',
         Relevance.PageViewsTargetingDataEditor);
}());
