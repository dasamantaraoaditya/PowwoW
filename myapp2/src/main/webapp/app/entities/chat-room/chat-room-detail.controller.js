(function() {
    'use strict';

    angular
        .module('myapp2App')
        .controller('ChatRoomDetailController', ChatRoomDetailController);

    ChatRoomDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChatRoom', 'User'];

    function ChatRoomDetailController($scope, $rootScope, $stateParams, previousState, entity, ChatRoom, User) {
        var vm = this;

        vm.chatRoom = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myapp2App:chatRoomUpdate', function(event, result) {
            vm.chatRoom = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
