define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/views/collectionLayoutView',
    'app/views/roleCollectionView',
], function($, _, Backbone, i18n, Coke, CollectionLayoutView, RoleCollectionView) {

    var RoleRouter = Backbone.Router.extend({
        initialize: function(params) {
            params = params || {};
        },
        
        routes: {
            "role/list" : "onRouteListRole",

            "role/filter(/:query)" : "onRouteFilterRole",

            "role/create" : "onRouteCreateRole",

            "role/detail/:id" : "onRouteDetailRole",

            "role/update/:id" : "onRouteUpdateRole",

            "role/delete/:id" : "onRouteDeleteRole"
        },

        onRouteListRole: function() {
            var that = this;
            Coke.log.debug("run RoleRouter.onRouteListRole()");
            that.renderRolePage(function() {});
        },

        onRouteFilterRole: function(query, page) {
            var page_number = page || 1;
            var that = this;
            Coke.log.debug("run RoleRouter.onRouteFilterRole()");
            that.renderRolePage(function() {
                that.collectionView.showFilterRole(query, page);
            });
        },

        onRouteCreateRole: function() {
            var that = this;
            Coke.log.debug("run RoleRouter.onRouteCreateRole()");
            that.renderRolePage(function() {
                that.collectionView.showCreateRole();
            });
        },

        onRouteDetailRole: function(id) {
            var that = this;
            Coke.log.debug("run RoleRouter.onRouteDetailRole()");
            that.renderRolePage(function() {
                that.collectionView.collectionView[id].showDetailRole();
            });
        },

        onRouteUpdateRole: function(id) {
            var that = this;
            Coke.log.debug("run RoleRouter.onRouteUpdateRole()");
            that.renderRolePage(function() {
                that.collectionView.collectionView[id].showUpdateRole();
            });
        },

        onRouteDeleteRole: function(id) {
            var that = this;
            Coke.log.debug("run RoleRouter.onRouteDeleteRole()");
            that.renderRolePage(function() {
                that.collectionView.collectionView[id].showDeleteRole();
            });
        },

        renderRolePage: function(callback) {
            var that = this;
            i18n.init(_.extend(Coke.i18nConfig, {
                ns: {
                    namespaces: ['common', 'navbar', 'role'],
                    defaultNs: 'role'
                }
            }), function(t) {
                that.layoutView = new CollectionLayoutView({
                    title: t('role.collection.title', {defaultValue: 'Roles'})
                });
                that.layoutView.render({afterTrigger: function() {
                    that.collectionView = new RoleCollectionView({router: that});
                    that.collectionView.startup({afterTrigger: function() {
                        if (_.isFunction(callback)) callback();
                    }});
                }});
                $('#navigation').i18n();
            });
        }
    });

    return RoleRouter;
});
