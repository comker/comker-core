define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
    'app/models/userObject',
], function($, _, Backbone, Coke, UserObject) {

    var UserObjectView = Backbone.View.extend({
        tagName: 'div',

        initialize: function(params) {
            params = params || {};
            
            this.model.on('change', this.render, this);

            this.router = params.router;
            if (Coke.isDefined(this.router)) {
                Coke.log.debug("run UserObjectView.initialize() - router is defined");
            } else {
                Coke.log.debug("run UserObjectView.initialize() - router not found");
            }
        },

        render: function() {
            Coke.log.debug("run UserObjectView.render() for model#" + this.model.id);
            var that = this;
            Coke.TemplateManager.get('user', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.html($tmpl);
            });
            return this;
        },

        events: {
            'click .detail': 'onDetailUser',
            'click #cancel-detail-user': 'cancelExtensionUser',

            'click .update': 'onUpdateUser',
            'click #submit-update-user': 'submitUpdateUser',
            'click #cancel-update-user': 'cancelExtensionUser',

            'click .delete': 'onDeleteUser',
            'click #submit-delete-user': 'submitDeleteUser',
            'click #cancel-delete-user': 'cancelExtensionUser'
        },

        onDetailUser: function() {
            Coke.log.debug("run UserObjectView.onDetailUser()");
            if (Coke.isDefined(this.router)) {
                this.router.navigate('user/detail/' + this.model.id);
            } else {
                Coke.log.debug("run UserObjectView.onDetailUser() - router not found");
            }
            this.showDetailUser();
        },

        showDetailUser: function() {
            Coke.log.debug("run UserObjectView.showDetailUser()");
            var that = this;
            Coke.TemplateManager.get('user-detail-form', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.find(".user-message").empty();
                that.$el.find(".user-content").html($tmpl);
                that.$el.find(".user-dialog").slideDown("fast", function() {});
            });
        },

        onUpdateUser: function() {
            Coke.log.debug("run UserObjectView.onUpdateUser()");
            if (Coke.isDefined(this.router)) {
                this.router.navigate('user/update/' + this.model.id);
            } else {
                Coke.log.debug("run UserObjectView.onUpdateUser() - router not found");
            }
            this.showUpdateUser();
        },

        showUpdateUser: function() {
            Coke.log.debug("run UserObjectView.showUpdateUser()");
            var that = this;
            Coke.TemplateManager.get('user-update-form', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.find(".user-message").empty();
                that.$el.find(".user-content").html($tmpl);
                that.$el.find(".user-dialog").slideDown("fast", function() {});
            });
        },

        submitUpdateUser: function( e ) {
            Coke.log.debug("run UserObjectView.submitUpdateUser()");
            e.preventDefault();
            var formData = {};
            $( '#updateUser div' ).children('input').each(function(i, el) {
                if( $(el).val() != '' ){
                    formData[el.id] = $(el).val();
                }
            });

            var that = this;
            this.model.save(formData, {
                wait: true,
                error: function(model, xhr, params) {
                    that.$el.find(".user-message").html(
                        Coke.alert('Cannot update User#' + model.id + '. Please reload page and try again.', 'error')
                    );
                    that.$el.find(".user-message").slideDown("fast", function() {});
                }
            });
        },

        onDeleteUser: function() {
            Coke.log.debug("run UserObjectView.onDeleteUser()");
            if (Coke.isDefined(this.router)) {
                this.router.navigate('user/delete/' + this.model.id);
            } else {
                Coke.log.debug("run UserObjectView.onDeleteUser() - router not found");
            }
            this.showDeleteUser();
        },

        showDeleteUser: function() {
            Coke.log.debug("run UserObjectView.showDeleteUser()");
            var that = this;
            Coke.TemplateManager.get('user-delete-form', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.find(".user-message").empty();
                that.$el.find(".user-content").html($tmpl);
                that.$el.find(".user-dialog").slideDown("fast", function() {});
            });
        },

        submitDeleteUser: function(e) {
            Coke.log.debug("run UserObjectView.submitDeleteUser()");
            e.preventDefault();

            var that = this;
            this.model.destroy({
                wait: true,
                success: function(model, response, params) {
                    that.remove(); // delete view (this object)
                },
                error: function(model, xhr, params) {
                    that.$el.find(".user-message").html(
                        Coke.alert('Cannot delete User#' + model.id + '. Please reload page and try again.', 'error')
                    );
                    that.$el.find(".user-message").slideDown("fast", function() {});
                }
            });
        },

        cancelExtensionUser: function(e) {
            Coke.log.debug("run UserObjectView.cancelExtensionUser()");
            e.preventDefault();

            this.$el.find(".user-message").empty();
            this.$el.find(".user-content").empty();
            this.$el.find(".user-dialog").slideUp("fast", function() {});
        }
    });

    return UserObjectView;
});

