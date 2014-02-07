define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/views/navigationView',
    'app/views/collectionLayoutView',
    'app/views/permissionCollectionView',
], function($, _, Backbone, i18n, Coke, NavigationView, CollectionLayoutView, PermissionCollectionView) {

    var PermissionRouter = Backbone.Router.extend({
        initialize: function(params) {
            params = params || {};
        },
        
        routes: {
            "permission/list" : "onRouteListPermission",

            "permission/filter(/:query)" : "onRouteFilterPermission",

            "permission/detail/:id" : "onRouteDetailPermission"
        },

        onRouteListPermission: function() {
            var that = this;
            Coke.log.debug("run PermissionRouter.onRouteListPermission()");
            that.renderPermissionPage(function() {});
        },

        onRouteFilterPermission: function(query, page) {
            var page_number = page || 1;
            var that = this;
            Coke.log.debug("run PermissionRouter.onRouteFilterPermission()");
            that.renderPermissionPage(function() {
                that.collectionView.showFilterPermission(query, page);
            });
        },

        onRouteDetailPermission: function(id) {
            var that = this;
            Coke.log.debug("run PermissionRouter.onRouteDetailPermission()");
            that.renderPermissionPage(function() {
                that.collectionView.collectionView[id].showDetailPermission();
            });
        },

        renderPermissionPage: function(callback) {
            var that = this;
            Coke.startup({
                i18nNamespaces: ['permission'],
                afterTrigger: function(t, session) {
                    that.layoutView = new CollectionLayoutView({
                        title: t('permission:permission.collection.title', {defaultValue: 'Permissions'})
                    });
                    that.layoutView.render({afterTrigger: function() {
                        that.collectionView = new PermissionCollectionView({router: that});
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

    return PermissionRouter;
});
