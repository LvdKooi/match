#Feature: the player is injured use case
#
#  Scenario: A player that is currently in the match gets injured
#    Given a match that is currently taking place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    When player Ronaldo becomes injured
#    Then player Ronaldo is not playing in the match anymore
#
#  Scenario: A player that is not playing in the match gets injured
#    Given a match that is currently taking place
#    And this match is between team1 and team2
#    When  player Ronaldo becomes injured
#    Then an error is shown stating: Player is currently not in match
#    And the player is still not part of the match
#
#  Scenario: A player that is was already injured before gets injured again
#    Given a match that is currently taking place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    And player Ronaldo became injured
#    When player Ronaldo becomes injured again
#    Then an error is shown stating: Player is currently not in match
#    And the player is still not part of the match
#
#  Scenario: A player becomes injured while match has not started yet
#    Given a match that is still to be taken place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    When player Ronaldo became injured
#    Then an error is shown stating: Cannot add playerEvent since match is not active
#
#  Scenario: A player becomes injured after match has taken place
#    Given a match that has already taken place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    When player Ronaldo became injured
#    Then an error is shown stating: Cannot add playerEvent since match is not active