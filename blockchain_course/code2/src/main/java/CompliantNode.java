import java.util.HashSet;
import java.util.Set;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    private final double p_graph;
    private final double p_malicious;
    private final double p_txDistribution;
    private final int numRounds;
    private Set<Transaction> transactions;
    private boolean[] followees;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this.p_graph = p_graph;
        this.p_malicious = p_malicious;
        this.p_txDistribution = p_txDistribution;
        this.numRounds = numRounds;
        this.transactions = new HashSet<>();
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees;
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        //TODO it is necessary to update
        //TODO discard if we have already as finished transaction
        this.transactions.addAll(pendingTransactions);
        // IMPLEMENT THIS
    }

    public Set<Transaction> sendToFollowers() {
        return this.transactions;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        assert(followees != null);

        Set<Candidate> knownCandidates = new HashSet<>();

        // known candidates
        for (Candidate candidate: candidates) {
              int sender = candidate.sender;
              if (followees[sender]) {
                    knownCandidates.add(candidate);
              }
        }

        for (Candidate candidate: knownCandidates) {
            if (!this.transactions.contains(candidate.tx)) {
                this.transactions.add(candidate.tx);
            }
        }
    }
}
