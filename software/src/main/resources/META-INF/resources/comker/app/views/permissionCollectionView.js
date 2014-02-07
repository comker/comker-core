define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/models/permissionCollection',
    'app/views/permissionObjectView',
], function($, _, Backbone, i18n, Coke, PermissionCollection, PermissionObjectView) {

    var PermissionCollectionView = Backbone.View.extend({
        el: '#content',

        initialize: function(options) {
            options = options || {};

            this.collectionView = {};
            this.collection = new PermissionCollection();

            this.stateRendered = false;

            this.router = options.router;
            if (Coke.isDefined(this.router)) {
                Coke.log.debug("run PermissionCollectionView.initialize() - router is defined");
            } else {
                Coke.log.debug("run PermissionCollectionView.initialize() - router not found");
            }
        },

        startup: function(params) {
            params = params || {};

            Coke.log.debug("run PermissionCollectionView.startup()");

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

            Coke.log.debug("run PermissionCollectionView.render()");

            if (this.$el.find("#panelToolbar").find("ul").is(':empty')) {
                Coke.TemplateManager.get('permission-list-toolbar', function(strg) {
                    that.$el.find("#panelToolbar").find("ul").html(strg);
                });
            }
            this.$el.find('#panelDialog').slideUp("fast", function() {});

            this.$el.find("#panelMain").empty();
            this.$el.find("#panelMain").append(
                    $('<table>', {"class": "table table-striped table-bordered"}));

            var beforeFinish = _.after(that.collection.length, function() {
                that.stateRendered = true;
                Coke.log.debug('run afterTrigger of PermissionCollectionView.render()');
                if (_.isFunction(params.afterTrigger)) {
                    params.afterTrigger();
                }
                Coke.log.debug('PermissionCollectionView.render() done!');
            });

            this.collection.each(function(item) {
                this.renderPermission(item);
                beforeFinish();
            }, this);

            return this;
        },

        renderPermission: function(item) {
            var permissionObjectView = new PermissionObjectView({
                    model: item,
                    router: this.router
                });
            this.$el.find("#panelMain").find("table").append(permissionObjectView.render().el);

            this.collectionView[item.id] = permissionObjectView;
        },

        onFilterPermission: function() {
            Coke.log.debug("run PermissionCollectionView.onFilterPermission()");
            if (Coke.isDefined(this.router)) {
                this.router.navigate('permission/filter');
            } else {
                Coke.log.debug("run PermissionCollectionView.onFilterPermission() - router not found");
            }
            this.showFilterPermission();
        },

        showFilterPermission: function(query, page) {
            Coke.log.debug("run PermissionCollectionView.showFilterPermission()");
            var that = this;
            Coke.TemplateManager.get('permission-filter-form', function(strg) {
                that.$el.find(".panelMessage").empty();
                that.$el.find(".panelContent").html(strg);
                that.$el.find("#panelDialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        submitFilterPermission: function(e) {
            Coke.log.debug("run PermissionCollectionView.submitFilterPermission()");
            e.preventDefault();
        },

        cancelFilterPermission: function(e) {
            Coke.log.debug("run PermissionCollectionView.cancelFilterPermission()");
            e.preventDefault();
            this.$el.find(".panelMessage").empty();
            this.$el.find(".panelContent").empty();
            this.$el.find("#panelDialog").slideUp("fast", function() {});
        },

        events:{
            'click .filter': 'onFilterPermission',
            'click #submit-filter-permission': 'submitFilterPermission',
            'click #cancel-filter-permission': 'cancelFilterPermission'
        }
    });

    return PermissionCollectionView;
});