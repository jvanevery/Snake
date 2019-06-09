/**
 * @author jordan vanevery
 * Describes the events of a turn (timer event) of snake
 */
public enum TurnOutcome{
    EMPTY,              //Nothing of interest happened
    FOOD_COLLISION,     //Snake head encountered food
    WALL_COLLISION,     //Snake head hit wall
    SNAKE_COLLISION;    //Snake head hit snake
}

