Feature: the player is injured use case

  Background:
    Given a match between team1 and team2
    And team1 has a player Ronaldo
    And this match is currently taking place
    And player Ronaldo is currently lined up

  Scenario: A player that is currently in the match gets lined up again
    When player Ronaldo gets lined up again
    Then event is not added to the match because the line up is not allowed

  Scenario: A player that is not part of the teams that are playing in the match gets lined up
    When player that is not part of both teams gets lined up
    Then event is not added to the match because the line up is not allowed

  Scenario: A player is lined up after match has taken place
    Given this match has already ended
    When player Ronaldo gets lined up again
    Then event is not added to the match because the line up is not allowed