(function() {
    'use strict';

    angular
        .module('amas2App')
        .controller('AssoDetailController', AssoDetailController);

    AssoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Asso', 'Member'];

    function AssoDetailController($scope, $rootScope, $stateParams, previousState, entity, Asso, Member) {
        var vm = this;

        vm.asso = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('amas2App:assoUpdate', function(event, result) {
            vm.asso = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
