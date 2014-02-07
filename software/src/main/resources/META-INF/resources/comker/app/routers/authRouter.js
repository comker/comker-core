define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/views/authLoginView',
], function($, _, Backbone, i18n, Coke, AuthLoginView) {

    var AuthRouter = Backbone.Router.extend({
        initialize: function(params) {
            params = params || {};
        },
        
        routes: {
            "auth/login" : "onRouteLogin"
        },

        onRouteLogin: function() {
            var self = this;
            Coke.log.debug("run AuthRouter.onRouteLogin()");

            Coke.startup({
                i18nNamespaces: ['auth'],
                afterTrigger: function(t, session) {
                    self.loginView = new AuthLoginView({router: self});
                    self.loginView.render();
                }
            });
        }
    });

    return AuthRouter;
});
