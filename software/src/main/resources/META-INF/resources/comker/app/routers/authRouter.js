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

            i18n.init(_.extend(Coke.i18nConfig, {
                ns: {
                    namespaces: ['common', 'navigation', 'auth'],
                    defaultNs: 'auth'
                }
            }), function(t) {
                self.loginView = new AuthLoginView({router: self});
                self.loginView.render();
                $('#navigation').i18n();
            });
        }
    });

    return AuthRouter;
});
