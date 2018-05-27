/**
 * @author aditya
 */

(function() {
	'use strict';
	angular.module('myapp2App').factory('Profile', Profile);

	Profile.$inject = [ '$resource', 'DateUtils' ];

	function Profile($resource, DateUtils) {
		var resourceUrl = 'api/userprofiles/:id';

		return $resource(resourceUrl, {}, {
			'query' : {
				method : 'GET',
				isArray : true
			},
			'get' : {
				method : 'GET',
				isArray : true
			},
			'getByUser':{
            	method: 'GET',
            	isArray : false,
            	url: 'api/userprofiles/user/:userId'
            },
			'update' : {
				method : 'PUT'
			}
		});
	}
})();
