define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/views/navigationView',
    'app/views/collectionLayoutView',
    'app/views/userCollectionView',
], function($, _, Backbone, i18n, Coke, NavigationView, CollectionLayoutView, UserCollectionView) {

    var UserRouter = Backbone.Router.extend({
        initialize: function(params) {
            params = params || {};
        },
        
        routes: {
            "user/list" : "onRouteListUser",

            "user/filter(/:query)" : "onRouteFilterUser",

            "user/filter(/:query/p:page)" : "onRouteFilterUser",

            "user/create" : "onRouteCreateUser",

            "user/detail/:id" : "onRouteDetailUser",

            "user/update/:id" : "onRouteUpdateUser",

            "user/delete/:id" : "onRouteDeleteUser"
        },

        onRouteListUser: function() {
            var that = this;
            Coke.log.debug("run UserRouter.onRouteListUser()");
            that.renderUserPage(function() {});
        },

        onRouteFilterUser: function(query, page) {
            var page_number = page || 1;
            var that = this;
            Coke.log.debug("run UserRouter.onRouteFilterUser()");
            that.renderUserPage(function() {
                that.collectionView.showFilterUser(query, page);
            });
        },

        onRouteCreateUser: function() {
            var that = this;
            Coke.log.debug("run UserRouter.onRouteCreateUser()");
            that.renderUserPage(function() {
                that.collectionView.showCreateUser();
            });
        },

        onRouteDetailUser: function(id) {
            var that = this;
            Coke.log.debug("run UserRouter.onRouteDetailUser()");
            that.renderUserPage(function() {
                that.collectionView.collectionView[id].showDetailUser();
            });
        },

        onRouteUpdateUser: function(id) {
            var that = this;
            Coke.log.debug("run UserRouter.onRouteUpdateUser()");
            that.renderUserPage(function() {
                that.collectionView.collectionView[id].showUpdateUser();
            });
        },

        onRouteDeleteUser: function(id) {
            var that = this;
            Coke.log.debug("run UserRouter.onRouteDeleteUser()");
            that.renderUserPage(function() {
                that.collectionView.collectionView[id].showDeleteUser();
            });
        },

        renderUserPage: function(callback) {
            var that = this;
            Coke.startup({
                i18nNamespaces: ['user'],
                afterTrigger: function(t, session) {
                    that.layoutView = new CollectionLayoutView({
                        title: t('user:user.collection.title', {defaultValue: 'Users'})
                    });
                    that.layoutView.render({afterTrigger: function() {
                        that.collectionView = new UserCollectionView({router: that});
                        that.collectionView.startup({afterTrigger: function() {
                            if (_.isFunction(callback)) callback();
                        }});
                    }});
                    that.navigationView = new NavigationView();
                    that.navigationView.render();
                }
            });
        }
    });

    return UserRouter;
});
