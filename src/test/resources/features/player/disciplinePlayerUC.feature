Feature: the player substitute use case

  Background:
    Given a match between team1 and team2
    And this match is currently taking place
    And team1 has a player Ronaldo
    And player Ronaldo is currently lined up

  Scenario Outline: A player that is currently in the match gets a valid card
    When player Ronaldo gets a <card_type> card at minute 0
    Then the event is added to the match
    And the match contains 2 events for player Ronaldo
    Examples:
      | card_type |
      | YELLOW    |
      | RED       |

  Scenario: A player that is not playing in the match gets a yellow card
    When a player that is not part of the match gets a YELLOW card at minute 0
    Then event is not added to the match because the player is not active in the match

  Scenario: A player that already had a yellow card gets another yellow card
    Given player Ronaldo got a YELLOW card at minute 0
    When player Ronaldo gets a YELLOW card at minute 1
    Then event is not added to the match because the player already had a yellow card
    And the match contains 2 events for player Ronaldo

  Scenario: A player that already had a yellow card gets a red card
    Given player Ronaldo got a YELLOW card at minute 0
    When player Ronaldo gets a RED card at minute 1
    Then the event is added to the match
    And the match contains 3 events for player Ronaldo

  Scenario: A player that already had taking red card gets another red card
    Given player Ronaldo got a RED card at minute 0
    When player Ronaldo gets a RED card at minute 1
    Then event is not added to the match because the player is not active in the match
    And the match contains 2 events for player Ronaldo

  Scenario: A player gets disciplined while match has not started yet
    When a player that is not part of the match gets a RED card at minute 0
    Then event is not added to the match because the player is not active in the match

  Scenario: A player gets disciplined after match has taken place
    Given this match has already ended
    And team1 has a player Ronaldo
    Given player Ronaldo got a RED card at minute 0
    Then event is not added to the match because the match is not active