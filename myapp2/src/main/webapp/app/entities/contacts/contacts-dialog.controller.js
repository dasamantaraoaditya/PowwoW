(function() {
    'use strict';

    angular
        .module('myapp2App')
        .controller('ContactsDialogController', ContactsDialogController);

    ContactsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Contacts', 'User'];

    function ContactsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Contacts, User) {
        var vm = this;

        vm.contacts = entity;
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
            if (vm.contacts.id !== null) {
                Contacts.update(vm.contacts, onSaveSuccess, onSaveError);
            } else {
                Contacts.save(vm.contacts, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myapp2App:contactsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.created_on = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
