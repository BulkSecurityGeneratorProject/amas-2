(function() {
    'use strict';

    angular
        .module('amas2App')
        .controller('PersonnDialogController', PersonnDialogController);

    PersonnDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Personn'];

    function PersonnDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Personn) {
        var vm = this;

        vm.personn = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.personn.id !== null) {
                Personn.update(vm.personn, onSaveSuccess, onSaveError);
            } else {
                Personn.save(vm.personn, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('amas2App:personnUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
