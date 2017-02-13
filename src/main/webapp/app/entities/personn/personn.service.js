(function() {
    'use strict';
    angular
        .module('amas2App')
        .factory('Personn', Personn);

    Personn.$inject = ['$resource'];

    function Personn ($resource) {
        var resourceUrl =  'api/personns/:id';

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
