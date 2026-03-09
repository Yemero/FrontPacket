import json, os
import readchar

from testing import encounters

# -----------------------------
# Data Structures
# -----------------------------

class GameState:
    def __init__(self, health=100, gold=10, location="town"):
        self.health = health
        self.gold = gold
        self.location = location
        self.inventory = []

    def to_dict(self):
        return {
            "health": self.health,
            "gold": self.gold,
            "location": self.location,
            "inventory": self.inventory
        }

    @staticmethod
    def from_dict(data):
        state = GameState()
        state.health = data.get("health", 100)
        state.gold = data.get("gold", 10)
        state.location = data.get("location", "town")
        state.inventory = data.get("inventory", [])
        return state

class Encounter:
    def __init__(self, description, choices):
        self.description = description
        self.choices = choices

    def play(self, state):
        print("\n" + self.description + "\n")

        # Show choices
        for key, choice in self.choices.items():
            print(f"[{key}] {choice['text']}")

        print("\nPress a key...")

        # Get single key without Enter
        player_choice = readchar.readkey().lower()

        if player_choice in self.choices:
            effect = self.choices[player_choice].get("effect")
            if effect:
                effect(state)
        else:
            print("Invalid choice. You hesitate and lose your turn.")



# -----------------------------
# Example Effects
# -----------------------------

def fight_bandit(state):
    print("You fight the bandit!")
    state.health -= 20
    state.gold += 5
    print("You took 20 damage but gained 5 gold.")

def run_away(state):
    print("You run away!")
    state.health -= 5
    print("You took 5 damage while escaping.")

# -----------------------------
# Game Engine
# -----------------------------

class GameEngine:
    SAVE_FILE = "save.json"

    def __init__(self):
        self.state = GameState()

    def save(self):
        with open(self.SAVE_FILE, "w") as f:
            json.dump(self.state.to_dict(), f)
        print("Game saved.")

    def load(self):
        if os.path.exists(self.SAVE_FILE):
            with open(self.SAVE_FILE, "r") as f:
                data = json.load(f)
                self.state = GameState.from_dict(data)
            print("Game loaded.")
        else:
            print("No save file found, starting new game.")

    def play(self):
        print("Welcome to the Adventure!\n")

        while self.state.health > 0:
            print(f"\n--- STATUS ---\nHealth: {self.state.health} | Gold: {self.state.gold}\n")

            # Example encounter
            encounter = Encounter(
                "A bandit jumps out from the bushes!",
                {
                    "f": {"text": "Fight", "effect": encounters.fight_bandit()},
                    "r": {"text": "Run", "effect": encounters.run_away()},
                    "s": {"text": "Save Game", "effect": lambda s: self.save()},
                }
            )

            encounter.play(self.state)

        print("Game Over! You have died.")


# -----------------------------
# Run the Game
# -----------------------------

if __name__ == "__main__":
    engine = GameEngine()
    engine.load()
    engine.play()
