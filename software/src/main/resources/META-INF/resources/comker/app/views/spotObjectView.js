define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/models/spotObject',
], function($, _, Backbone, i18n, Coke, SpotObject) {

    var SpotObjectView = Backbone.View.extend({
        tagName: 'div',

        initialize: function(params) {
            params = params || {};
            
            this.model.on('change', this.render, this);

            this.router = params.router;
            if (_.isObject(this.router)) {
                Coke.log.debug("run SpotObjectView.initialize() - router is defined");
            } else {
                Coke.log.debug("run SpotObjectView.initialize() - router not found");
            }
        },

        render: function() {
            Coke.log.debug("run SpotObjectView.render() for model#" + this.model.id);
            var that = this;
            Coke.TemplateManager.get('spot-list-item', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.html($tmpl);
                that.$el.i18n();
            });
            return this;
        },

        events: {
            'click .detail': 'onDetailSpot',
            'click #cancel-detail-spot': 'cancelExtensionSpot',

            'click .update': 'onUpdateSpot',
            'click #submit-update-spot': 'submitUpdateSpot',
            'click #cancel-update-spot': 'cancelExtensionSpot',

            'click .delete': 'onDeleteSpot',
            'click #submit-delete-spot': 'submitDeleteSpot',
            'click #cancel-delete-spot': 'cancelExtensionSpot'
        },

        onDetailSpot: function() {
            Coke.log.debug("run SpotObjectView.onDetailSpot()");
            if (_.isObject(this.router)) {
                this.router.navigate('spot/detail/' + this.model.id);
            } else {
                Coke.log.debug("run SpotObjectView.onDetailSpot() - router not found");
            }
            this.showDetailSpot();
        },

        showDetailSpot: function() {
            Coke.log.debug("run SpotObjectView.showDetailSpot()");
            var that = this;
            Coke.TemplateManager.get('spot-detail-form', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.find(".spot-message").empty();
                that.$el.find(".spot-content").html($tmpl);
                that.$el.find(".spot-dialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        onUpdateSpot: function() {
            Coke.log.debug("run SpotObjectView.onUpdateSpot()");
            if (_.isObject(this.router)) {
                this.router.navigate('spot/update/' + this.model.id);
            } else {
                Coke.log.debug("run SpotObjectView.onUpdateSpot() - router not found");
            }
            this.showUpdateSpot();
        },

        showUpdateSpot: function() {
            Coke.log.debug("run SpotObjectView.showUpdateSpot()");
            var that = this;
            Coke.TemplateManager.get('spot-update-form', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.find(".spot-message").empty();
                that.$el.find(".spot-content").html($tmpl);
                that.$el.find(".spot-dialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        submitUpdateSpot: function( e ) {
            Coke.log.debug("run SpotObjectView.submitUpdateSpot()");
            e.preventDefault();
            var formData = {};
            $( '#formUpdateSpot div' ).children('input').each(function(i, el) {
                if( $(el).val() != '' ){
                    formData[el.id] = $(el).val();
                }
            });

            var that = this;
            this.model.save(formData, {
                wait: true,
                error: function(model, xhr, params) {
                    that.$el.find(".spot-message").html(
                        Coke.alert('Cannot update Spot#' + model.id + '. Please reload page and try again.', 'error')
                    );
                    that.$el.find(".spot-message").slideDown("fast", function() {});
                }
            });
        },

        onDeleteSpot: function() {
            Coke.log.debug("run SpotObjectView.onDeleteSpot()");
            if (_.isObject(this.router)) {
                this.router.navigate('spot/delete/' + this.model.id);
            } else {
                Coke.log.debug("run SpotObjectView.onDeleteSpot() - router not found");
            }
            this.showDeleteSpot();
        },

        showDeleteSpot: function() {
            Coke.log.debug("run SpotObjectView.showDeleteSpot()");
            var that = this;
            Coke.TemplateManager.get('spot-delete-form', function(tmpl) {
                var $tmpl = tmpl(that.model.toJSON());
                that.$el.find(".spot-message").empty();
                that.$el.find(".spot-content").html($tmpl);
                that.$el.find(".spot-dialog").slideDown("fast", function() {});
                that.$el.i18n();
            });
        },

        submitDeleteSpot: function(e) {
            Coke.log.debug("run SpotObjectView.submitDeleteSpot()");
            e.preventDefault();

            var that = this;
            this.model.destroy({
                wait: true,
                success: function(model, response, params) {
                    that.remove(); // delete view (this object)
                },
                error: function(model, xhr, params) {
                    that.$el.find(".spot-message").html(
                        Coke.alert('Cannot delete Spot#' + model.id + '. Please reload page and try again.', 'error')
                    );
                    that.$el.find(".spot-message").slideDown("fast", function() {});
                }
            });
        },

        cancelExtensionSpot: function(e) {
            Coke.log.debug("run SpotObjectView.cancelExtensionSpot()");
            e.preventDefault();

            this.$el.find(".spot-message").empty();
            this.$el.find(".spot-content").empty();
            this.$el.find(".spot-dialog").slideUp("fast", function() {});
        }
    });

    return SpotObjectView;
});

