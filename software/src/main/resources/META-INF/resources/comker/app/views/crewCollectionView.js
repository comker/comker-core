define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/models/crewCollection',
    'app/views/crewObjectView',
], function($, _, Backbone, i18n, Coke, CrewCollection, CrewObjectView) {

    var CrewCollectionView = Backbone.View.extend({
        el: '#content',

        initialize: function(options) {
            options = options || {};

            this.collectionView = {};
            this.collection = new CrewCollection();

            this.stateRendered = false;

            this.router = options.router;
            if (_.isObject(this.router)) {
                Coke.log.debug("run CrewCollectionView.initialize() - router is defined");
            } else {
                Coke.log.debug("run CrewCollectionView.initialize() - router not found");
            }
        },

        startup: function(params) {
            params = params || {};

            Coke.log.debug("run CrewCollectionView.startup()");

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

            Coke.log.debug("run CrewCollectionView.render()");

            if (this.$el.find("#panelToolbar").find("ul").is(':empty')) {
                Coke.TemplateManager.get('crew-list-toolbar', function(strg) {
                    var toolbarBox = that.$el.find("#panelToolbar").find("ul");
                    toolbarBox.html(strg);
                    toolbarBox.i18n();
                });
            }
            this.$el.find('#panelDialog').slideUp("fast", function() {});

            this.$el.find("#panelMain").empty();

            var beforeFinish = _.after(that.collection.length, function() {
                that.stateRendered = true;
                Coke.log.debug('run afterTrigger of CrewCollectionView.render()');
                if (_.isFunction(params.afterTrigger)) {
                    params.afterTrigger();
                }
                Coke.log.debug('CrewCollectionView.render() done!');
            });

            this.collection.each(function(item) {
                this.renderCrew(item);
                beforeFinish();
            }, this);

            return this;
        },

        renderCrew: function(item) {
            var crewObjectView = new CrewObjectView({
                    model: item,
                    router: this.router
                });
            this.$el.find("#panelMain").append(crewObjectView.render().el);

            this.collectionView[item.id] = crewObjectView;
        },

        onCreateCrew: function() {
            Coke.log.debug("run CrewCollectionView.onCreateCrew()");
            if (_.isObject(this.router)) {
                this.router.navigate('crew/create');
            } else {
                Coke.log.debug("run CrewCollectionView.onCreateCrew() - router not found");
            }
            this.showCreateCrew();
        },

        showCreateCrew: function() {
            Coke.log.debug("run CrewCollectionView.showCreateCrew()");
            var that = this;
            Coke.TemplateManager.get('crew-create-form', function(strg) {
                that.$el.find(".panelMessage").empty();
                that.$el.find(".panelContent").html(strg);
                that.$el.find("#panelDialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        onFilterCrew: function() {
            Coke.log.debug("run CrewCollectionView.onFilterCrew()");
            if (_.isObject(this.router)) {
                this.router.navigate('crew/filter');
            } else {
                Coke.log.debug("run CrewCollectionView.onFilterCrew() - router not found");
            }
            this.showFilterCrew();
        },

        showFilterCrew: function(query, page) {
            Coke.log.debug("run CrewCollectionView.showFilterCrew()");
            var that = this;
            Coke.TemplateManager.get('crew-filter-form', function(strg) {
                that.$el.find(".panelMessage").empty();
                that.$el.find(".panelContent").html(strg);
                that.$el.find("#panelDialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        submitFilterCrew: function(e) {
            Coke.log.debug("run CrewCollectionView.submitFilterCrew()");
            e.preventDefault();
        },

        cancelFilterCrew: function(e) {
            Coke.log.debug("run CrewCollectionView.cancelFilterCrew()");
            e.preventDefault();
            this.$el.find(".panelMessage").empty();
            this.$el.find(".panelContent").empty();
            this.$el.find("#panelDialog").slideUp("fast", function() {});
        },

        events:{
            'click .create': 'onCreateCrew',
            'click #submit-create-crew': 'submitCreateCrew',
            'click #cancel-create-crew': 'cancelCreateCrew',

            'click .filter': 'onFilterCrew',
            'click #submit-filter-crew': 'submitFilterCrew',
            'click #cancel-filter-crew': 'cancelFilterCrew'
        },

        submitCreateCrew: function(e) {
            Coke.log.debug("run CrewCollectionView.submitCreateCrew()");
            e.preventDefault();

            var formData = {};
            $( '#formCreateCrew div' ).children('input').each(function(i, el) {
                if( $(el).val() != '' ){
                    formData[el.id] = $(el).val();
                }
            });

            var that = this;
            this.collection.create(formData, {
                wait: true,

                success: function(model, response, options){
                    Coke.log.debug('Crew creation success');
                    that.renderCrew(model);
                    that.$el.find(".panelMessage").html(
                        Coke.alert('Crew "' + model.get('title') + '" had been inserted successfully.', 'success')
                    );
                    that.$el.find(".panelMessage").slideDown("fast", function() {});
                },

                error: function(model, xhr, options) {
                    Coke.log.debug('Crew creation failure');
                    that.$el.find(".panelMessage").html(
                        Coke.alert('Cannot insert Crew "' + model.get('title') + '". Please reload page and try again.', 'error')
                    );
                    that.$el.find(".panelMessage").slideDown("fast", function() {});
                }
            });
        },

        cancelCreateCrew: function(e) {
            Coke.log.debug("run CrewCollectionView.cancelCreateCrew()");
            e.preventDefault();
            this.$el.find(".panelMessage").empty();
            this.$el.find(".panelContent").empty();
            this.$el.find("#panelDialog").slideUp("fast", function() {});
        }
    });

    return CrewCollectionView;
});