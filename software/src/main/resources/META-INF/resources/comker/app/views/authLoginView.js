define([
    'jquery',
    'underscore',
    'backbone',
    'i18n',
    'utils',
    'app/models/authCredentials',
], function($, _, Backbone, i18n, Coke, AuthCredentials){

    var AuthLoginView = Backbone.View.extend({
        el: $("#page"),

        initialize: function(params) {
            params = params || {};

            this.model = new AuthCredentials();
        },

        render: function(params) {
            params = params || {};
            
            Coke.log.debug("run AuthLoginView.render()");

            var self = this;
            Coke.TemplateManager.get('auth-login', function(tmpl) {
                var $tmpl = tmpl({});
                self.$el.html($tmpl);
                self.$el.i18n();

                this.username = $("#username");
                this.password = $("#password");

                this.username.change(function(e){
                  self.model.set({username: $(e.currentTarget).val()});
                });

                this.password.change(function(e){
                  self.model.set({password: $(e.currentTarget).val()});
                });
            });
        },

        events: {
            "click #login": "login"
        },

        login: function() {
            var user = this.model.get('username');
            var pword = this.model.get('password');
            alert("You logged in as " + user + " and a password of " + pword);
            return false;
        }
    });

    return AuthLoginView;
});