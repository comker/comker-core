define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
    'app/models/roleObject',
], function($, _, Backbone, Coke, RoleObject) {

    var RoleCollection = Backbone.Collection.extend({
        model: RoleObject,
        url: '../ws/comker/api/role/crud'
    });

    return RoleCollection;
});
