define([
  'jquery',
  'backbone',
  'models/enemy'
], function($, Backbone, EnemyModel){
//FUCK!!! I dont know what to do with this stupid collections(((((
  var OtherShipsCollection = Backbone.Collection.extend({
    model: EnemyModel,
    url: '/api/v1/score',

    sync: function(method, model, options){
    	console.log('collection sync', method, model, options);
    	options.success(model);
    }
  });

  return new OtherShipsCollection();
});
