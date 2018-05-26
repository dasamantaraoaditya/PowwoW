/**
 * 
 */

(function() {
	'use strict';

	angular.module('myapp2App').config(stateConfig);

	stateConfig.$inject = [ '$stateProvider' ];

	function stateConfig($stateProvider) {
		$stateProvider.state('profile', {
			parent : 'entity',
			url : '/profile',
			data : {
				authorities : [ 'ROLE_USER' ],
				pageTitle : 'Profile'
			},
			views : {
				'content@' : {
					templateUrl : 'app/entities/profile/profile.html',
					controller : 'ProfileController',
					controllerAs : 'vm'
				}
			},
			resolve : {}
		})
	}
})();
