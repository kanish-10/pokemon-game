import java.util.*;

class Pokemon {
    private String name;
    private String type;
    private int health;
    private int maxHealth;
    private int attack;
    private int defence;
    private int specialAttack;
    private int specialDefence;
    private int speed;
    private int level;
    private int experience;
    private List<Move> moves;
    private boolean isWild;

    public Pokemon(String name, String type, int health, int attack, int defence, int specialAttack, int specialDefence, int speed, int level, boolean isWild) {
        this.name = name;
        this.type = type;
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
        this.defence = defence;
        this.specialAttack = specialAttack;
        this.specialDefence = specialDefence;
        this.speed = speed;
        this.level = level;
        this.experience = 0;
        this.moves = new ArrayList<>();
        this.isWild = isWild;
    }

    public void attack(Pokemon opponent, Move move) {
        System.out.println(this.name + " uses " + move.getName() + "!");
        int damage = calculateDamage(move, opponent);
        opponent.takeDamage(damage);
    }

    private int calculateDamage(Move move, Pokemon opponent) {
        Random random = new Random();
        double effectiveness = getTypeEffectiveness(move.getType(), opponent.getType());
        int power = move.getPower();
        int attackStat = move.getCategory().equals("Physical") ? this.attack : this.specialAttack;
        int defenseStat = move.getCategory().equals("Physical") ? opponent.defence : opponent.specialDefence;

        double damage = ((2 * this.level / 5.0 + 2) * power * attackStat / defenseStat / 50.0 + 2) * effectiveness * (random.nextDouble(0.85, 1.0));
        return (int) damage;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) this.health = 0;
        System.out.println(this.name + " takes " + damage + " damage!");
        if (this.health == 0) {
            System.out.println(this.name + " fainted!");
        }
    }

    public void heal(int amount) {
        this.health = Math.min(this.health + amount, this.maxHealth);
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        System.out.println(this.name + " gained " + exp + " experience points!");
        if (this.experience >= 100) {
            levelUp();
        }
    }

    private void levelUp() {
        this.level++;
        this.experience -= 100;
        this.maxHealth += 5;
        this.health = this.maxHealth;
        this.attack += 2;
        this.defence += 2;
        this.specialAttack += 2;
        this.specialDefence += 2;
        this.speed += 2;
        System.out.println(this.name + " leveled up to level " + this.level + "!");
    }

    public void learnMove(Move move, boolean silent) {
        if (this.moves.size() < 4) {
            this.moves.add(move);
            if (!silent) {
                System.out.println(this.name + " learned " + move.getName() + "!");
            }
        } else {
            System.out.println(this.name + " already knows 4 moves. Choose a move to forget:");
            for (int i = 0; i < this.moves.size(); i++) {
                System.out.println((i + 1) + ". " + this.moves.get(i).getName());
            }
            System.out.println("5. Don't learn " + move.getName());

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            if (choice >= 1 && choice <= 4) {
                Move forgottenMove = this.moves.get(choice - 1);
                this.moves.set(choice - 1, move);
                System.out.println(this.name + " forgot " + forgottenMove.getName() + " and learned " + move.getName() + "!");
            } else {
                System.out.println(this.name + " did not learn " + move.getName() + ".");
            }
        }
    }

    private double getTypeEffectiveness(String moveType, String targetType) {
        // Simplified type chart
        if (moveType.equals("Fire") && targetType.equals("Grass")) return 2.0;
        if (moveType.equals("Water") && targetType.equals("Fire")) return 2.0;
        if (moveType.equals("Grass") && targetType.equals("Water")) return 2.0;
        return 1.0;
    }

    // Getters and setters
    public String getName() { return name; }
    public String getType() { return type; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public List<Move> getMoves() { return moves; }
    public boolean isWild() { return isWild; }
    public void setWild(boolean wild) { isWild = wild; }
}

// Move class
class Move {
    private String name;
    private String type;
    private int power;
    private int accuracy;
    private String category;
    private String effect;

