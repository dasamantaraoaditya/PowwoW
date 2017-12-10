(function() {
    'use strict';

    angular
        .module('myapp2App')
        .controller('ChatRoomDialogController', ChatRoomDialogController);

    ChatRoomDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ChatRoom', 'User'];

    function ChatRoomDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ChatRoom, User) {
        var vm = this;

        vm.chatRoom = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.chatRoom.id !== null) {
                ChatRoom.update(vm.chatRoom, onSaveSuccess, onSaveError);
            } else {
                ChatRoom.save(vm.chatRoom, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myapp2App:chatRoomUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.sent_on = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
