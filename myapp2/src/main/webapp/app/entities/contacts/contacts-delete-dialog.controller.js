(function() {
    'use strict';

    angular
        .module('myapp2App')
        .controller('ContactsDeleteController',ContactsDeleteController);

    ContactsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Contacts'];

    function ContactsDeleteController($uibModalInstance, entity, Contacts) {
        var vm = this;

        vm.contacts = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Contacts.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
