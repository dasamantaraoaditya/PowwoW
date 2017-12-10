(function() {
    'use strict';
    angular
        .module('myapp2App')
        .factory('ChatRoom', ChatRoom);

    ChatRoom.$inject = ['$resource', 'DateUtils'];

    function ChatRoom ($resource, DateUtils) {
        var resourceUrl =  'api/chat-rooms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.sent_on = DateUtils.convertDateTimeFromServer(data.sent_on);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
