define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/models/userCollection',
    'app/views/userObjectView',
], function($, _, Backbone, i18n, Coke, UserCollection, UserObjectView) {

    var UserCollectionView = Backbone.View.extend({
        el: '#content',

        initialize: function(options) {
            options = options || {};

            this.collectionView = {};
            this.collection = new UserCollection();

            this.stateRendered = false;

            this.router = options.router;
            if (_.isObject(this.router)) {
                Coke.log.debug("run UserCollectionView.initialize() - router is defined");
            } else {
                Coke.log.debug("run UserCollectionView.initialize() - router not found");
            }
        },

        startup: function(params) {
            params = params || {};

            Coke.log.debug("run UserCollectionView.startup()");

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

            Coke.log.debug("run UserCollectionView.render()");

            if (this.$el.find("#panelToolbar").find("ul").is(':empty')) {
                Coke.TemplateManager.get('user-list-toolbar', function(strg) {
                    var toolbarBox = that.$el.find("#panelToolbar").find("ul");
                    toolbarBox.html(strg);
                    toolbarBox.i18n();
                });
            }
            this.$el.find('#panelDialog').slideUp("fast", function() {});

            this.$el.find("#panelMain").empty();

            var beforeFinish = _.after(that.collection.length, function() {
                that.stateRendered = true;
                Coke.log.debug('run afterTrigger of UserCollectionView.render()');
                if (_.isFunction(params.afterTrigger)) {
                    params.afterTrigger();
                }
                Coke.log.debug('UserCollectionView.render() done!');
            });

            this.collection.each(function(item) {
                this.renderUser(item);
                beforeFinish();
            }, this);

            return this;
        },

        renderUser: function(item) {
            var userObjectView = new UserObjectView({
                    model: item,
                    router: this.router
                });
            this.$el.find("#panelMain").append(userObjectView.render().el);

            this.collectionView[item.id] = userObjectView;
        },

        onCreateUser: function() {
            Coke.log.debug("run UserCollectionView.onCreateUser()");
            if (_.isObject(this.router)) {
                this.router.navigate('user/create');
            } else {
                Coke.log.debug("run UserCollectionView.onCreateUser() - router not found");
            }
            this.showCreateUser();
        },

        showCreateUser: function() {
            Coke.log.debug("run UserCollectionView.showCreateUser()");
            var that = this;
            Coke.TemplateManager.get('user-create-form', function(strg) {
                that.$el.find(".panelMessage").empty();
                that.$el.find(".panelContent").html(strg);
                that.$el.find("#panelDialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        onFilterUser: function() {
            Coke.log.debug("run UserCollectionView.onFilterUser()");
            if (_.isObject(this.router)) {
                this.router.navigate('user/filter');
            } else {
                Coke.log.debug("run UserCollectionView.onFilterUser() - router not found");
            }
            this.showFilterUser();
        },

        showFilterUser: function(query, page) {
            Coke.log.debug("run UserCollectionView.showFilterUser()");
            var that = this;
            Coke.TemplateManager.get('user-filter-form', function(strg) {
                that.$el.find(".panelMessage").empty();
                that.$el.find(".panelContent").html(strg);
                that.$el.find("#panelDialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        submitFilterUser: function(e) {
            Coke.log.debug("run UserCollectionView.submitFilterUser()");
            e.preventDefault();
        },

        cancelFilterUser: function(e) {
            Coke.log.debug("run UserCollectionView.cancelFilterUser()");
            e.preventDefault();
            this.$el.find(".panelMessage").empty();
            this.$el.find(".panelContent").empty();
            this.$el.find("#panelDialog").slideUp("fast", function() {});
        },

        events:{
            'click .create': 'onCreateUser',
            'click #submit-create-user': 'submitCreateUser',
            'click #cancel-create-user': 'cancelCreateUser',

            'click .filter': 'onFilterUser',
            'click #submit-filter-user': 'submitFilterUser',
            'click #cancel-filter-user': 'cancelFilterUser'
        },

        submitCreateUser: function(e) {
            Coke.log.debug("run UserCollectionView.submitCreateUser()");
            e.preventDefault();

            var formData = {};
            $( '#formCreateUser div' ).children('input').each(function(i, el) {
                if( $(el).val() != '' ){
                    formData[el.id] = $(el).val();
                }
            });

            var that = this;
            this.collection.create(formData, {
                wait: true,

                success: function(model, response, options){
                    Coke.log.debug('User creation success');
                    that.renderUser(model);
                    that.$el.find(".panelMessage").html(
                        Coke.alert('User "' + model.get('title') + '" had been inserted successfully.', 'success')
                    );
                    that.$el.find(".panelMessage").slideDown("fast", function() {});
                },

                error: function(model, xhr, options) {
                    Coke.log.debug('User creation failure');
                    that.$el.find(".panelMessage").html(
                        Coke.alert('Cannot insert User "' + model.get('title') + '". Please reload page and try again.', 'error')
                    );
                    that.$el.find(".panelMessage").slideDown("fast", function() {});
                }
            });
        },

        cancelCreateUser: function(e) {
            Coke.log.debug("run UserCollectionView.cancelCreateUser()");
            e.preventDefault();
            this.$el.find(".panelMessage").empty();
            this.$el.find(".panelContent").empty();
            this.$el.find("#panelDialog").slideUp("fast", function() {});
        }
    });

    return UserCollectionView;
});