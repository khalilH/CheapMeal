	
var app = angular.module('app', ['ngRoute']);

app.config(function($routeProvider){
	$routeProvider
	.when('/',{templateUrl: 'Test2.html',controller:'PostsController'})
	.when('/comments/:id',{templateUrl : 'Test3.html',controller :'commentController'})
	.otherwise({redirectTo : '/'})
});

app.controller('PostsController',function ($scope){
	console.log($scope);
	$scope.companies = [
		  {
		    "id": 0,
		    "company": "Animalia",
		    "comments": [
		      {
		        "id": 0,
		        "name": "Daisy Everett"
		      },
		      {
		        "id": 1,
		        "name": "Benton Mayo"
		      },
		      {
		        "id": 2,
		        "name": "May Moses"
		      }
		    ]
		  },
		  {
		    "id": 1,
		    "company": "Nurplex",
		    "comments": [
		      {
		        "id": 0,
		        "name": "Carrillo Mcbride"
		      },
		      {
		        "id": 1,
		        "name": "Eula Mcclain"
		      },
		      {
		        "id": 2,
		        "name": "Valdez Wall"
		      }
		    ]
		  },
		  {
		    "id": 2,
		    "company": "Surelogic",
		    "comments": [
		      {
		        "id": 0,
		        "name": "Mason Mays"
		      },
		      {
		        "id": 1,
		        "name": "Concetta Burris"
		      },
		      {
		        "id": 2,
		        "name": "Flossie Hall"
		      }
		    ]
		  },
		  {
		    "id": 3,
		    "company": "Valreda",
		    "comments": [
		      {
		        "id": 0,
		        "name": "Sweeney Mathis"
		      },
		      {
		        "id": 1,
		        "name": "Sharlene Floyd"
		      },
		      {
		        "id": 2,
		        "name": "Ellis Christensen"
		      }
		    ]
		  },
		  {
		    "id": 4,
		    "company": "Dancity",
		    "comments": [
		      {
		        "id": 0,
		        "name": "Jerri Nielsen"
		      },
		      {
		        "id": 1,
		        "name": "Kerry Rollins"
		      },
		      {
		        "id": 2,
		        "name": "Mcdaniel Fulton"
		      }
		    ]
		  },
		  {
		    "id": 5,
		    "company": "Globoil",
		    "comments": [
		      {
		        "id": 0,
		        "name": "Crane Chang"
		      },
		      {
		        "id": 1,
		        "name": "Brock Ayala"
		      },
		      {
		        "id": 2,
		        "name": "Rocha Hayden"
		      }
		    ]
		  },
		  {
		    "id": 6,
		    "company": "Rubadub",
		    "comments": [
		      {
		        "id": 0,
		        "name": "Chan Wilder"
		      },
		      {
		        "id": 1,
		        "name": "Nunez Figueroa"
		      },
		      {
		        "id": 2,
		        "name": "Battle Thomas"
		      }
		    ]
		  }
	];

});

app.controller('commentController',function ($scope){
	console.log($scope);

});
