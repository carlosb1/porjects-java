import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TxHandler {

    private final UTXOPool utxoPool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        this.utxoPool = utxoPool;
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        addOutputsToPool(tx, utxoPool);

        // IMPLEMENT THIS
        //Check output
        for (int index=0; index < tx.getOutputs().size(); index++) {
            if (!this.utxoPool.contains(new UTXO(tx.getHash(),index))) {
                return false;
            }

        }


        int index = 0;
        for (Transaction.Input input: tx.getInputs()) {
                    //input.outputIndex;
                    byte [] outputHash = input.prevTxHash;
                    Transaction.Output txOutput = this.utxoPool.getTxOutput(new UTXO(outputHash, input.outputIndex));
                    if (txOutput == null) {
                        return false;
                    }
                    byte [] raw = tx.getRawDataToSign(index);
                    if (!Crypto.verifySignature(txOutput.address,raw, input.signature)) {
                        return false;
                    }
                    index++;
        }

        for (Transaction.Input input: tx.getInputs()) {
            Set<UTXO> set = new HashSet<UTXO>();
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            if (!set.contains(utxo)) {
                set.add(utxo);
            } else {
                return false;
            }
        }

        for (Transaction.Output output: tx.getOutputs()) {
            if (output.value<0) {
                return false;
            }
        }

        int outputValues = 0;
        for (Transaction.Output output: tx.getOutputs()) {
            outputValues+=output.value;
        }

        int inputValues = 0;
        for  (Transaction.Input input: tx.getInputs()) {
            Transaction.Output txOutput = this.utxoPool.getTxOutput(new UTXO(input.prevTxHash, input.outputIndex));
            if (txOutput == null) {
                continue;
            }
            inputValues+=txOutput.value;
        }
        if (inputValues < outputValues) {
            return false;
        }


        return true;
    }

    public static void addOutputsToPool (Transaction tx, UTXOPool utxoPool) {
        for (int i = 0; i < tx.getOutputs().size(); i++) {
            Transaction.Output output = tx.getOutput(i);
            utxoPool.addUTXO(new UTXO(tx.getHash(), i), output);
        }
        for (int i = 0; i < tx.getInputs().size(); i++) {
            Transaction.Input input = tx.getInput(i);
            UTXO ut = new UTXO(input.prevTxHash, i);
            utxoPool.addUTXO(ut, utxoPool.getTxOutput(ut));
        }
    }


    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction tx : possibleTxs) {
            addOutputsToPool(tx, utxoPool);
        }
        int index = 0;

        for (Transaction tx : possibleTxs) {
            UTXO utxo = new UTXO(tx.getHash(), index);
            if (isNotDoubleSpend(tx) && isValidTx(tx)) {
                result.add(tx);
                utxoPool.addUTXO(utxo, tx.getOutput(index));
            } else if (tx.getInputs() != null && tx.getInputs().size() != 0) {
                utxoPool.removeUTXO(utxo);
            }
        }
        return result.toArray(new Transaction[]{});
    }

    UTXOPool doubleCheckPool = new UTXOPool();
    /**
     *
     * @param tx
     * @return If corresponding outputs of tx's inputs not in other outputs of tx's inputs
     */
    private boolean isNotDoubleSpend(Transaction tx) {
        for (Transaction.Input input : tx.getInputs()) {
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            if (doubleCheckPool.contains(utxo)) {
                return false;
            }
            doubleCheckPool.addUTXO(utxo, doubleCheckPool.getTxOutput(utxo));
        }
        return true;
    }
}
