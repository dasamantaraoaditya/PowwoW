(function() {
    'use strict';

    angular
        .module('myapp2App')
        .controller('UserprofileDialogController', UserprofileDialogController);

    UserprofileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Userprofile', 'User'];

    function UserprofileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Userprofile, User) {
        var vm = this;

        vm.userprofile = entity;
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
            if (vm.userprofile.id !== null) {
                Userprofile.update(vm.userprofile, onSaveSuccess, onSaveError);
            } else {
                Userprofile.save(vm.userprofile, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myapp2App:userprofileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateofbirth = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
