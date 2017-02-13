(function() {
    'use strict';

    angular
        .module('amas2App')
        .controller('PersonnDeleteController',PersonnDeleteController);

    PersonnDeleteController.$inject = ['$uibModalInstance', 'entity', 'Personn'];

    function PersonnDeleteController($uibModalInstance, entity, Personn) {
        var vm = this;

        vm.personn = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Personn.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
