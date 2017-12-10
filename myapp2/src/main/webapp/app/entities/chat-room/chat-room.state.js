(function() {
    'use strict';

    angular
        .module('myapp2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('chat-room', {
            parent: 'entity',
            url: '/chat-room',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ChatRooms'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chat-room/chat-rooms.html',
                    controller: 'ChatRoomController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('chat-room-detail', {
            parent: 'chat-room',
            url: '/chat-room/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ChatRoom'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chat-room/chat-room-detail.html',
                    controller: 'ChatRoomDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ChatRoom', function($stateParams, ChatRoom) {
                    return ChatRoom.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'chat-room',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('chat-room-detail.edit', {
            parent: 'chat-room-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chat-room/chat-room-dialog.html',
                    controller: 'ChatRoomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChatRoom', function(ChatRoom) {
                            return ChatRoom.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chat-room.new', {
            parent: 'chat-room',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chat-room/chat-room-dialog.html',
                    controller: 'ChatRoomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                message: null,
                                sent_on: null,
                                is_visible_to_sender: false,
                                is_visible_to_reciver: false,
                                is_read: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('chat-room', null, { reload: 'chat-room' });
                }, function() {
                    $state.go('chat-room');
                });
            }]
        })
        .state('chat-room.edit', {
            parent: 'chat-room',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chat-room/chat-room-dialog.html',
                    controller: 'ChatRoomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChatRoom', function(ChatRoom) {
                            return ChatRoom.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chat-room', null, { reload: 'chat-room' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chat-room.delete', {
            parent: 'chat-room',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chat-room/chat-room-delete-dialog.html',
                    controller: 'ChatRoomDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ChatRoom', function(ChatRoom) {
                            return ChatRoom.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chat-room', null, { reload: 'chat-room' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
