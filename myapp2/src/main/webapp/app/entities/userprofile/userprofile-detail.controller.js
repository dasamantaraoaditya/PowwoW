(function() {
    'use strict';

    angular
        .module('myapp2App')
        .controller('UserprofileDetailController', UserprofileDetailController);

    UserprofileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Userprofile', 'User'];

    function UserprofileDetailController($scope, $rootScope, $stateParams, previousState, entity, Userprofile, User) {
        var vm = this;

        vm.userprofile = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myapp2App:userprofileUpdate', function(event, result) {
            vm.userprofile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
