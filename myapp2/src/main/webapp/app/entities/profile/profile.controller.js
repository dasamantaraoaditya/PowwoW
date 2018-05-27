/**
 * @author aditya
 */

(function() {
	'use strict';

	angular.module('myapp2App').controller('ProfileController',
			ProfileController);

	ProfileController.$inject = [ 'Profile', '$scope', 'Friends', 'Principal' ];

	function ProfileController(Profile, $scope, Friends, Principal) {

		var vm = this;
		vm.Profile = [];
		getAccount();
		
		function getAccount() {
			Principal.identity().then(function(account) {
				vm.account = account;
				vm.isAuthenticated = Principal.isAuthenticated;
				getUserProfile();
			});
		}

		function getUserProfile() {
			Profile.getByUser({userId : vm.account.id},function(result) {
				vm.Profile = result;
				vm.searchQuery = null;
			});
		}
	}
})();
