package org.example.demoss12maven.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.example.ss14th.entity.Account;
import org.example.ss14th.entity.TransactionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class BankingService {
    @Autowired
    private SessionFactory sessionFactory;

    public void transferMoney(Long fromId, Long toId, double amount) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Account fromAcc = session.find(Account.class, fromId);
            Account toAcc = session.find(Account.class, toId);

            if (fromAcc == null || toAcc == null || fromAcc.getBalance() < amount) {
                throw new RuntimeException("Giao dịch không hợp lệ!");
            }

            fromAcc.setBalance(fromAcc.getBalance() - amount);
            toAcc.setBalance(toAcc.getBalance() + amount);

            session.merge(fromAcc);
            session.merge(toAcc);

            TransactionLog log = new TransactionLog(null, fromId, toId, amount, "SUCCESS", LocalDateTime.now());
            session.persist(log);

            tx.commit();
            System.out.println("Giao dịch thành công!");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void testCache(Long id) {
        Session session = sessionFactory.openSession();
        try {
            System.out.println("--- Kiểm tra First-Level Cache ---");
            Account a1 = session.find(Account.class, id);
            Account a2 = session.find(Account.class, id);
            System.out.println("Kết quả so sánh (a1 == a2): " + (a1 == a2));
        } finally {
            session.close();
        }
    }
}