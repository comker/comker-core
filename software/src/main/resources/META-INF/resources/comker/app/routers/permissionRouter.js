define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/views/collectionLayoutView',
    'app/views/permissionCollectionView',
], function($, _, Backbone, i18n, Coke, CollectionLayoutView, PermissionCollectionView) {

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
            i18n.init(_.extend(Coke.i18nConfig, {
                ns: {
                    namespaces: ['common', 'navbar', 'permission'],
                    defaultNs: 'permission'
                }
            }), function(t) {
                that.layoutView = new CollectionLayoutView({
                    title: t('permission.collection.title', {defaultValue: 'Permissions'})
                });
                that.layoutView.render({afterTrigger: function() {
                    that.collectionView = new PermissionCollectionView({router: that});
                    that.collectionView.startup({afterTrigger: function() {
                        if (_.isFunction(callback)) callback();
                    }});
                }});
                $('#navigation').i18n();
            });
        }
    });

    return PermissionRouter;
});