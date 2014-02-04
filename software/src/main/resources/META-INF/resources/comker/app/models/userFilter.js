define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
], function($, _, Backbone, Coke) {
    var UserFilter = Backbone.Model.extend({
        defaults: {
            title: '',
            author: ''
        }
    });
    return UserFilter;
});