    public Move(String name, String type, int power, int accuracy, String category, String effect) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.accuracy = accuracy;
        this.category = category;
        this.effect = effect;
    }

    public void applyEffect(Pokemon target) {
        // Implement status effects here
    }

    // Getters
    public String getName() { return name; }
    public String getType() { return type; }
    public int getPower() { return power; }
    public int getAccuracy() { return accuracy; }
    public String getCategory() { return category; }
    public String getEffect() { return effect; }
}

// Trainer class
class Trainer {
    private String name;
    private List<Pokemon> team;
    private List<Item> inventory;

    public Trainer(String name) {
        this.name = name;
        this.team = new ArrayList<>();
        this.inventory = new ArrayList<>();
    }

    public void catchPokemon(Pokemon wildPokemon) {
        if (team.size() < 6) {
            wildPokemon.setWild(false);
            team.add(wildPokemon);
            System.out.println(name + " caught " + wildPokemon.getName() + "!");
        } else {
            System.out.println("Your team is full! " + wildPokemon.getName() + " was not caught.");
        }
    }

    public void useItem(Item item, Pokemon target) {
        if (inventory.contains(item)) {
            item.use(target);
            inventory.remove(item);
        } else {
            System.out.println("You don't have that item!");
        }
    }

    public Move chooseMove(Pokemon pokemon) {
        System.out.println("Choose a move for " + pokemon.getName() + ":");
        List<Move> moves = pokemon.getMoves();
        for (int i = 0; i < moves.size(); i++) {
            System.out.println((i + 1) + ". " + moves.get(i).getName());
        }

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt() - 1;
        return moves.get(choice);
    }

    // Getters and setters
    public String getName() { return name; }
    public List<Pokemon> getTeam() { return team; }
    public List<Item> getInventory() { return inventory; }
}

// Item class
class Item {
    private String name;
    private String type;
    private String effect;
    private int healAmount;

    public Item(String name, String type, String effect, int healAmount) {
        this.name = name;
        this.type = type;
        this.effect = effect;
        this.healAmount = healAmount;
    }

    public void use(Pokemon target) {
        if (type.equals("Potion")) {
            int actualHeal = Math.min(healAmount, target.getMaxHealth() - target.getHealth());
            target.heal(actualHeal);
            System.out.println(target.getName() + " was healed for " + actualHeal + " HP!");
        }
        // Implement other item effects here
    }

    // Getters
    public String getName() { return name; }
    public String getType() { return type; }
    public String getEffect() { return effect; }
    public int getHealAmount() { return healAmount; }
}

