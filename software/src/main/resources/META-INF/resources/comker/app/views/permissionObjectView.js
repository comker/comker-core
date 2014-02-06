define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/models/permissionObject',
], function($, _, Backbone, i18n, Coke, PermissionObject) {

    var PermissionObjectView = Backbone.View.extend({
        tagName: 'tr',

        initialize: function(params) {
            params = params || {};
            
            this.model.on('change', this.render, this);

            this.router = params.router;
            if (Coke.isDefined(this.router)) {
                Coke.log.debug("run PermissionObjectView.initialize() - router is defined");
            } else {
                Coke.log.debug("run PermissionObjectView.initialize() - router not found");
            }
        },

        render: function() {
            Coke.log.debug("run PermissionObjectView.render() for model#" + this.model.id);
            var that = this;
            Coke.TemplateManager.get('permission-list-item', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.html($tmpl);
                that.$el.i18n();
            });
            return this;
        }
    });

    return PermissionObjectView;
});

