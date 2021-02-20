Feature: Testing the Diff Creation Success Scenarios
  Background:
    Given url baseUrl
    #  "test"
    * def invalidJson = 'dGVzdA=='
    * def invalidBase64 = 'Invalid Base64 String'

  Scenario: Trying to create a diff with a invalid JSON
    Given path '/diff/1/left'
    And request invalidJson
    When method PUT
    Then status 400
    And match response == { type: 'IPT-002', title: 'The input is not a valid Json object', status: 400, detail: '#string', instance: '#notnull'}

  Scenario: Trying to create a diff with a invalid Base64 String
    Given path '/diff/1/right'
    And request invalidBase64
    When method PUT
    Then status 400
    And match response == { type: 'IPT-001', title: 'The input is not a valid base64 string', status: 400, detail: '#string', instance: '#notnull'}
