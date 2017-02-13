(function() {
    'use strict';

    angular
        .module('amas2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('personn', {
            parent: 'entity',
            url: '/personn',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amas2App.personn.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/personn/personns.html',
                    controller: 'PersonnController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personn');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('personn-detail', {
            parent: 'entity',
            url: '/personn/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amas2App.personn.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/personn/personn-detail.html',
                    controller: 'PersonnDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personn');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Personn', function($stateParams, Personn) {
                    return Personn.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'personn',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('personn-detail.edit', {
            parent: 'personn-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personn/personn-dialog.html',
                    controller: 'PersonnDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Personn', function(Personn) {
                            return Personn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('personn.new', {
            parent: 'personn',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personn/personn-dialog.html',
                    controller: 'PersonnDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('personn', null, { reload: 'personn' });
                }, function() {
                    $state.go('personn');
                });
            }]
        })
        .state('personn.edit', {
            parent: 'personn',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personn/personn-dialog.html',
                    controller: 'PersonnDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Personn', function(Personn) {
                            return Personn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('personn', null, { reload: 'personn' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('personn.delete', {
            parent: 'personn',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personn/personn-delete-dialog.html',
                    controller: 'PersonnDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Personn', function(Personn) {
                            return Personn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('personn', null, { reload: 'personn' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
