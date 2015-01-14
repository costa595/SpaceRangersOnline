define([
  'backbone',
  'models/ship'
], function (Backbone, shipModel) {

  var BaseModel = Backbone.Model.extend({
    initialize: function () {
      this.listenTo(shipModel, 'move:start', this.startMove);
    },
    startMove: function (coords) {
      // console.log('startMove Base', coords)
      this.set(coords);
    },
    setCenter: function (coords) {
      this.center = coords;
      this.position = coords;
    },
    getPosition: function () {
      return this.position;
    },
    changeX: function (newX) {
      this.position.x += newX;
    },
    changeY: function (newY) {
      this.position.y += newY;
    }
  });

  return new BaseModel();
});