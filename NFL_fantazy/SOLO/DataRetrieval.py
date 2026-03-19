import nfl_data_py as nfl
import pandas as pd

# Pick the year(s) you want
years = [2023]
weekly_data = nfl.import_weekly_data(years)

# List of players you want to track
players_of_interest = ["Patrick Mahomes", "Christian McCaffrey", "Justin Jefferson"]

# Filter data for those players
filtered = weekly_data[weekly_data["player_display_name"].isin(players_of_interest)]

# Keep only relevant columns
filtered = filtered[["player_display_name", "season", "week", "fantasy_points_ppr"]]

# Export to CSV
#print(filtered)
filtered.to_csv("fantasy_points.csv", index=False)