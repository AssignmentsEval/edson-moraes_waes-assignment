Feature: Diff Success Secanrios

  Background:
    Given url baseUrl

  Scenario Outline: Scenario <diffId> - <name>
    Given path '/diff/<diffId>/left'
    And request <textLeft>
    When method PUT

    Given path '/diff/<diffId>/right'
    And request <textRight>
    When method PUT

    Given path '/diff/<diffId>'
    When method GET
    Then status 200
    And response.diffId == '<diffID>'
    And assert response.stringDiff.equal == <textEqual>
    And assert response.stringDiff.stringDifferences.length == <stringDifferences>
    And assert response.jsonDiff.equal == <jsonEqual>
    And assert response.jsonDiff.jsonDifferences.length == <jsonDifferences>

    Examples:
      | diffId | textLeft       | textRight              | textEqual | jsonEqual | stringDifferences | jsonDifferences | name                               |
      | 1      | 'eyJhIjoiYiJ9' | 'eyJhIjoiYiJ9'         | true      | true      | 0                 | 0               | Equal                              |
      | 2      | 'eyJhIjoieiJ9' | 'eyJhIjoiYiJ9'         | false     | false     | 1                 | 1               | Not Equal                          |
      | 3      | 'eyJhIjoiYiJ9' | 'eyAiYSIgOiAiYiIgfQ==' | true      | true      | 0                 | 0               | Equal, Right Input With WhiteSpace |

#  'eyJhIjoiYiJ9' -> {"a":"b"}
#  'eyJhIjoieiJ9' -> {"a":"z"}
#  'eyAiYSIgOiAiYiIgfQ==' -> { "a" : "b" }
