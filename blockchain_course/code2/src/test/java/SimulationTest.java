import org.junit.Test;


public class SimulationTest {
    @Test
    public void shouldRunCorrectly()
    {
        // There are four required command line arguments: p_graph (.1, .2, .3),
        // p_malicious (.15, .30, .45), p_txDistribution (.01, .05, .10),
        // and numRounds (10, 20). You should try to test your CompliantNode
        // code for all 3x3x3x2 = 54 combinations.

        double p_graph = .1;
        double p_malicious = .15;
        double p_txDistribution =  .10;
        int numRounds = 20;

        Simulation.myrun(p_graph, p_malicious, p_txDistribution, numRounds);


    }
}
