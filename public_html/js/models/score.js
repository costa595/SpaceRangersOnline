define([
  'jquery',
  'backbone'
], function($, Backbone) {

  var ScoreModel = Backbone.Model.extend({
    defaults: {
      login: 'Sample User',
      value: '100500'
    }
  });

  return ScoreModel;

});
