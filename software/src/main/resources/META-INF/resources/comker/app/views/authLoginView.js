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

                self.username = $("#username");
                self.password = $("#password");

                self.username.change(function(e){
                  self.model.set({username: $(e.currentTarget).val()});
                  Coke.log.debug("run AuthLoginView.username.change() - changed");
                });

                self.password.change(function(e){
                  self.model.set({password: $(e.currentTarget).val()});
                  Coke.log.debug("run AuthLoginView.password.change() - changed");
                });
            });
        },

        events: {
            "click #login": "login"
        },

        login: function() {
            var form_username = this.model.get('username');
            var form_password = this.model.get('password');
            $.ajax({
                url: '../j_spring_security_check',
                type: 'POST',
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                dataType: 'json',
                data: {j_username: form_username, j_password: form_password},
                beforeSend: function(data){
                    Coke.log.debug("run AuthLoginView.login() - before $.ajax()");
                    console.log(data);
                },
                success: function(data) {
                    Coke.log.debug("run AuthLoginView.login() - run $.ajax() success");
                },
                error: function(xhr, status, error) {
                    Coke.log.debug("run AuthLoginView.login() - run $.ajax() error");
                },
                complete: function(xhr, status) {
                    Coke.log.debug("run AuthLoginView.login() - complete $.ajax()");
                }
            });
            return false;
        }
    });

    return AuthLoginView;
});