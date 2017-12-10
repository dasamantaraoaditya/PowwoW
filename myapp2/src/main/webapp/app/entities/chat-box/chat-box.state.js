/**
 * 
 */

(function() {
	'use strict';

	angular.module('myapp2App').config(stateConfig);

	stateConfig.$inject = [ '$stateProvider' ];

	function stateConfig($stateProvider) {
		$stateProvider.state('chatbox', {
			parent : 'entity',
			url : '/chat-box',
			data : {
				authorities : [ 'ROLE_USER' ],
				pageTitle : 'ChatBox'
			},
			views : {
				'content@' : {
					templateUrl : 'app/entities/chat-box/chat-boxs.html',
					controller : 'ChatBoxController',
					controllerAs : 'vm'
				}
			},
			resolve : {}
		})
	}
})();
