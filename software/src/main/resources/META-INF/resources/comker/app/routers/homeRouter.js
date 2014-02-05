define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/views/homeView',
], function($, _, Backbone, i18n, Coke, HomeView) {
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
            i18n.init(_.extend(Coke.i18nConfig, {
                ns: {
                    namespaces: ['common', 'navbar'],
                    defaultNs: 'common'
                }
            }), function(t) {
                that.homeView = new HomeView();
                that.homeView.render();
                $('#navigation').i18n();
            });
        }
    });

    return HomeRouter;
});
