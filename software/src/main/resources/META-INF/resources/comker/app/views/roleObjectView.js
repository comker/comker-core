define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/models/roleObject',
], function($, _, Backbone, i18n, Coke, RoleObject) {

    var RoleObjectView = Backbone.View.extend({
        tagName: 'div',

        initialize: function(params) {
            params = params || {};
            
            this.model.on('change', this.render, this);

            this.router = params.router;
            if (_.isObject(this.router)) {
                Coke.log.debug("run RoleObjectView.initialize() - router is defined");
            } else {
                Coke.log.debug("run RoleObjectView.initialize() - router not found");
            }
        },

        render: function() {
            Coke.log.debug("run RoleObjectView.render() for model#" + this.model.id);
            var that = this;
            Coke.TemplateManager.get('role-list-item', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.html($tmpl);
                that.$el.i18n();
            });
            return this;
        },

        events: {
            'click .detail': 'onDetailRole',
            'click #cancel-detail-role': 'cancelExtensionRole',

            'click .update': 'onUpdateRole',
            'click #submit-update-role': 'submitUpdateRole',
            'click #cancel-update-role': 'cancelExtensionRole',

            'click .delete': 'onDeleteRole',
            'click #submit-delete-role': 'submitDeleteRole',
            'click #cancel-delete-role': 'cancelExtensionRole'
        },

        onDetailRole: function() {
            Coke.log.debug("run RoleObjectView.onDetailRole()");
            if (_.isObject(this.router)) {
                this.router.navigate('role/detail/' + this.model.id);
            } else {
                Coke.log.debug("run RoleObjectView.onDetailRole() - router not found");
            }
            this.showDetailRole();
        },

        showDetailRole: function() {
            Coke.log.debug("run RoleObjectView.showDetailRole()");
            var that = this;
            Coke.TemplateManager.get('role-detail-form', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.find(".role-message").empty();
                that.$el.find(".role-content").html($tmpl);
                that.$el.find(".role-dialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        onUpdateRole: function() {
            Coke.log.debug("run RoleObjectView.onUpdateRole()");
            if (_.isObject(this.router)) {
                this.router.navigate('role/update/' + this.model.id);
            } else {
                Coke.log.debug("run RoleObjectView.onUpdateRole() - router not found");
            }
            this.showUpdateRole();
        },

        showUpdateRole: function() {
            Coke.log.debug("run RoleObjectView.showUpdateRole()");
            var that = this;
            Coke.TemplateManager.get('role-update-form', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.find(".role-message").empty();
                that.$el.find(".role-content").html($tmpl);
                that.$el.find(".role-dialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        submitUpdateRole: function( e ) {
            Coke.log.debug("run RoleObjectView.submitUpdateRole()");
            e.preventDefault();
            var formData = {};
            $( '#formUpdateRole div' ).children('input').each(function(i, el) {
                if( $(el).val() != '' ){
                    formData[el.id] = $(el).val();
                }
            });

            var that = this;
            this.model.save(formData, {
                wait: true,
                error: function(model, xhr, params) {
                    that.$el.find(".role-message").html(
                        Coke.alert('Cannot update Role#' + model.id + '. Please reload page and try again.', 'error')
                    );
                    that.$el.find(".role-message").slideDown("fast", function() {});
                }
            });
        },

        onDeleteRole: function() {
            Coke.log.debug("run RoleObjectView.onDeleteRole()");
            if (_.isObject(this.router)) {
                this.router.navigate('role/delete/' + this.model.id);
            } else {
                Coke.log.debug("run RoleObjectView.onDeleteRole() - router not found");
            }
            this.showDeleteRole();
        },

        showDeleteRole: function() {
            Coke.log.debug("run RoleObjectView.showDeleteRole()");
            var that = this;
            Coke.TemplateManager.get('role-delete-form', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.find(".role-message").empty();
                that.$el.find(".role-content").html($tmpl);
                that.$el.find(".role-dialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        submitDeleteRole: function(e) {
            Coke.log.debug("run RoleObjectView.submitDeleteRole()");
            e.preventDefault();

            var that = this;
            this.model.destroy({
                wait: true,
                success: function(model, response, params) {
                    that.remove(); // delete view (this object)
                },
                error: function(model, xhr, params) {
                    that.$el.find(".role-message").html(
                        Coke.alert('Cannot delete Role#' + model.id + '. Please reload page and try again.', 'error')
                    );
                    that.$el.find(".role-message").slideDown("fast", function() {});
                }
            });
        },

        cancelExtensionRole: function(e) {
            Coke.log.debug("run RoleObjectView.cancelExtensionRole()");
            e.preventDefault();

            this.$el.find(".role-message").empty();
            this.$el.find(".role-content").empty();
            this.$el.find(".role-dialog").slideUp("fast", function() {});
        }
    });

    return RoleObjectView;
});

