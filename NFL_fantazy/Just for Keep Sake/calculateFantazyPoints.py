import DataRetrieval
import PointSystem

offense_data = DataRetrieval.filtered_data
offense_scoring = PointSystem.FantasyScoringOffense()
defensive_scoring = PointSystem.FantasyScoringDefense()

# Example lineup of player names
lineup = ['Player A', 'Player B']

player_scores = {}

for player_name in lineup:
    # Grab the row for this player
    player_row = offense_data[offense_data['player_name'] == player_name].iloc[0]
    
    # Calculate points for this player
    player_score = PointSystem.calculate_offensive_player_pts(player_row, offense_scoring)
    
    player_scores[player_name] = player_score

print(player_scores)
