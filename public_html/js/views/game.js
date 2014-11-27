define([
  'backbone',
  'tmpl/game',
  'models/user',
  'models/ship',
  'models/sun',
  'models/enemy',
  'views/stage',
  'views/ship',
  'views/enemy',
  'views/sun',
  'collections/otherShips',
  'socketsHandler',
  'motion'
], function(Backbone, tmpl, userModel, ShipModel, SunModel, EnemyModel, stageView, shipView, EnemyView, sunView, OtherShipsCollection, SocketsHandler) {
  var GameView = Backbone.View.extend({
    className: 'game',
    id: 'game',
    model: userModel,
    events: {
      'click': 'moveShip'
    },
    initialize: function() {
      this.render();
      // var that = this;
      // OtherShipsCollection.fetch();
      this.listenTo(SocketsHandler, 'connection:opened', this.connectionOpened);
      this.listenTo(SocketsHandler, 'connection:newMessage', this.messageReceived);

    },
    template: function() {
      return tmpl();
    },
    render: function() {
      this.$el.html(this.template());
      this.$el.append(stageView.render().$el);
      this.$el.append(sunView.render().$el);
      this.$el.append(shipView.render().$el);
      // this.$el.append(EnemyView.render().$el);
      return this;
    },
    show: function() {
      if (userModel.isLogined()) {
        this.createSocket();
        this.trigger('show', this);
        this.model.set('inGame', true);
      } else {
        userModel.trigger('login:bad');
      }
    },
    createSocket: function() {
      SocketsHandler.create();
    },
    connectionOpened: function(connection) {
      console.log("CONNECTION OPENED")
      ShipModel.set({
        'connection': connection
      });
      EnemyModel.set({
        'connection': connection
      });
      connection.send(JSON.stringify({
        type: "giveOtherShipsCoords",
        sessionId: userModel.get("sessionId")
      }));
    },
    messageReceived: function(event) {
      var response = JSON.parse(event.data);
      console.log("response", response)
      switch (response.type) {
        case "otherShips":
          // EnemyModel.set({'inGame': true});
          var enemyCoord = response.ships[0];
          if (enemyCoord) {
            console.log('otherShips enemyCoord sunCoords', enemyCoord, SunModel.getPosition());
            EnemyModel.initPosition({
              'x': (enemyCoord.prevX - 35),
              'y': (enemyCoord.prevY - 35)
            });

            enemyCoord.x -= 35;
            enemyCoord.y -= 35;
            EnemyModel.setPosition(enemyCoord);
            this.$el.append(EnemyView.render().$el);
          }
          break;
        case "coordsNotification":
          console.log('coordsNotification enemyCoord sunCoords', response, ' -- ', response.x, response.y, SunModel.getPosition());
          var recountedCoords = new Object();
          // recountedCoords.x = 
          // EnemyModel.setPosition({
          //   'x': (response.x - 35),
          //   'y': (response.y - 35),
          //   'prevX': response.prevX,
          //   'prevY': response.prevY
          // });
          EnemyModel.setPosition(response);
          break;
      }
    }
  });

  return new GameView();
});