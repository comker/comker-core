define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
], function($, _, Backbone, i18n, Coke){

    var HomeView = Backbone.View.extend({
        el: $("#page"),

        render: function() {
            Coke.log.debug("run HomeView.render()");
            var that = this;
            Coke.TemplateManager.get('home', function(tmpl) {
                var $tmpl = tmpl({});
                that.$el.html($tmpl);
                that.$el.i18n();
            });
        }
    });

    return HomeView;
});