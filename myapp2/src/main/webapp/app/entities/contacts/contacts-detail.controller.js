(function() {
    'use strict';

    angular
        .module('myapp2App')
        .controller('ContactsDetailController', ContactsDetailController);

    ContactsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Contacts', 'User'];

    function ContactsDetailController($scope, $rootScope, $stateParams, previousState, entity, Contacts, User) {
        var vm = this;

        vm.contacts = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myapp2App:contactsUpdate', function(event, result) {
            vm.contacts = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
