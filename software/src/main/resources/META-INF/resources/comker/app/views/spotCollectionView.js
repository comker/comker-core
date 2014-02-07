define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/models/spotCollection',
    'app/views/spotObjectView',
], function($, _, Backbone, i18n, Coke, SpotCollection, SpotObjectView) {

    var SpotCollectionView = Backbone.View.extend({
        el: '#content',

        initialize: function(options) {
            options = options || {};

            this.collectionView = {};
            this.collection = new SpotCollection();

            this.stateRendered = false;

            this.router = options.router;
            if (Coke.isDefined(this.router)) {
                Coke.log.debug("run SpotCollectionView.initialize() - router is defined");
            } else {
                Coke.log.debug("run SpotCollectionView.initialize() - router not found");
            }
        },

        startup: function(params) {
            params = params || {};

            Coke.log.debug("run SpotCollectionView.startup()");

            var that = this;
            this.collection.fetch({success: function() {
                that.render({
                    afterTrigger: params.afterTrigger
                });
            }});
        },

        render: function(params) {
            var that = this;
            params = params || {};

            Coke.log.debug("run SpotCollectionView.render()");

            if (this.$el.find("#panelToolbar").find("ul").is(':empty')) {
                Coke.TemplateManager.get('spot-list-toolbar', function(strg) {
                    var toolbarBox = that.$el.find("#panelToolbar").find("ul");
                    toolbarBox.html(strg);
                    toolbarBox.i18n();
                });
            }
            this.$el.find('#panelDialog').slideUp("fast", function() {});

            this.$el.find("#panelMain").empty();

            var beforeFinish = _.after(that.collection.length, function() {
                that.stateRendered = true;
                Coke.log.debug('run afterTrigger of SpotCollectionView.render()');
                if (_.isFunction(params.afterTrigger)) {
                    params.afterTrigger();
                }
                Coke.log.debug('SpotCollectionView.render() done!');
            });

            this.collection.each(function(item) {
                this.renderSpot(item);
                beforeFinish();
            }, this);

            return this;
        },

        renderSpot: function(item) {
            var spotObjectView = new SpotObjectView({
                    model: item,
                    router: this.router
                });
            this.$el.find("#panelMain").append(spotObjectView.render().el);

            this.collectionView[item.id] = spotObjectView;
        },

        onCreateSpot: function() {
            Coke.log.debug("run SpotCollectionView.onCreateSpot()");
            if (Coke.isDefined(this.router)) {
                this.router.navigate('spot/create');
            } else {
                Coke.log.debug("run SpotCollectionView.onCreateSpot() - router not found");
            }
            this.showCreateSpot();
        },

        showCreateSpot: function() {
            Coke.log.debug("run SpotCollectionView.showCreateSpot()");
            var that = this;
            Coke.TemplateManager.get('spot-create-form', function(strg) {
                that.$el.find(".panelMessage").empty();
                that.$el.find(".panelContent").html(strg);
                that.$el.find("#panelDialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        onFilterSpot: function() {
            Coke.log.debug("run SpotCollectionView.onFilterSpot()");
            if (Coke.isDefined(this.router)) {
                this.router.navigate('spot/filter');
            } else {
                Coke.log.debug("run SpotCollectionView.onFilterSpot() - router not found");
            }
            this.showFilterSpot();
        },

        showFilterSpot: function(query, page) {
            Coke.log.debug("run SpotCollectionView.showFilterSpot()");
            var that = this;
            Coke.TemplateManager.get('spot-filter-form', function(strg) {
                that.$el.find(".panelMessage").empty();
                that.$el.find(".panelContent").html(strg);
                that.$el.find("#panelDialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        submitFilterSpot: function(e) {
            Coke.log.debug("run SpotCollectionView.submitFilterSpot()");
            e.preventDefault();
        },

        cancelFilterSpot: function(e) {
            Coke.log.debug("run SpotCollectionView.cancelFilterSpot()");
            e.preventDefault();
            this.$el.find(".panelMessage").empty();
            this.$el.find(".panelContent").empty();
            this.$el.find("#panelDialog").slideUp("fast", function() {});
        },

        events:{
            'click .create': 'onCreateSpot',
            'click #submit-create-spot': 'submitCreateSpot',
            'click #cancel-create-spot': 'cancelCreateSpot',

            'click .filter': 'onFilterSpot',
            'click #submit-filter-spot': 'submitFilterSpot',
            'click #cancel-filter-spot': 'cancelFilterSpot'
        },

        submitCreateSpot: function(e) {
            Coke.log.debug("run SpotCollectionView.submitCreateSpot()");
            e.preventDefault();

            var formData = {};
            $( '#formCreateSpot div' ).children('input').each(function(i, el) {
                if( $(el).val() != '' ){
                    formData[el.id] = $(el).val();
                }
            });

            var that = this;
            this.collection.create(formData, {
                wait: true,

                success: function(model, response, options){
                    Coke.log.debug('Spot creation success');
                    that.renderSpot(model);
                    that.$el.find(".panelMessage").html(
                        Coke.alert('Spot "' + model.get('title') + '" had been inserted successfully.', 'success')
                    );
                    that.$el.find(".panelMessage").slideDown("fast", function() {});
                },

                error: function(model, xhr, options) {
                    Coke.log.debug('Spot creation failure');
                    that.$el.find(".panelMessage").html(
                        Coke.alert('Cannot insert Spot "' + model.get('title') + '". Please reload page and try again.', 'error')
                    );
                    that.$el.find(".panelMessage").slideDown("fast", function() {});
                }
            });
        },

        cancelCreateSpot: function(e) {
            Coke.log.debug("run SpotCollectionView.cancelCreateSpot()");
            e.preventDefault();
            this.$el.find(".panelMessage").empty();
            this.$el.find(".panelContent").empty();
            this.$el.find("#panelDialog").slideUp("fast", function() {});
        }
    });

    return SpotCollectionView;
});