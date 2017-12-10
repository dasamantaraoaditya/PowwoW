(function() {
    'use strict';

    angular
        .module('myapp2App')
        .controller('ChatRoomDeleteController',ChatRoomDeleteController);

    ChatRoomDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChatRoom'];

    function ChatRoomDeleteController($uibModalInstance, entity, ChatRoom) {
        var vm = this;

        vm.chatRoom = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ChatRoom.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
