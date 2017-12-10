(function() {
    'use strict';

    angular
        .module('myapp2App')
        .controller('ContactsController', ContactsController);

    ContactsController.$inject = ['Contacts'];

    function ContactsController(Contacts) {

        var vm = this;

        vm.contacts = [];

        loadAll();

        function loadAll() {
            Contacts.query(function(result) {
                vm.contacts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
