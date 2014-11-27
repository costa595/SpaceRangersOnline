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
			// this.connection = new WebSocket('ws://127.0.0.1:8000/api/v1/pull');
			this.x = 0; //на какое расстояние перемещаемся
			this.y = 0;
			this.position = new Object(); //Текущая позиция относиткльно центра, по ней и рендерим корабль
			this.position.x = 0;
			this.position.y = 0;
			this.listenTo(shipModel, 'move:start', this.palyerMoved);
			// this.listenTo(EnemyView, 'move:start', this.palyerMoved);
			// this.listenTo(EnemyView, 'move:done', this.palyerMoved);
			this.inGame = false;
			this.inMove = false;

		},
		palyerMoved: function(coords) {
			//coords - на сколько переместить корабль
			// console.log('palyerMoved new coords ', coords, this.get("x"), this.get("y"), this.position, this.inMove, this.get("inMove"));
			if (this.get("inMove")) {
				coords.x = this.get("x") - coords.x;
				coords.y = this.get("y") - coords.y;
			}

			this.set(coords);
		},
		calculatePosition: function() {},
		getPosition: function() {
			return this.position;
		},
		initPosition: function(coords) {
			// this.set(coords);
			var newPositions = new Object();
			newPositions.x = coords.x - this.position.x - this.x;
			newPositions.y = coords.y - this.position.y - this.y;
			newPositions.inGame = true;
			this.set(newPositions);
			// console.log('modelInited!')
			this.trigger('modelInited');
		},
		setPosition: function(coords) {
			// this.position = coords;
			// console.log('SET POSITION COORDS', coords)
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
			// console.log('setPosition this.position', this.x, this.y, coords, this.position, sunCoords, newPositions.x, newPositions.y)

			this.set(newPositions);
		},
		changeX: function(newX) {
			this.position.x += newX;
		},
		changeY: function(newY) {
			this.position.y += newY;
		}
	});

	return new EnemyModel();
});