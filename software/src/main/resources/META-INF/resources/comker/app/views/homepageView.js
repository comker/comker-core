define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
], function($, _, Backbone, Coke){

    var HomepageView = Backbone.View.extend({
        el: $("#page"),

        render: function() {
            Coke.log.debug("run HomepageView.render()");
            //$('.nav li').removeClass('active');
            //$('.nav li a[href="#"]').parent().addClass('active');
            var that = this;
            Coke.TemplateManager.get('homepage', function(tmpl) {
                var $tmpl = tmpl({});
                that.$el.html($tmpl);
            });
        }
    });

    return HomepageView;
});