define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
], function($, _, Backbone, Coke) {
    var RoleObject = Backbone.Model.extend({
        defaults: {
            code: 'Unknown',
            name: 'Unknown',
            description: 'Unknown'
        },
        initialize: function(){
            Coke.log.debug("run RoleObject.initialize()");
        }
    });
    return RoleObject;
});


