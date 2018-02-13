(function() {
    'use strict';

    angular
        .module('myapp2App')
        .controller('UserprofileController', UserprofileController);

    UserprofileController.$inject = ['Userprofile'];

    function UserprofileController(Userprofile) {

        var vm = this;

        vm.userprofiles = [];

        loadAll();

        function loadAll() {
            Userprofile.query(function(result) {
                vm.userprofiles = result;
                vm.searchQuery = null;
            });
        }
    }
})();
