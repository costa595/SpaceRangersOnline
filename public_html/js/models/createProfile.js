define([
  'jquery',
  'backbone'
], function($, Backbone) {
var positionCreateProfile = Backbone.Model.extend({
  initialize: function() {
     this.windowResize();
     result = {
        name : "",
        race : "race_malok",
        class : "class_war"
     }
  },
  sizeWindow :function() {
      var sizeWindow = {
          width : $('body').css('width'),
          height : $('body').css('height')
      }
      return sizeWindow;
  },
  position: function(sizeWindow) {
      var positionWindowCreate = {
          left : (parseInt(sizeWindow.width) - 481)/2, //481px without zoom
          top : (parseInt(sizeWindow.height) - 665)/2  //665px without zoom
      }
      return positionWindowCreate;
  },
  changePosition: function(positionWindowCreate){
    this.set({marginleft : positionWindowCreate.left, margintop: positionWindowCreate.top})
    return this;
  },
  windowResize: function(){
     var that = this;
     $(window).resize(function() {
        that.trigger('window:resize');
     });
  },
  changeModelPerson: function(type,count) {
    if (type == 'name'){
        result.name = count
    }
    if (type == 'race'){
        result.race = count
    }
    if (type == 'class'){
        result.class = count
    }
    return result;
  }

});
    return new positionCreateProfile();
});