//Starrr plugin (https://github.com/dobtco/starrr)
var __slice = [].slice;

(function($, window) {
	var Starrr;

	Starrr = (function() {
		Starrr.prototype.defaults = {
				rating: void 0,
				numStars: 5,
				change: function(e, value) {
					/* A implémenter */
					
					/* Une fois la recette notée, remplacer par un texte "Vous avez note cette recette" */
					/* Si l'utilisateur a deja note la recette, afficher directement la note qu'il a mise */
				}
		};

		function Starrr($el, options) {
			var i, _, _ref,
			_this = this;

			this.options = $.extend({}, this.defaults, options);
			this.$el = $el;
			_ref = this.defaults;
			for (i in _ref) {
				_ = _ref[i];
				if (this.$el.data(i) != null) {
					this.options[i] = this.$el.data(i);
				}
			}
			this.createStars();
			this.syncRating();
			this.$el.on('mouseover.starrr', 'span', function(e) {
				return _this.syncRating(_this.$el.find('span').index(e.currentTarget) + 1);
			});
			this.$el.on('mouseout.starrr', function() {
				return _this.syncRating();
			});
			this.$el.on('click.starrr', 'span', function(e) {
				return _this.setRating(_this.$el.find('span').index(e.currentTarget) + 1);
			});
			this.$el.on('starrr:change', this.options.change);
		}

		Starrr.prototype.createStars = function() {
			var _i, _ref, _results;

			_results = [];
			for (_i = 1, _ref = this.options.numStars; 1 <= _ref ? _i <= _ref : _i >= _ref; 1 <= _ref ? _i++ : _i--) {
				_results.push(this.$el.append("<span class='glyphicon .glyphicon-star-empty'></span>"));
			}
			return _results;
		};

		Starrr.prototype.setRating = function(rating) {
			if (this.options.rating === rating) {
				rating = void 0;
			}
			this.options.rating = rating;
			this.syncRating();
			return this.$el.trigger('starrr:change', rating);
		};

		Starrr.prototype.syncRating = function(rating) {
			var i, _i, _j, _ref;

			rating || (rating = this.options.rating);
			if (rating) {
				for (i = _i = 0, _ref = rating - 1; 0 <= _ref ? _i <= _ref : _i >= _ref; i = 0 <= _ref ? ++_i : --_i) {
					this.$el.find('span').eq(i).removeClass('glyphicon-star-empty').addClass('glyphicon-star');
				}
			}
			if (rating && rating < 5) {
				for (i = _j = rating; rating <= 4 ? _j <= 4 : _j >= 4; i = rating <= 4 ? ++_j : --_j) {
					this.$el.find('span').eq(i).removeClass('glyphicon-star').addClass('glyphicon-star-empty');
				}
			}
			if (!rating) {
				return this.$el.find('span').removeClass('glyphicon-star').addClass('glyphicon-star-empty');
			}
		};

		return Starrr;

	})();

	return $.fn.extend({
		starrr: function() {
			var args, option;

			option = arguments[0], args = 2 <= arguments.length ? __slice.call(arguments, 1) : [];
			return this.each(function() {
				var data;

				data = $(this).data('star-rating');
				if (!data) {
					$(this).data('star-rating', (data = new Starrr($(this), option)));
				}
				if (typeof option === 'string') {
					return data[option].apply(data, args);
				}
			});
		}
	});
})(window.jQuery, window);

$(function() {
	return $(".starrr").starrr();
});


/***** DOWN HERE CODE FOR PARSING JSON RESPONSE **********/
function Auteur(id,login){
	this.id = id;
	this.login = login;
}

function Recette(id,auteur,titre,photo){
	this.id = id;
	this.auteur = auteur;
	this.titre = titre;
	this.photo = photo;
}

Ingredients.prototype.getHtml=function(){
	var s = "<div class='ingr'>";
	var tmp = "";
	for(...)
	"<ul><li><span class='txt-size-25'>poulet</span></li>"+
	"<li><span class='txt-size-25'>épices</span></li>"+	
	"<li><span class='txt-size-25'>sauce verte</span></li></ul>"+
	"</div>"
}

Recette.prototype.getHtml=function(){
	
}


$(document).ready(function() {

	$('#hearts').on('starrr:change', function(e, value){
		$('#count').html(value);
	});

});

