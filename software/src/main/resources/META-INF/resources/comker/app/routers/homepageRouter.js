define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/views/homepageView',
], function($, _, Backbone, i18n, Coke, HomepageView) {
    var HomepageRouter = Backbone.Router.extend({
        initialize: function(options) {
            options = options || {};
        },
        
        routes: {
            "" : "onRouteHomepage",
            "contact" : "onRouteContact"
        },

        onRouteHomepage: function() {
            Coke.log.debug("run HomepageRouter.onRouteHomepage()");
            var homepageView = new HomepageView();
            homepageView.render();
        },

        onRouteContact: function() {
            Coke.log.debug("run HomepageRouter.onRouteContact()");
        }
    });

    return HomepageRouter;
});
