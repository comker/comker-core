define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/views/navbarView',
    'app/views/collectionLayoutView',
    'app/views/spotCollectionView',
], function($, _, Backbone, i18n, Coke, NavbarView, CollectionLayoutView, SpotCollectionView) {

    var SpotRouter = Backbone.Router.extend({
        initialize: function(params) {
            params = params || {};
        },
        
        routes: {
            "spot/list" : "onRouteListSpot",

            "spot/filter(/:query)" : "onRouteFilterSpot",

            "spot/create" : "onRouteCreateSpot",

            "spot/detail/:id" : "onRouteDetailSpot",

            "spot/update/:id" : "onRouteUpdateSpot",

            "spot/delete/:id" : "onRouteDeleteSpot"
        },

        onRouteListSpot: function() {
            var that = this;
            Coke.log.debug("run SpotRouter.onRouteListSpot()");
            that.renderSpotPage(function() {});
        },

        onRouteFilterSpot: function(query, page) {
            var page_number = page || 1;
            var that = this;
            Coke.log.debug("run SpotRouter.onRouteFilterSpot()");
            that.renderSpotPage(function() {
                that.collectionView.showFilterSpot(query, page);
            });
        },

        onRouteCreateSpot: function() {
            var that = this;
            Coke.log.debug("run SpotRouter.onRouteCreateSpot()");
            that.renderSpotPage(function() {
                that.collectionView.showCreateSpot();
            });
        },

        onRouteDetailSpot: function(id) {
            var that = this;
            Coke.log.debug("run SpotRouter.onRouteDetailSpot()");
            that.renderSpotPage(function() {
                that.collectionView.collectionView[id].showDetailSpot();
            });
        },

        onRouteUpdateSpot: function(id) {
            var that = this;
            Coke.log.debug("run SpotRouter.onRouteUpdateSpot()");
            that.renderSpotPage(function() {
                that.collectionView.collectionView[id].showUpdateSpot();
            });
        },

        onRouteDeleteSpot: function(id) {
            var that = this;
            Coke.log.debug("run SpotRouter.onRouteDeleteSpot()");
            that.renderSpotPage(function() {
                that.collectionView.collectionView[id].showDeleteSpot();
            });
        },

        renderSpotPage: function(callback) {
            var that = this;
            Coke.startup({
                i18nNamespaces: ['spot'],
                afterTrigger: function(t, session) {
                    that.layoutView = new CollectionLayoutView({
                        title: t('spot:spot.collection.title', {defaultValue: 'Spots'})
                    });
                    that.layoutView.render({afterTrigger: function() {
                        that.collectionView = new SpotCollectionView({router: that});
                        that.collectionView.startup({afterTrigger: function() {
                            if (_.isFunction(callback)) callback();
                        }});
                    }});
                    that.navbarView = new NavbarView();
                    that.navbarView.render();
                }
            });
        }
    });

    return SpotRouter;
});
