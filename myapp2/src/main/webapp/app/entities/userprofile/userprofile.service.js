(function() {
    'use strict';
    angular
        .module('myapp2App')
        .factory('Userprofile', Userprofile);

    Userprofile.$inject = ['$resource', 'DateUtils'];

    function Userprofile ($resource, DateUtils) {
        var resourceUrl =  'api/userprofiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateofbirth = DateUtils.convertLocalDateFromServer(data.dateofbirth);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateofbirth = DateUtils.convertLocalDateToServer(copy.dateofbirth);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateofbirth = DateUtils.convertLocalDateToServer(copy.dateofbirth);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
