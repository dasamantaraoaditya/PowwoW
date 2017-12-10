/**
 * 
 */

(function() {
	'use strict';
	angular.module('myapp2App').factory('ChatBox', ChatBox);

	ChatBox.$inject = [ '$resource', 'DateUtils' ];

	function ChatBox($resource, DateUtils) {
		var resourceUrl = 'api/chat-box/:id';

		return $resource(resourceUrl, {}, {
			'query' : {
				method : 'GET',
				isArray : true
			},
			'get' : {
				method : 'GET',
				isArray : true
			},
			'sendMessage' : {
				method : 'POST',
				isArray : false
			},
			'update' : {
				method : 'PUT'
			}
		});
	}
})();
