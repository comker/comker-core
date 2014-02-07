define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
], function($, _, Backbone, i18n, Coke){

    var NavigationView = Backbone.View.extend({
        el: $("#navigation"),

        render: function() {
            Coke.log.debug("run NavigationView.render()");
            var that = this;
            Coke.TemplateManager.get('navigation', function(tmpl) {
                var $tmpl = tmpl({});
                that.$el.html($tmpl);
                that.$el.i18n();
            });
        }
    });

    return NavigationView;
});