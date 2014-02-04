define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
    'app/views/homepageView',
], function($, _, Backbone, Coke, HomepageView) {
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
