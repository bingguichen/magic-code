package com.bin.csp.demo.ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

public class BankCardARTest {
    private static final Logger logger = LoggerFactory.getLogger(BankCardARTest.class);
    private static AtomicReference<BankCard> bankCardRef = new AtomicReference<>(new BankCard("bin",100));

    public static void main(String[] args) {

        for(int i = 0;i < 10;i++){
            new Thread(() -> {
//                while (true){
                    // 使用 AtomicReference.get 获取
                    final BankCard card = bankCardRef.get();
                    BankCard newCard = new BankCard(card.getAccountName(), card.getMoney() + 100);
                    // 使用 CAS 乐观锁进行非阻塞更新
                    if(bankCardRef.compareAndSet(card,newCard)){
                        logger.info(String.valueOf(newCard));
                    }
//                }
            }).start();

//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }
}
