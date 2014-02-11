define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/views/navbarView',
    'app/views/collectionLayoutView',
    'app/views/crewCollectionView',
], function($, _, Backbone, i18n, Coke, NavbarView, CollectionLayoutView, CrewCollectionView) {

    var CrewRouter = Backbone.Router.extend({
        initialize: function(params) {
            params = params || {};
        },
        
        routes: {
            "crew/list" : "onRouteListCrew",

            "crew/filter(/:query)" : "onRouteFilterCrew",

            "crew/create" : "onRouteCreateCrew",

            "crew/detail/:id" : "onRouteDetailCrew",

            "crew/update/:id" : "onRouteUpdateCrew",

            "crew/delete/:id" : "onRouteDeleteCrew"
        },

        onRouteListCrew: function() {
            var that = this;
            Coke.log.debug("run CrewRouter.onRouteListCrew()");
            that.renderCrewPage(function() {});
        },

        onRouteFilterCrew: function(query, page) {
            var page_number = page || 1;
            var that = this;
            Coke.log.debug("run CrewRouter.onRouteFilterCrew()");
            that.renderCrewPage(function() {
                that.collectionView.showFilterCrew(query, page);
            });
        },

        onRouteCreateCrew: function() {
            var that = this;
            Coke.log.debug("run CrewRouter.onRouteCreateCrew()");
            that.renderCrewPage(function() {
                that.collectionView.showCreateCrew();
            });
        },

        onRouteDetailCrew: function(id) {
            var that = this;
            Coke.log.debug("run CrewRouter.onRouteDetailCrew()");
            that.renderCrewPage(function() {
                that.collectionView.collectionView[id].showDetailCrew();
            });
        },

        onRouteUpdateCrew: function(id) {
            var that = this;
            Coke.log.debug("run CrewRouter.onRouteUpdateCrew()");
            that.renderCrewPage(function() {
                that.collectionView.collectionView[id].showUpdateCrew();
            });
        },

        onRouteDeleteCrew: function(id) {
            var that = this;
            Coke.log.debug("run CrewRouter.onRouteDeleteCrew()");
            that.renderCrewPage(function() {
                that.collectionView.collectionView[id].showDeleteCrew();
            });
        },

        renderCrewPage: function(callback) {
            var that = this;
            Coke.startup({
                i18nNamespaces: ['crew'],
                afterTrigger: function(t, session) {
                    that.layoutView = new CollectionLayoutView({
                        title: t('crew:crew.collection.title', {defaultValue: 'Crews'})
                    });
                    that.layoutView.render({afterTrigger: function() {
                        that.collectionView = new CrewCollectionView({router: that});
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

    return CrewRouter;
});
