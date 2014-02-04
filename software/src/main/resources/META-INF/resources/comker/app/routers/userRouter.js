define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
    'app/views/collectionLayoutView',
    'app/views/userCollectionView',
], function($, _, Backbone, Coke, CollectionLayoutView, UserCollectionView) {

    var UserRouter = Backbone.Router.extend({
        initialize: function(params) {
            params = params || {};
            this.layoutView = new CollectionLayoutView({title: 'Users'});
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
            that.layoutView.render({afterTrigger: function() {
                that.collectionView = new UserCollectionView({router: that});
                that.collectionView.startup();
            }});
        },

        onRouteFilterUser: function(query, page) {
            var page_number = page || 1;
            var that = this;
            Coke.log.debug("run UserRouter.onRouteFilterUser()");
            that.layoutView.render({afterTrigger: function() {
                that.collectionView = new UserCollectionView({router: that});
                that.collectionView.startup({afterTrigger: function() {
                    that.collectionView.showFilterUser(query, page);
                }});
            }});
        },

        onRouteCreateUser: function() {
            var that = this;
            Coke.log.debug("run UserRouter.onRouteCreateUser()");
            that.layoutView.render({afterTrigger: function() {
                that.collectionView = new UserCollectionView({router: that});
                that.collectionView.startup({afterTrigger: function() {
                    that.collectionView.showCreateUser();
                }});
            }});
        },

        onRouteDetailUser: function(id) {
            var that = this;
            Coke.log.debug("run UserRouter.onRouteDetailUser()");
            that.layoutView.render({afterTrigger: function() {
                that.collectionView = new UserCollectionView({router: that});
                that.collectionView.startup({afterTrigger: function() {
                    that.collectionView.collectionView[id].showDetailUser();
                }});
            }});
        },

        onRouteUpdateUser: function(id) {
            var that = this;
            Coke.log.debug("run UserRouter.onRouteUpdateUser()");
            that.layoutView.render({afterTrigger: function() {
                that.collectionView = new UserCollectionView({router: that});
                that.collectionView.startup({afterTrigger: function() {
                    that.collectionView.collectionView[id].showUpdateUser();
                }});
            }});
        },

        onRouteDeleteUser: function(id) {
            var that = this;
            Coke.log.debug("run UserRouter.onRouteDeleteUser()");
            that.layoutView.render({afterTrigger: function() {
                that.collectionView = new UserCollectionView({router: that});
                that.collectionView.startup({afterTrigger: function() {
                    that.collectionView.collectionView[id].showDeleteUser();
                }});
            }});
        }
    });

    return UserRouter;
});
