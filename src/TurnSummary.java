/**
 * @author jordan vanevery
 * Holds the overall TurnOutcome in addition to other relevant data regarding
 * the events of the last turn.
 */
public class TurnSummary{

    public final TurnOutcome status;
    public final int snakeLengthGain;
    public final int scoreGain;

    public TurnSummary(TurnOutcome status, int snakeLengthGain, int scoreGain){
        this.status = status;
        this.snakeLengthGain = snakeLengthGain;
        this.scoreGain = scoreGain;
    }
}

