define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/views/navbarView',
    'app/views/homeView',
], function($, _, Backbone, i18n, Coke, NavbarView, HomeView) {
    var HomeRouter = Backbone.Router.extend({
        initialize: function(options) {
            options = options || {};
        },
        
        routes: {
            "" : "onRouteHome",
            "contact" : "onRouteContact"
        },

        onRouteHome: function() {
            Coke.log.debug("run HomeRouter.onRouteHome()");
            this.renderHomePage();
        },

        onRouteContact: function() {
            Coke.log.debug("run HomeRouter.onRouteContact()");
        },

        renderHomePage: function(callback) {
            var that = this;
            Coke.startup({
                i18nNamespaces: ['home'],
                afterTrigger: function(t, session) {
                    that.homeView = new HomeView();
                    that.homeView.render();
                    that.navbarView = new NavbarView();
                    that.navbarView.render();
                }
            });
        }
    });

    return HomeRouter;
});
