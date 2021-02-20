Feature: Testing the Game Creation
  Background:
    Given url baseUrl

  Scenario: Creating Diff 1 With the left input
    Given path '/diff/1/left'
    And request 'eyJuYW1lIjoiUGF1LWJyYXNpbCIsInNwZWNpZXMiOiJQYXVicmFzaWxpYSBlY2hpbmF0YSJ9'
    When method PUT
    Then status 200