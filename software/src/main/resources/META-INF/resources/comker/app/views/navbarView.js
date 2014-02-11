define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
], function($, _, Backbone, i18n, Coke){

    var NavbarView = Backbone.View.extend({
        el: $("#navbar"),

        render: function() {
            Coke.log.debug("run NavbarView.render()");
            var that = this;
            Coke.TemplateManager.get('navbar', function(tmpl) {
                var $tmpl = tmpl({});
                that.$el.html($tmpl);
                that.$el.i18n();
            });
        }
    });

    return NavbarView;
});