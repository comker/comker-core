define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
], function($, _, Backbone, Coke) {
    var PermissionObject = Backbone.Model.extend({
        defaults: {
            authority: 'ROLE_NOTHING',
            description: null
        },
        initialize: function(){
            Coke.log.debug("run PermissionObject.initialize()");
        }
    });
    return PermissionObject;
});


