
public class Connect4State implements MutableState {
  /**
   * Constant for number of cells on a Connect-4 board
   */
  public static int NUM_CELLS = 42;

  public static String VAR_GAME_BOARD = "gameBoard";
  public static String VAR_GAME_STATUS = "gameStatus";


  public static int MARK1 = 1;
  public static int MARK2 = 2;
  public static int EMPTY = 0;

  public static String GAME_STATUS_IN_PROGRESS = "I";
  public static String GAME_STATUS_1_WON =       "1";
  public static String GAME_STATUS_2_WON =       "2";
  public static String GAME_STATUS_CATS_GAME =   "C";

  public static String EMPTY_BOARD = "000000000"; //TODO: Put back
  public static String ONE_X_BOARD = "100000000"; //TODO: Put back

  /**
   * String representation of cells on the game board.
   * For example: "XOIIXOXIO"
   */
  public String gameBoard = EMPTY_BOARD;

  /**
   * Game status, specifically, whether the game is in-progress, or if X won,
   * or if O won, or if it is cat's game (nobody won).
   */
  public String gameStatus = GAME_STATUS_IN_PROGRESS;

  private final static List<Object> keys =
      Arrays.asList(VAR_GAME_BOARD, VAR_GAME_STATUS);

  public Connect4State() {
  }

  public Connect4State(String gameBoard, String gameStatus) {
    this.gameBoard = gameBoard;
    this.gameStatus = gameStatus;
  }

  @Override
  // Get the game state
  // Run the loop to see if anyone won
  // Then run through accordingly
  public MutableState set(Object variableKey, Object value) {
    if(variableKey.equals(VAR_GAME_BOARD)){
      this.gameBoard = (String)value;
    }
    else if(variableKey.equals(VAR_GAME_STATUS)){
      this.gameStatus = (String)value;
    }
    else{
      throw new UnknownKeyException(variableKey);
    }
    return this;
  }

  @Override
  public List<Object> variableKeys() {
    return keys;
  }

  @Override
  public Object get(Object variableKey) {
    if(variableKey.equals(VAR_GAME_BOARD)){
      return this.gameBoard;
    }
    else if(variableKey.equals(VAR_GAME_STATUS)){
      return this.gameStatus;
    }
    throw new UnknownKeyException(variableKey);
  }
  
  // Need to implement a Set
  public Set() {
	  
  }

  @Override
  public Connect4State copy() {
    return new Connect4State(gameBoard, gameStatus);
  }

  @Override
  public String toString() {
    return StateUtilities.stateToString(this);
  }
}
