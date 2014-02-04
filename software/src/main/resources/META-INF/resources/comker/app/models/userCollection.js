define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
    'app/models/userObject',
], function($, _, Backbone, Coke, UserObject) {

    var UserCollection = Backbone.Collection.extend({
        model: UserObject,
        url: '../ws/comker/api/user/crud'
    });

    return UserCollection;
});
