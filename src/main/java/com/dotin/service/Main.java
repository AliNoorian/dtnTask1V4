package com.dotin.service;

import com.dotin.beans.AccountDTO;
import com.dotin.beans.PaymentDTO;
import com.dotin.exception.CanNotOpenFile;
import com.dotin.exception.LowDepositAmount;
import com.dotin.model.LoadFile;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        try {
            LoadFile loadfile = new LoadFile();

            // Check for deposit numbers, if not exist make it for project
            if (loadfile.getAccountList().isEmpty()) {
                loadfile.getAccountList();
            }

            // Check deposit numbers for pay, if not exist make it for project
            if (loadfile.getPaymentList().isEmpty()) {
                loadfile.getPaymentList();
            }

            // Check transaction file, if not exist make it for project
            if (loadfile.getTransaction().isEmpty()) {
                loadfile.getTransaction();
            }

            List<PaymentDTO> payList = loadfile.getPaymentList();
            List<AccountDTO> accountList = loadfile.getAccountList();

            if (accountList.get(0).getAmount().compareTo(payList.get(0).getAmount()) < 0) {
                throw new LowDepositAmount();
            } else {

                logger.info("Process started, Please wait until finished processing...");
                ServiceThread serviceThread = new ServiceThread();
                CountDownLatch latch = serviceThread.threadExecutor();
                latch.await();
                logger.info("Process finished");

            }
        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            throw new CanNotOpenFile();

        }
    }
}
