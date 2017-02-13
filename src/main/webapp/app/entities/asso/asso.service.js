(function() {
    'use strict';
    angular
        .module('amas2App')
        .factory('Asso', Asso);

    Asso.$inject = ['$resource'];

    function Asso ($resource) {
        var resourceUrl =  'api/assos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
