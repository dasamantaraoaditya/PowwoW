/**
 * @author aditya
 */

(function() {
    'use strict';
    angular
        .module('myapp2App')
        .factory('Friends', Friends);

    Friends.$inject = ['$resource', 'DateUtils'];

    function Friends ($resource, DateUtils) {
        var resourceUrl =  'api/friends/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.created_on = DateUtils.convertDateTimeFromServer(data.created_on);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'search':{
            	method: 'GET',
            	isArray : true,
            	url: 'api/friends/search/:searchtext'
            },
            'sendFriendRequest':{
            	method: 'POST',
            	isArray: false,
            	url: 'api/friends/addFriend'
            },
            'cancelFriendRequest':{
            	method: 'DELETE',
            	isArray: false,
            	url: 'api/friends/cancelRequest/:id'
            },
            'acceptFriendRequest':{
            	method: 'GET',
            	isArray: false,
            	url: 'api/friends/acceptRequest/:id'
            }
        });
    }
})();
