define(["angular"], function(angular) {
  "use strict";

  return {
    LoginCtrl: function($scope, $cookies, $http, $q, $timeout) {
      // This is only for demo purposes
      $scope.credentials = {
        email: "jeffusan@atware.jp",
        password: "secret"
      };

      // Check token cookie and try to authenticate
      // Otherwise the account has to log in
      var token = $cookies["XSRF-TOKEN"];
      if (token) {
        $http.get("/ping")
        .then(
          function(response) {
            // Token valid, fetch account data
            return $http.get("/accounts/" + response.data.accountId);
          },
          function(response) {
            token = undefined;
            $cookies["XSRF-TOKEN"] = undefined;
            return $q.reject("Token invalid");
          }
        ).then(
          function(response) {
            $scope.account = response.data;
          }, function(response) {
            // Token invalid or fetching the account failed
          }
        );
      }

      /**
       * Login using the given credentials as (email,password).
       * The server adds the XSRF-TOKEN cookie which is then picked up by Play.
       */
      $scope.login = function(credentials) {
        $http.post("/login", credentials)
        .then(
          function(response) { // success
            token = response.data.token;
            var accountId = response.data.accountId;
            return $http.get("/accounts/" + accountId); // return another promise to chain `then`
          }, function(response) { // error
            $scope.error = response.data.err;
            // return 'empty' promise so the right `then` function is called
            return $q.reject("Login failed");
          }
        )
        .then(
          function(response) {
            $scope.account = response.data;
          },
          function(response) {
            console.log(response);
          }
        );
      };

      /**
       * Invalidate the token on the server.
       */
      $scope.logout = function() {
        $http.post("/logout").then(function() {
          $scope.account = undefined;
        });
      };

      /**
       * Pings the server. When the request is not authorized, the $http interceptor should
       * log out the current account.
       * When using routes, this is not necessary.
       */
      $scope.ping = function() {
        $http.get("/ping").then(function() {
          $scope.ok = true;
          $timeout(function() {$scope.ok = false;}, 3000);
        });
      };

      /** Subscribe to the logout event emitted by the $http interceptor. */
      $scope.$on("InvalidToken", function() {
        console.log("InvalidToken!");
        $scope.account = undefined;
      });
    }
  };
});
