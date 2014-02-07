define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/models/roleCollection',
    'app/views/roleObjectView',
], function($, _, Backbone, i18n, Coke, RoleCollection, RoleObjectView) {

    var RoleCollectionView = Backbone.View.extend({
        el: '#content',

        initialize: function(options) {
            options = options || {};

            this.collectionView = {};
            this.collection = new RoleCollection();

            this.stateRendered = false;

            this.router = options.router;
            if (_.isObject(this.router)) {
                Coke.log.debug("run RoleCollectionView.initialize() - router is defined");
            } else {
                Coke.log.debug("run RoleCollectionView.initialize() - router not found");
            }
        },

        startup: function(params) {
            params = params || {};

            Coke.log.debug("run RoleCollectionView.startup()");

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

            Coke.log.debug("run RoleCollectionView.render()");

            if (this.$el.find("#panelToolbar").find("ul").is(':empty')) {
                Coke.TemplateManager.get('role-list-toolbar', function(strg) {
                    var toolbarBox = that.$el.find("#panelToolbar").find("ul");
                    toolbarBox.html(strg);
                    toolbarBox.i18n();
                });
            }
            this.$el.find('#panelDialog').slideUp("fast", function() {});

            this.$el.find("#panelMain").empty();

            var beforeFinish = _.after(that.collection.length, function() {
                that.stateRendered = true;
                Coke.log.debug('run afterTrigger of RoleCollectionView.render()');
                if (_.isFunction(params.afterTrigger)) {
                    params.afterTrigger();
                }
                Coke.log.debug('RoleCollectionView.render() done!');
            });

            this.collection.each(function(item) {
                this.renderRole(item);
                beforeFinish();
            }, this);

            return this;
        },

        renderRole: function(item) {
            var roleObjectView = new RoleObjectView({
                    model: item,
                    router: this.router
                });
            this.$el.find("#panelMain").append(roleObjectView.render().el);

            this.collectionView[item.id] = roleObjectView;
        },

        onCreateRole: function() {
            Coke.log.debug("run RoleCollectionView.onCreateRole()");
            if (_.isObject(this.router)) {
                this.router.navigate('role/create');
            } else {
                Coke.log.debug("run RoleCollectionView.onCreateRole() - router not found");
            }
            this.showCreateRole();
        },

        showCreateRole: function() {
            Coke.log.debug("run RoleCollectionView.showCreateRole()");
            var that = this;
            Coke.TemplateManager.get('role-create-form', function(strg) {
                that.$el.find(".panelMessage").empty();
                that.$el.find(".panelContent").html(strg);
                that.$el.find("#panelDialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        onFilterRole: function() {
            Coke.log.debug("run RoleCollectionView.onFilterRole()");
            if (_.isObject(this.router)) {
                this.router.navigate('role/filter');
            } else {
                Coke.log.debug("run RoleCollectionView.onFilterRole() - router not found");
            }
            this.showFilterRole();
        },

        showFilterRole: function(query, page) {
            Coke.log.debug("run RoleCollectionView.showFilterRole()");
            var that = this;
            Coke.TemplateManager.get('role-filter-form', function(strg) {
                that.$el.find(".panelMessage").empty();
                that.$el.find(".panelContent").html(strg);
                that.$el.find("#panelDialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        submitFilterRole: function(e) {
            Coke.log.debug("run RoleCollectionView.submitFilterRole()");
            e.preventDefault();
        },

        cancelFilterRole: function(e) {
            Coke.log.debug("run RoleCollectionView.cancelFilterRole()");
            e.preventDefault();
            this.$el.find(".panelMessage").empty();
            this.$el.find(".panelContent").empty();
            this.$el.find("#panelDialog").slideUp("fast", function() {});
        },

        events:{
            'click .create': 'onCreateRole',
            'click #submit-create-role': 'submitCreateRole',
            'click #cancel-create-role': 'cancelCreateRole',

            'click .filter': 'onFilterRole',
            'click #submit-filter-role': 'submitFilterRole',
            'click #cancel-filter-role': 'cancelFilterRole'
        },

        submitCreateRole: function(e) {
            Coke.log.debug("run RoleCollectionView.submitCreateRole()");
            e.preventDefault();

            var formData = {};
            $( '#formCreateRole div' ).children('input').each(function(i, el) {
                if( $(el).val() != '' ){
                    formData[el.id] = $(el).val();
                }
            });

            var that = this;
            this.collection.create(formData, {
                wait: true,

                success: function(model, response, options){
                    Coke.log.debug('Role creation success');
                    that.renderRole(model);
                    that.$el.find(".panelMessage").html(
                        Coke.alert('Role "' + model.get('title') + '" had been inserted successfully.', 'success')
                    );
                    that.$el.find(".panelMessage").slideDown("fast", function() {});
                },

                error: function(model, xhr, options) {
                    Coke.log.debug('Role creation failure');
                    that.$el.find(".panelMessage").html(
                        Coke.alert('Cannot insert Role "' + model.get('title') + '". Please reload page and try again.', 'error')
                    );
                    that.$el.find(".panelMessage").slideDown("fast", function() {});
                }
            });
        },

        cancelCreateRole: function(e) {
            Coke.log.debug("run RoleCollectionView.cancelCreateRole()");
            e.preventDefault();
            this.$el.find(".panelMessage").empty();
            this.$el.find(".panelContent").empty();
            this.$el.find("#panelDialog").slideUp("fast", function() {});
        }
    });

    return RoleCollectionView;
});