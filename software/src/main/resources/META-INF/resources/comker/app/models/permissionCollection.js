define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
    'app/models/permissionObject',
], function($, _, Backbone, Coke, PermissionObject) {

    var PermissionCollection = Backbone.Collection.extend({
        model: PermissionObject,
        url: '../ws/comker/api/permission/crud'
    });

    return PermissionCollection;
});
