
from dataclasses import dataclass, fields
import pandas as pd

# ==============================
# Fantasy Scoring Dataclasses
# ==============================

#| Stat            | Points (example)                               |
#| --------------- | ---------------------------------------------- |
#| Passing Yards   | 1 point per 25 yards                           |
#| Passing TD      | 4 points                                       |
#| Interception    | -2 points                                      |
#| Rushing Yards   | 1 point per 10 yards                           |
#| Rushing TD      | 6 points                                       |
#| Reception       | 1 point per catch (PPR: 1 point per reception) |
#| Receiving Yards | 1 point per 10 yards                           |
#| Receiving TD    | 6 points                                       |
#| Fumbles Lost    | -2 points                                      |
#| 2-pt Conversion | 2 points                                       |

@dataclass
class FantasyScoringOffense:
    passing_yards: float = 1/25
    passing_tds: float = 4
    interceptions: float = -2
    rushing_yards: float = 1/10
    rushing_tds: float = 6
    rushing_fumbles_lost: float = -2
    receptions: float = 1
    receiving_yards: float = 1/10
    receiving_tds: float = 6
    receiving_fumbles_lost: float = -2
    two_pt_conversions: float = 2

@dataclass
class FantasyScoringDefense:
    sacks: float = 1
    interceptions: float = 2
    fumbles_forced: float = 2
    touchdowns: float = 6
    points_allowed_0: float = 10
    points_allowed_1_6: float = 7
    points_allowed_7_13: float = 4
    points_allowed_14_20: float = 1
    points_allowed_21_27: float = 0
    points_allowed_28_34: float = -1
    points_allowed_35_plus: float = -4

#Calculates the sum of a players fantazy pts
def calculateOffensePlayerPTS(data: pd.Series, scoring: FantasyScoringOffense, pla) -> float:
    total_points = 0
    for field in fields(scoring):
        stat = field.name
        if stat in data:
            total_points += data[stat] * getattr(scoring, stat)
    return total_points

def calculate_defense_pts(defense_data: pd.DataFrame, scoring: FantasyScoringDefense) -> pd.DataFrame:
    df = defense_data.copy()
    df['defense_points'] = 0

    for field in fields(scoring):
        stat = field.name
        if stat in df.columns:
            df['defense_points'] += df[stat] * getattr(scoring, stat)

    return df