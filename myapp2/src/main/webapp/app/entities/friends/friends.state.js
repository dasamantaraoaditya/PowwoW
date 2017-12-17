/**
 * @author aditya
 */

(function() {
	'use strict';

	angular.module('myapp2App').config(stateConfig);

	stateConfig.$inject = [ '$stateProvider' ];

	function stateConfig($stateProvider) {
		$stateProvider.state('friends', {
			parent : 'entity',
			url : '/friends',
			data : {
				authorities : [ 'ROLE_USER' ],
				pageTitle : 'Friends'
			},
			views : {
				'content@' : {
					templateUrl : 'app/entities/friends/friends.html',
					controller : 'FriendsController',
					controllerAs : 'vm'
				}
			},
			resolve : {}
		})
	}
})();
