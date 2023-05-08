#Feature: the player substitute use case
#
#  Scenario: A player that is currently in the match gets substituted
#    Given a match that is currently taking place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    When player Ronaldo is substituted by player Messi
#    Then player Ronaldo is not playing in the match anymore
#    And player Messi is playing in the match
#
#  Scenario: A player that is not playing in the match gets substituted
#    Given a match that is currently taking place
#    And this match is between team1 and team2
#    When player Ronaldo is substituted by player Messi
#    Then an error is shown stating: Player is currently not in match
#    And the player Ronaldo is still not part of the match
#    And the player Messi is still not part of the match
#
#  Scenario: A player that was already substituted gets substituted again
#    Given a match that is currently taking place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    And player Ronaldo is substituted by player Messi
#    When player Ronaldo is substituted by player Messi
#    Then an error is shown stating: Player is currently not in match
#    And the player Ronaldo is still not part of the match
#    And the player Messi is still part of the match
#
#  Scenario: A player that is currently in the match gets substituted by another player that is currently in the match
#    Given a match that is currently taking place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    And team1 has a player Messi
#    When player Ronaldo is substituted by player Messi
#    Then an error is shown stating: Both players are already in the match
#    And player Ronaldo is still part of the match
#    And player Messi is still part of the match
#
#  Scenario: A player gets substituted while match has not started yet
#    Given a match that is still to be taken place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    When player Ronaldo is substituted by player Messi
#    Then an error is shown stating: Cannot add playerEvent since match is not active
#
#  Scenario: A player gets substituted after match has taken place
#    Given a match that has already taken place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    When player Ronaldo is substituted by player Messi
#    Then an error is shown stating: Cannot add playerEvent since match is not active