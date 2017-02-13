(function() {
    'use strict';

    angular
        .module('amas2App')
        .controller('AssoDialogController', AssoDialogController);

    AssoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Asso', 'Member'];

    function AssoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Asso, Member) {
        var vm = this;

        vm.asso = entity;
        vm.clear = clear;
        vm.save = save;
        vm.members = Member.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.asso.id !== null) {
                Asso.update(vm.asso, onSaveSuccess, onSaveError);
            } else {
                Asso.save(vm.asso, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('amas2App:assoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
