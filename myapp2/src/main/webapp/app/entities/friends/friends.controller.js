/**
 * @author aditya
 */

(function() {
	'use strict';

	angular.module('myapp2App').controller('FriendsController',
			FriendsController);

	FriendsController.$inject = [ 'Friends', '$scope' ];

	function FriendsController(Friends, $scope) {

		var vm = this;

		vm.friends = [];

		function loadAll() {
			Friends.query(function(result) {
				vm.contacts = result;
				vm.searchQuery = null;
			});
		}

		$scope.getMyFriends = function(params) {
			loadAll();
		};

		$scope.searchAllContactsAndUsers = function(searchText) {
			if (searchText && searchText.trim()) {
				Friends.search({
					searchtext : searchText
				}, function(result) {
					vm.contacts = result;
					vm.searchQuery = null;
				});
			} else {
				vm.contacts = [];
			}
		};

		$scope.sendFriendRequest = function(friendContact) {
			friendContact.status = 'REQUEST_SENT';
			friendContact.created_on = new Date();
			Friends.sendFriendRequest(friendContact, function(result) {
				friendContact = result;
			});
		};

		$scope.cancelFriendRequest = function(friendContact) {
			Friends.cancelFriendRequest({
				id : friendContact.contact.id
			}, function(result) {
				friendContact.status = '';
			});
		};

		$scope.acceptFriendRequest = function(friendContact) {
			Friends.acceptFriendRequest({
				id : friendContact.contact.id
			}, function(result) {
				friendContact.status = 'FRIEND';
			});
		};

	}
})();
