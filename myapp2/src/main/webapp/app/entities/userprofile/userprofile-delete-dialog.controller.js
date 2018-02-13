(function() {
    'use strict';

    angular
        .module('myapp2App')
        .controller('UserprofileDeleteController',UserprofileDeleteController);

    UserprofileDeleteController.$inject = ['$uibModalInstance', 'entity', 'Userprofile'];

    function UserprofileDeleteController($uibModalInstance, entity, Userprofile) {
        var vm = this;

        vm.userprofile = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Userprofile.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
