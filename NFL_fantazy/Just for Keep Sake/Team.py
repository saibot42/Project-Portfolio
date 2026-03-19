from dataclasses import dataclass, field
from typing import List

@dataclass
class Lineup:
    team_name: str
    QBs: List[str] = field(default_factory=list)   # List of quarterbacks
    WRs: List[str] = field(default_factory=list)   # List of wide receivers
    RBs: List[str] = field(default_factory=list)   # List of running backs
    DEF: str = ""                                   # Single defensive team
    bench: List[str] = field(default_factory=list) # Optional bench players

    def get_all_players(self) -> List[str]:
        """
        Returns a list of all active players in the lineup including defense.
        """
        return self.QBs + self.WRs + self.RBs + [self.DEF]

    def get_skill_position_players(self) -> List[str]:
        """
        Returns only offensive skill position players (QBs, WRs, RBs)
        """
        return self.QBs + self.WRs + self.RBs

    def get_defense(self) -> str:
        return self.DEF