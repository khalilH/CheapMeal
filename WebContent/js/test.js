	
var app = angular.module('app', ['ngRoute']);

app.config(function($routeProvider){
	$routeProvider
	.when('/',{templateUrl: 'Test2.html'})
	.when('/comments',{templateUrl : 'Test3.html'})
	.otherwise({redirectTo : '/'})
});

app.controller('commentController',function ($scope){
	console.log($scope);
	$scope.comments = [
	{
		"firstname": "Cannon",
		"city": "Irwin"
	},
	{
		"firstname": "Joy",
		"city": "Trucksville"
	},
	{
		"firstname": "Gloria",
		"city": "Graball"
	},
	{
		"firstname": "Rivera",
		"city": "Craig"
	},
	{
		"firstname": "Moreno",
		"city": "Lewis"
	},
	{
		"firstname": "Landry",
		"city": "Caron"
	},
	{
		"firstname": "Duncan",
		"city": "Southmont"
	},
	{
		"firstname": "Farley",
		"city": "Alfarata"
	},
	{
		"firstname": "Finch",
		"city": "Kiskimere"
	},
	{
		"firstname": "Leanna",
		"city": "Enoree"
	}
	];

});
