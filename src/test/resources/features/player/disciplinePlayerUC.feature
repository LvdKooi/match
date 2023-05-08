Feature: the player substitute use case

  Scenario: A player that is currently in the match gets a yellow card
    Given a match between team1 and team2
    And this match is currently taking place
    And team1 has a player Ronaldo
    When player Ronaldo gets a yellow card at minute 0
    Then the request is handled successfully

#  Scenario: A player that is currently in the match gets a red card
#    Given a match that is currently taking place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    When player Ronaldo gets a red card
#    Then player Ronaldo is not part of the match anymore
#
#  Scenario: A player that is not playing in the match gets a yellow card
#    Given a match that is currently taking place
#    And this match is between team1 and team2
#    When player Ronaldo gets a yellow card
#    Then an error is shown stating: Player is currently not in match
#    And the player Ronaldo is still not part of the match
#
#  Scenario: A player that already had a yellow card gets another yellow card
#    Given a match that is currently taking place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    And player Ronaldo got a yellow card
#    When player Ronaldo gets a yellow card
#    Then an error is shown stating: Player already had a yellow card
#    Then player Ronaldo is still part of the match
#
#  Scenario: A player that already had a yellow card gets a red card
#    Given a match that is currently taking place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    And player Ronaldo got a yellow card
#    When player Ronaldo gets a red card
#    Then player Ronaldo is not part of the match anymore
#
#  Scenario: A player that already had taking red card gets another red card
#    Given a match that is currently taking place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    And player Ronaldo got a red card
#    When player Ronaldo gets a red card
#    Then an error is shown stating: Player is currently not in match
#    And the player Ronaldo is still not part of the match
#
#  Scenario: A player gets disciplined while match has not started yet
#    Given a match that is still to be taken place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    When player Ronaldo gets a red card
#    Then an error is shown stating: Cannot add playerEvent since match is not active
#
#  Scenario: A player gets disciplined after match has taken place
#    Given a match that has already taken place
#    And this match is between team1 and team2
#    And team1 has a player Ronaldo
#    When player Ronaldo gets a yellow card
#    Then an error is shown stating: Cannot add playerEvent since match is not active