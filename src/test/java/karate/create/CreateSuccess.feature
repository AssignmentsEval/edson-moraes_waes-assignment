Feature: Testing the Diff Creation Success Scenarios
  Background:
    Given url baseUrl
    * def validInput1 = 'eyJuYW1lIjoiUGF1LWJyYXNpbCIsInNwZWNpZXMiOiJQYXVicmFzaWxpYSBlY2hpbmF0YSJ9'
    * def validInput1String = '{\"name\":\"Pau-brasil\",\"species\":\"Paubrasilia echinata\"}'

    * def validInput2 = 'eyJuYW1lIjoiRWJvbnkiLCJzcGVjaWVzIjoiRGlvc3B5cm9zIGViYW51biJ9'
    * def validInput2String = '{\"name\":\"Ebony\",\"species\":\"Diospyros ebanun\"}'

    * def validInput3 = 'eyJuYW1lIjoiT2FrIiwic3BlY2llcyI6IlF1ZXJjdXMgcm9idXIifQ=='
    * def validInput3String = '{\"name\":\"Oak\",\"species\":\"Quercus robur\"}'

  Scenario: Creating Diff 1 With the left input
    Given path '/diff/1/left'
    And request validInput1
    When method PUT
    Then status 200
    And match response == { id: 1, leftJson: '#(validInput1String)', rightJson: '#null'}


  Scenario: Creating Diff 2 With the right input
    Given path '/diff/2/right'
    And request validInput1
    When method PUT
    Then status 200
    And match response == { id: 2, leftJson: '#null', rightJson: '#(validInput1String)'}

  Scenario: Adding Right Text to diff 1
    Given path '/diff/1/right'
    And request validInput2
    When method PUT
    Then status 200
    And match response == { id: 1, leftJson: '#(validInput1String)', rightJson: '#(validInput2String)'}

  Scenario: Adding Left Text to diff 2
    Given path '/diff/2/left'
    And request validInput3
    When method PUT
    Then status 200
    And match response == { id: 2, leftJson: '#(validInput3String)', rightJson: '#(validInput1String)'}
