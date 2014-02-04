define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
], function($, _, Backbone, Coke){

    var CollectionLayoutView = Backbone.View.extend({
        el: $("#page"),

        initialize: function(params) {
            params = params || {};

            this.title = params.title;
            if (Coke.isDefined(this.title)) {
                Coke.log.debug("run CollectionLayoutView.initialize() - title:" + this.title);
            } else {
                Coke.log.debug("run CollectionLayoutView.initialize() - empty title");
            }
        },

        render: function(params) {
            params = params || {};

            Coke.log.debug("run CollectionLayoutView.render()");
            var that = this;
            Coke.TemplateManager.get('collection-layout', function(tmpl) {
                var $tmpl = tmpl({title: that.title});
                that.$el.html($tmpl);
                if (params.afterTrigger != undefined) {
                    params.afterTrigger();
                }
            });
        }
    });

    return CollectionLayoutView;
});