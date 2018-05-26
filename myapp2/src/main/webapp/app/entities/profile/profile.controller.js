/**
 * @author aditya
 */

(function() {
	'use strict';

	angular.module('myapp2App').controller('ProfileController',
			ProfileController);

	ProfileController.$inject = [ 'Profile', '$scope', 'ChatRoom', 'Principal' ];

	function ProfileController(Profile, $scope, ChatRoom, Principal) {

		var vm = this;
		vm.Profile = [];
		getAccount();
		
		function getAccount() {
			Principal.identity().then(function(account) {
				vm.account = account;
				vm.isAuthenticated = Principal.isAuthenticated;
			});
		}

		loadAll();

		function loadAll() {
			Profile.query(function(result) {
				vm.Profile = result;
				vm.searchQuery = null;
			});
		}
	}
})();
