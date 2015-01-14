define([
  'jquery',
  'backbone'
], function($, Backbone) {
	var Profile = Backbone.Model.extend({
		initialize: function(){
			console.log('model_profile')
		}
	});

	return new Profile();
});