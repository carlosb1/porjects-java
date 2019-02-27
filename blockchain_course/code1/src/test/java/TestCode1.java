import org.junit.jupiter.api.Test;

import java.util.UUID;

public class TestCode1 {
    @Test
    void justAnExample() {
        System.out.println("This test method should be run");
    }

    /**
     * creates random hash byte date string as transaction unique id,
     * and adds input uses prevTx hash and output index
     *
     * @param prevTx transaction of corresponding outputs of inputs
     * @param outputIndex
     * @return
     */
    public static Transaction createTransaction (Transaction prevTx, int outputIndex) {
        byte [] hash = UUID.randomUUID().toString().getBytes();
        return new TransactionBuilder()
                .setHash(hash)
                .addInput(prevTx.getHash(), outputIndex)
                .build();
    }

    /**
     * creates random hash byte date string as transaction unique id
     *
     * @return
     */
    public static Transaction createTransaction () {
        byte [] hash = UUID.randomUUID().toString().getBytes();

        return new TransactionBuilder()
                .setHash(hash)
                .build();

    }
}
