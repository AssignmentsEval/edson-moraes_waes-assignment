Feature: Testing the Diff Creation Success Scenarios
  Background:
    Given url baseUrl
    #  {"name":"Pau-brasil","species":"Paubrasilia echinata"}
    * def validInput1 = 'eyJuYW1lIjoiUGF1LWJyYXNpbCIsInNwZWNpZXMiOiJQYXVicmFzaWxpYSBlY2hpbmF0YSJ9'
    #  {"name":"Ebony","species":"Diospyros ebanun"}
    * def validInput2 = 'eyJuYW1lIjoiRWJvbnkiLCJzcGVjaWVzIjoiRGlvc3B5cm9zIGViYW51biJ9'
    #  {"name":"Oak","species":"Quercus robur"}
    * def validInput3 = 'eyJuYW1lIjoiT2FrIiwic3BlY2llcyI6IlF1ZXJjdXMgcm9idXIifQ=='

  Scenario: Creating Diff 1 With the left input
    Given path '/diff/1/left'
    And request validInput1
    When method PUT
    Then status 200
    And match response == { id: 1, leftJson: '#string', rightJson: '#null'}


  Scenario: Creating Diff 2 With the right input
    Given path '/diff/2/right'
    And request validInput1
    When method PUT
    Then status 200
    And match response == { id: 2, leftJson: '#null', rightJson: '#string'}

  Scenario: Adding Right Text to diff 1
    Given path '/diff/1/right'
    And request validInput3
    When method PUT
    Then status 200
    And match response == { id: 1, leftJson: '#string', rightJson: '#string'}

  Scenario: Adding Left Text to diff 2
    Given path '/diff/2/left'
    And request validInput3
    When method PUT
    Then status 200
    And match response == { id: 2, leftJson: '#string', rightJson: '#string'}
