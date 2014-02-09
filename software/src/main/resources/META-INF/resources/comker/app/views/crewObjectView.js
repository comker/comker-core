define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/models/crewObject',
], function($, _, Backbone, i18n, Coke, CrewObject) {

    var CrewObjectView = Backbone.View.extend({
        tagName: 'div',

        initialize: function(params) {
            params = params || {};
            
            this.model.on('change', this.render, this);

            this.router = params.router;
            if (_.isObject(this.router)) {
                Coke.log.debug("run CrewObjectView.initialize() - router is defined");
            } else {
                Coke.log.debug("run CrewObjectView.initialize() - router not found");
            }
        },

        render: function() {
            Coke.log.debug("run CrewObjectView.render() for model#" + this.model.id);
            var that = this;
            Coke.TemplateManager.get('crew-list-item', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.html($tmpl);
                that.$el.i18n();
            });
            return this;
        },

        events: {
            'click .detail': 'onDetailCrew',
            'click #cancel-detail-crew': 'cancelExtensionCrew',

            'click .update': 'onUpdateCrew',
            'click #submit-update-crew': 'submitUpdateCrew',
            'click #cancel-update-crew': 'cancelExtensionCrew',

            'click .delete': 'onDeleteCrew',
            'click #submit-delete-crew': 'submitDeleteCrew',
            'click #cancel-delete-crew': 'cancelExtensionCrew'
        },

        onDetailCrew: function() {
            Coke.log.debug("run CrewObjectView.onDetailCrew()");
            if (_.isObject(this.router)) {
                this.router.navigate('crew/detail/' + this.model.id);
            } else {
                Coke.log.debug("run CrewObjectView.onDetailCrew() - router not found");
            }
            this.showDetailCrew();
        },

        showDetailCrew: function() {
            Coke.log.debug("run CrewObjectView.showDetailCrew()");
            var that = this;
            Coke.TemplateManager.get('crew-detail-form', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.find(".crew-message").empty();
                that.$el.find(".crew-content").html($tmpl);
                that.$el.find(".crew-dialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        onUpdateCrew: function() {
            Coke.log.debug("run CrewObjectView.onUpdateCrew()");
            if (_.isObject(this.router)) {
                this.router.navigate('crew/update/' + this.model.id);
            } else {
                Coke.log.debug("run CrewObjectView.onUpdateCrew() - router not found");
            }
            this.showUpdateCrew();
        },

        showUpdateCrew: function() {
            Coke.log.debug("run CrewObjectView.showUpdateCrew()");
            var that = this;
            Coke.TemplateManager.get('crew-update-form', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.find(".crew-message").empty();
                that.$el.find(".crew-content").html($tmpl);
                that.$el.find(".crew-dialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        submitUpdateCrew: function( e ) {
            Coke.log.debug("run CrewObjectView.submitUpdateCrew()");
            e.preventDefault();
            var formData = {};
            $( '#formUpdateCrew div' ).children('input').each(function(i, el) {
                if( $(el).val() != '' ){
                    formData[el.id] = $(el).val();
                }
            });

            var that = this;
            this.model.save(formData, {
                wait: true,
                error: function(model, xhr, params) {
                    that.$el.find(".crew-message").html(
                        Coke.alert('Cannot update Crew#' + model.id + '. Please reload page and try again.', 'error')
                    );
                    that.$el.find(".crew-message").slideDown("fast", function() {});
                }
            });
        },

        onDeleteCrew: function() {
            Coke.log.debug("run CrewObjectView.onDeleteCrew()");
            if (_.isObject(this.router)) {
                this.router.navigate('crew/delete/' + this.model.id);
            } else {
                Coke.log.debug("run CrewObjectView.onDeleteCrew() - router not found");
            }
            this.showDeleteCrew();
        },

        showDeleteCrew: function() {
            Coke.log.debug("run CrewObjectView.showDeleteCrew()");
            var that = this;
            Coke.TemplateManager.get('crew-delete-form', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.find(".crew-message").empty();
                that.$el.find(".crew-content").html($tmpl);
                that.$el.find(".crew-dialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        submitDeleteCrew: function(e) {
            Coke.log.debug("run CrewObjectView.submitDeleteCrew()");
            e.preventDefault();

            var that = this;
            this.model.destroy({
                wait: true,
                success: function(model, response, params) {
                    that.remove(); // delete view (this object)
                },
                error: function(model, xhr, params) {
                    that.$el.find(".crew-message").html(
                        Coke.alert('Cannot delete Crew#' + model.id + '. Please reload page and try again.', 'error')
                    );
                    that.$el.find(".crew-message").slideDown("fast", function() {});
                }
            });
        },

        cancelExtensionCrew: function(e) {
            Coke.log.debug("run CrewObjectView.cancelExtensionCrew()");
            e.preventDefault();

            this.$el.find(".crew-message").empty();
            this.$el.find(".crew-content").empty();
            this.$el.find(".crew-dialog").slideUp("fast", function() {});
        }
    });

    return CrewObjectView;
});

