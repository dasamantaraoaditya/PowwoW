/**
 * @author aditya
 */

(function() {
	'use strict';

	angular.module('myapp2App').controller('ChatBoxController',
			ChatBoxController);

	ChatBoxController.$inject = [ 'ChatBox', '$scope', 'ChatRoom', 'Principal' ];

	function ChatBoxController(ChatBox, $scope, ChatRoom, Principal) {

		var vm = this;
		vm.chatBox = [];
		vm.chatRoom = [];
		vm.messages = [];
		getAccount();

		function getAccount() {
			Principal.identity().then(function(account) {
				vm.account = account;
				vm.isAuthenticated = Principal.isAuthenticated;
			});
		}

		loadAll();

		function loadAll() {
			ChatBox.query(function(result) {
				vm.chatBox = result;
				vm.searchQuery = null;
			});
		}

		$scope.getMessages = function(contactId) {
			ChatBox.get({
				id : contactId
			}, function(result) {
				vm.messages = result;
				vm.searchQuery = null;
				vm.contactId = contactId;
			});
		};
		$scope.sendMessage = function(event) {
			if (vm.messageToBeSent && (event.which === 13 || event.which === 1)) {
				var messageData = {
					id : null,
					is_read : false,
					is_visible_to_reciver : true,
					is_visible_to_sender : true,
					message : vm.messageToBeSent,
					sent_from : {
						id : vm.account.id
					},
					sent_on : new Date(),
					sent_to : {
						id : vm.contactId
					}
				};
				vm.messageToBeSent = '';
				ChatBox.sendMessage(messageData, function(result) {
					vm.messages.push(messageData);
				});
			}
		};
	}
})();
