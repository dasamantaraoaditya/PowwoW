/**
 * @author aditya
 */

(function() {
	'use strict';
	angular.module('myapp2App').factory('Profile', Profile);

	Profile.$inject = [ '$resource', 'DateUtils' ];

	function Profile($resource, DateUtils) {
		var resourceUrl = 'api/profile/:id';

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
