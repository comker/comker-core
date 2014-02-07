define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
], function($, _, Backbone, Coke) {
    var AuthCredentials = Backbone.Model.extend({
        defaults: {
            username: null,
            password: null
        },
        initialize: function(){
            Coke.log.debug("run AuthCredentials.initialize()");
        }
    });
    return AuthCredentials;
});


