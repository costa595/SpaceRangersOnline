define([
  'backbone',
  'tmpl/game',
  'models/user',
  'models/ship',
  'models/sun',
  'models/base',
  'models/enemy',
  'views/stage',
  'views/ship',
  'views/enemy',
  'views/sun',
  'views/base',
  'collections/otherShips',
  'socketsHandler',
  'motion'
], function(Backbone, tmpl, userModel, ShipModel, SunModel, BaseModel, EnemyModel, stageView, shipView, EnemyView, sunView, BaseView, OtherShipsCollection, SocketsHandler, motionController) {
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
      // this.$el.append(BaseView.render().$el);
      
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
      ShipModel.set({
        'connection': connection
      });
      // EnemyModel.set({
      //   'connection': connection
      // });
      connection.send(JSON.stringify({
        type: "giveOtherShipsCoords",
        sessionId: userModel.get("sessionId")
      }));
    },
    messageReceived: function(event) {
      var response = JSON.parse(event.data);
      // console.log('response', response)
      switch (response.type) {
        case "otherShips":
          for (var k = 0; k < response.ships.length; k++) {
            var curShipData = response.ships[k];
            // console.log('curShipData', curShipData)
            
            var existedModels = OtherShipsCollection.where({
              login: curShipData.login
            })
            var tmpModel;
            var exist = false;
            if (existedModels.length != 0) {
              tmpModel = existedModels[0];
              exist = true;
            } else {
              tmpModel = new EnemyModel({
                login: curShipData.login,
                health: curShipData.health
              });
            }

            // console.log('exist', exist)
            var recountedCoords;
            if (!exist) {
              recountedCoords = this.calculateAbsoluteCoords({prevX: curShipData.x, prevY: curShipData.y});
            } else {
              recountedCoords = this.calculateAbsoluteCoords(curShipData);
            }
            
            tmpModel.setPosition(recountedCoords);
            tmpModel.initCoords({
              'x': (curShipData.prevX - 35),
              'y': (curShipData.prevY - 35)
            });

            if (!exist) {
              OtherShipsCollection.add(tmpModel);
              var tmpView = new EnemyView({
                model: tmpModel
              });
              this.$el.append(tmpView.render().$el);
              motionController.addViewListener(tmpView);
            }

          }

          break;
        case "coordsNotification":
          var existedModels = OtherShipsCollection.where({
            login: response.login
          })
          if (existedModels.length != 0) {
            var curModel = existedModels[0];
            var recountedCoords = this.calculateAbsoluteCoords(response);
            curModel.setPosition(recountedCoords);
            curModel.setCoords(response);
          }
          break;

        case "fireToEnemy":
          if (response.victim == userModel.get("login")) {
            if (response.newHealth == 0)
              alert("I have died")
          } else {
            var existedModels = OtherShipsCollection.where({
              login: response.victim
            })
            if (existedModels.length > 0)
              existedModels[0].attacked(response);
            if (response.newHealth == 0)
              shipView.enemyDied();
          }
      }
    },
    calculateAbsoluteCoords: function(coords) {
      //prevX и prevY - относительно солнца
      //enemy.position - относительно центра экрана (корабля игрока)
      var shipCoords = new Object();
      shipCoords.x = $('body').width() / 2;
      shipCoords.y = $('body').height() / 2;
      var sunCoords = SunModel.getPosition();
      var sunToCenterCoords = new Object();
      if (shipCoords && sunCoords) {
        sunToCenterCoords.x = (shipCoords.x - sunCoords.x);
        sunToCenterCoords.y = (shipCoords.y - sunCoords.y);
      } else {
        sunToCenterCoords.x = 0;
        sunToCenterCoords.y = 0;
      }
      var recountedCoords = new Object();
      recountedCoords.x = (coords.prevX - sunToCenterCoords.x - 35);
      recountedCoords.y = (coords.prevY - sunToCenterCoords.y - 35);
      return recountedCoords;
    }
  });

  return new GameView();
});