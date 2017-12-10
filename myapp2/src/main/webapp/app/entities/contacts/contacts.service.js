(function() {
    'use strict';
    angular
        .module('myapp2App')
        .factory('Contacts', Contacts);

    Contacts.$inject = ['$resource', 'DateUtils'];

    function Contacts ($resource, DateUtils) {
        var resourceUrl =  'api/contacts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.created_on = DateUtils.convertDateTimeFromServer(data.created_on);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