// Main game class
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Pokémon Game!");
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        Trainer player = new Trainer(playerName);

        // Add some potions to the player's inventory
        player.getInventory().add(new Item("Potion", "Potion", "Heals 20 HP", 20));
        player.getInventory().add(new Item("Super Potion", "Potion", "Heals 50 HP", 50));
        player.getInventory().add(new Item("Hyper Potion", "Potion", "Heals 200 HP", 200));
        player.getInventory().add(new Item("Max Potion", "Potion", "Fully heals", 999));

        // Create some starter Pokémon
        Pokemon charmander = new Pokemon("Charmander", "Fire", 39, 52, 43, 60, 50, 65, 5, false);
        charmander.learnMove(new Move("Scratch", "Normal", 40, 100, "Physical", null), true);
        charmander.learnMove(new Move("Ember", "Fire", 40, 100, "Special", null), true);

        Pokemon squirtle = new Pokemon("Squirtle", "Water", 44, 48, 65, 50, 64, 43, 5, false);
        squirtle.learnMove(new Move("Tackle", "Normal", 40, 100, "Physical", null), true);
        squirtle.learnMove(new Move("Water Gun", "Water", 40, 100, "Special", null), true);

        Pokemon bulbasaur = new Pokemon("Bulbasaur", "Grass", 45, 49, 49, 65, 65, 45, 5, false);
        bulbasaur.learnMove(new Move("Tackle", "Normal", 40, 100, "Physical", null), true);
        bulbasaur.learnMove(new Move("Vine Whip", "Grass", 45, 100, "Physical", null), true);

        // Choose starter Pokémon
        System.out.println("Choose your starter Pokémon:");
        System.out.println("1. Charmander");
        System.out.println("2. Squirtle");
        System.out.println("3. Bulbasaur");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                player.getTeam().add(charmander);
                break;
            case 2:
                player.getTeam().add(squirtle);
                break;
            case 3:
                player.getTeam().add(bulbasaur);
                break;
            default:
                System.out.println("Invalid choice. You get Pikachu!");
                player.getTeam().add(new Pokemon("Pikachu", "Electric", 35, 55, 40, 50, 50, 90, 5, false));
        }

        System.out.println("You chose " + player.getTeam().get(0).getName() + "!");

        // Main game loop
        while (true) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Battle a wild Pokémon");
            System.out.println("2. View your team");
            System.out.println("3. Use Items");
            System.out.println("4. Quit");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    battleWildPokemon(player);
                    break;
                case 2:
                    viewTeam(player);
                    break;
                case 3:
                    useItem(player);
                    break;
                case 4:
                    System.out.println("Thanks for playing!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewTeam(Trainer player) {
        System.out.println(player.getName() + "'s team:");
        for (Pokemon pokemon : player.getTeam()) {
            System.out.println(pokemon.getName() + " (HP: " + pokemon.getHealth() + "/" + pokemon.getMaxHealth() + ")");
        }
    }

    private static void battleWildPokemon(Trainer player) {
        Scanner scanner = new Scanner(System.in);
        boolean continueBattling = true;

        while (continueBattling) {
            Pokemon wildPokemon = generateWildPokemon(player);
            System.out.println("A wild " + wildPokemon.getName() + " appeared!");

            Pokemon playerPokemon = player.getTeam().get(0);

            while (playerPokemon.getHealth() > 0 && wildPokemon.getHealth() > 0) {
                System.out.println("\n" + playerPokemon.getName() + " HP: " + playerPokemon.getHealth() + "/" + playerPokemon.getMaxHealth());
                System.out.println(wildPokemon.getName() + " HP: " + wildPokemon.getHealth() + "/" + wildPokemon.getMaxHealth());

                System.out.println("\nWhat will you do?");
                System.out.println("1. Fight");
                System.out.println("2. Try to catch");
                System.out.println("3. Switch Pokémon");
                System.out.println("4. Run");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        Move playerMove = player.chooseMove(playerPokemon);
                        playerPokemon.attack(wildPokemon, playerMove);
                        if (wildPokemon.getHealth() > 0) {
                            Move wildMove = wildPokemon.getMoves().get(new Random().nextInt(wildPokemon.getMoves().size()));
                            wildPokemon.attack(playerPokemon, wildMove);
                        }
                        break;
                    case 2:
                        if (new Random().nextDouble() < 0.5) {  // 50% catch rate for simplicity 
                            player.catchPokemon(wildPokemon);
                            continueBattling = false;
                            break;
                        } else {
                            System.out.println(wildPokemon.getName() + " broke free!");
                            Move wildMove = wildPokemon.getMoves().get(new Random().nextInt(wildPokemon.getMoves().size()));
                            wildPokemon.attack(playerPokemon, wildMove);
                        }
                        break;
                    case 3:
                        playerPokemon = switchPokemon(player);
                         System.out.println("Go, " + playerPokemon.getName() + "!");
                        break;
                    case 4:
                        System.out.println("You ran away safely!");
                        continueBattling = false;
                        return;
                    default:
                        System.out.println("Invalid choice. You lose your turn!");
                }

                if (playerPokemon.getHealth() == 0) {
                    System.out.println(playerPokemon.getName() + " fainted!");
                    if (hasHealthyPokemon(player)) {
                        playerPokemon = switchPokemon(player);
                        System.out.println("Go, " + playerPokemon.getName() + "!");
                    } else {
                        System.out.println("You have no more Pokémon that can fight!");
                        continueBattling = false;
                    }
                }

                if (wildPokemon.getHealth() == 0) {
                    System.out.println("The wild " + wildPokemon.getName() + " fainted!");
                    playerPokemon.gainExperience(50);  // Simple exp gain
                    break;
                }
            }

            if (continueBattling) {
                System.out.println("\nDo you want to continue battling wild Pokémon?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                int choice = scanner.nextInt();
                continueBattling = (choice == 1);

                if (continueBattling) {
                    System.out.println("Do you want to switch your Pokémon before the next battle?");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    choice = scanner.nextInt();
                    if (choice == 1) {
                        playerPokemon = switchPokemon(player);
                        System.out.println("You switched to " + playerPokemon.getName() + "!");
                    }
                }
            }
        }
    }

    private static Pokemon switchPokemon(Trainer player) {
        Scanner scanner = new Scanner(System.in);
        List<Pokemon> team = player.getTeam();

        System.out.println("Choose a Pokémon to switch to:");
        for (int i = 0; i < team.size(); i++) {
            Pokemon pokemon = team.get(i);
            System.out.println((i + 1) + ". " + pokemon.getName() + " (HP: " + pokemon.getHealth() + "/" + pokemon.getMaxHealth() + ")");
        }

        int choice;
        do {
            choice = scanner.nextInt() - 1;
            if (choice < 0 || choice >= team.size() || team.get(choice).getHealth() == 0) {
                System.out.println("Invalid choice or fainted Pokémon. Please choose again.");
            }
        } while (choice < 0 || choice >= team.size() || team.get(choice).getHealth() == 0);

        return team.get(choice);
    }

    private static boolean hasHealthyPokemon(Trainer player) {
        for (Pokemon pokemon : player.getTeam()) {
            if (pokemon.getHealth() > 0) {
                return true;
            }
        }
        return false;
    }
      //code by Tejas T :P
    private static Pokemon generateWildPokemon(Trainer player) {
        Random random = new Random();
        List<Pokemon> wildPokemonList = new ArrayList<>();

        // Pidgey
        Pokemon pidgey = new Pokemon("Pidgey", "Normal", 40, 45, 40, 35, 35, 56, random.nextInt(3) + 3, true);
        pidgey.learnMove(new Move("Tackle", "Normal", 40, 100, "Physical", null), true);
        pidgey.learnMove(new Move("Gust", "Flying", 40, 100, "Special", null), true);
        wildPokemonList.add(pidgey);

        // Rattata
        Pokemon rattata = new Pokemon("Rattata", "Normal", 30, 56, 35, 25, 35, 72, random.nextInt(3) + 3, true);
        rattata.learnMove(new Move("Tackle", "Normal", 40, 100, "Physical", null), true);
        rattata.learnMove(new Move("Quick Attack", "Normal", 40, 100, "Physical", null), true);
        wildPokemonList.add(rattata);

        // Caterpie
        Pokemon caterpie = new Pokemon("Caterpie", "Bug", 45, 30, 35, 20, 20, 45, random.nextInt(3) + 2, true);
        caterpie.learnMove(new Move("Tackle", "Normal", 40, 100, "Physical", null), true);
        caterpie.learnMove(new Move("String Shot", "Bug", 0, 95, "Status", "Lowers target's Speed"), true);
        wildPokemonList.add(caterpie);

         // add pokemon if needed: code by Tejas T :P


        return wildPokemonList.get(random.nextInt(wildPokemonList.size()));
    }

    private static void useItem(Trainer player) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an item to use:");
        List<Item> inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i).getName());
        }
        System.out.println((inventory.size() + 1) + ". Cancel");

        int itemChoice = scanner.nextInt() - 1;
        if (itemChoice == inventory.size()) {
            System.out.println("Cancelled item use.");
            return;
        }

        Item chosenItem = inventory.get(itemChoice);

        System.out.println("Choose a Pokémon to use the item on:");
        List<Pokemon> team = player.getTeam();
        for (int i = 0; i < team.size(); i++) {
            Pokemon pokemon = team.get(i);
            System.out.println((i + 1) + ". " + pokemon.getName() + " (HP: " + pokemon.getHealth() + "/" + pokemon.getMaxHealth() + ")");
        }

        int pokemonChoice = scanner.nextInt() - 1;
        Pokemon targetPokemon = team.get(pokemonChoice);

        player.useItem(chosenItem, targetPokemon);
    }
}
