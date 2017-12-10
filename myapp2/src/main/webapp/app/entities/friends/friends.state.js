/**
 * @author aditya
 *
 */

(function() {
    'use strict';

    angular
        .module('myapp2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('friends', {
            parent: 'entity',
            url: '/contacts',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Contacts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contacts/contacts.html',
                    controller: 'ContactsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('contacts-detail', {
            parent: 'contacts',
            url: '/contacts/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Contacts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contacts/contacts-detail.html',
                    controller: 'ContactsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Contacts', function($stateParams, Contacts) {
                    return Contacts.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'contacts',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('contacts-detail.edit', {
            parent: 'contacts-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contacts/contacts-dialog.html',
                    controller: 'ContactsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contacts', function(Contacts) {
                            return Contacts.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contacts.new', {
            parent: 'contacts',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contacts/contacts-dialog.html',
                    controller: 'ContactsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                created_on: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('contacts', null, { reload: 'contacts' });
                }, function() {
                    $state.go('contacts');
                });
            }]
        })
        .state('contacts.edit', {
            parent: 'contacts',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contacts/contacts-dialog.html',
                    controller: 'ContactsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contacts', function(Contacts) {
                            return Contacts.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contacts', null, { reload: 'contacts' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contacts.delete', {
            parent: 'contacts',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contacts/contacts-delete-dialog.html',
                    controller: 'ContactsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Contacts', function(Contacts) {
                            return Contacts.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contacts', null, { reload: 'contacts' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
