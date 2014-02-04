define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
], function($, _, Backbone, i18n, Coke){

    var HomepageView = Backbone.View.extend({
        el: $("#page"),

        render: function() {
            Coke.log.debug("run HomepageView.render()");
            var that = this;
            Coke.TemplateManager.get('homepage', function(tmpl) {
                var $tmpl = tmpl({});
                that.$el.html($tmpl);
                that.$el.i18n();
            });
        }
    });

    return HomepageView;
});