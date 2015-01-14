define([
	'backbone',
	'models/ship',
	'models/sun'
], function(Backbone, shipModel, SunModel) {
	var EnemyModel = Backbone.Model.extend({
		defaults: {
			inGame: false
		},
		initialize: function() {
			this.login = '';
			this.health = 100; //Значение по умолчанию
			this.x = 0; //на какое расстояние перемещаемся
			this.y = 0;
			this.position = new Object(); //Текущая позиция относиткльно центра, по ней и рендерим корабль
			this.position.x = 0;
			this.position.y = 0;
			this.listenTo(shipModel, 'move:start', this.palyerMoved);
			// this.listenTo(EnemyView, 'move:start', this.palyerMoved);
			// this.listenTo(EnemyView, 'move:done', this.palyerMoved);
			this.inGame = false;
			this.palyerIsMoved = false;
			this.setNewPosition = false;
			this.ownDeltha = new Object(); //Расстояние, на которое надо переместиться иходя из перемещеняи пользователя (пришло с сервера)
			this.ownDeltha.x = 0;
			this.ownDeltha.y = 0;

			this.palyerDeltha = new Object(); //Расстояние, на которое происходит перемещение корабля, инициированное пользователем
			this.palyerDeltha.x = 0;
			this.palyerDeltha.y = 0;

			this.marked = false;
		},
		palyerMoved: function(coords) {
			//coords - на сколько переместить корабль
			// this.set(coords);
			console.log('palyerMoved')
			this.palyerDeltha.x = coords.x;
			this.palyerDeltha.y = coords.y;
			this.trigger('enemyMove:start');
		},
		getPosition: function() {
			return this.position;
		},
		setPosition: function (coords) {
			this.position.x = coords.x;
			this.position.y = coords.y;
		},
		initCoords: function(coords) {
			// this.set(coords);
			var newPositions = new Object();
			newPositions.x = coords.x - this.position.x - this.x;
			newPositions.y = coords.y - this.position.y - this.y;
			newPositions.inGame = true;
			this.set(newPositions);
			this.trigger('updateView');
		},
		attacked: function(data){
			this.health = data.newHealth;
			if (this.health == 0){
				this.inGame = false;
			}
			this.trigger('updateView');
		},
		setCoords: function(coords) {
			// this.position = coords;
			this.setNewPosition = true;
			var newPositions = new Object();
			newPositions.x = coords.x - this.position.x;
			newPositions.y = coords.y - this.position.y;
			var delthaMovedX = coords.x - coords.prevX;
			var delthaMovedY = coords.y - coords.prevY;
			var sunCoords = SunModel.getPosition();
			newPositions.x = delthaMovedX;
			newPositions.y = delthaMovedY;
			if (!this.inGame) {
				newPositions.inGame = true;
				this.inGame = true;
			}
			this.ownDeltha.x = newPositions.x;
			this.ownDeltha.y = newPositions.y;
			// this.set(newPositions);
			this.trigger('enemyMove:start');
			//Тут отраотает триггер на model change и строчка нижу выполнится после обработки
			this.setNewPosition = false;
		},
		changeX: function(newX) {
			if (Math.abs(this.ownDeltha.x) > 2)
				this.ownDeltha.x -= newX;
			if (Math.abs(this.palyerDeltha.x) > 2)
				this.palyerDeltha.x -= newX;
			this.position.x += newX;
		},
		changeY: function(newY) {
			if (Math.abs(this.ownDeltha.y) > 2)
				this.ownDeltha.y -= newY;
			if (Math.abs(this.palyerDeltha.y) > 2)
				this.palyerDeltha.y -= newY;
			this.position.y += newY;
		},
		markThisShip: function() {
			this.marked = true;
			this.trigger('updateView');
		},
	});

	return EnemyModel;
});