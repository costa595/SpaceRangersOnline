define([
  'backbone',
  'models/ship'
], function (Backbone, shipModel) {

  var StageModel = Backbone.Model.extend({
    initialize: function () {
      this.listenTo(shipModel, 'move:start', this.move);
    },
    move: function (coords) {
      this.set(coords);
    },
    calculatePosition: function () {
      var x = this.get('x'),
          y = this.get('y');
      if (Math.abs(x) >= Math.abs(y)) {
          this.xpos -= x / Math.abs(x);
          this.ypos -= y / Math.abs(y) * Math.abs(y/x);
      }
      else {
          this.ypos -= y / Math.abs(y);
          this.xpos -= x / Math.abs(x) * Math.abs(x/y);
      }
    }
  });

  return new StageModel();

});