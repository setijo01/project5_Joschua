/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpledb.tx.recovery;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import simpledb.buffer.Buffer;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;


public class QuiescentCP extends Thread implements Runnable{
    public static final Integer lock = 0;

    public QuiescentCP(){

    public void run(){
        System.out.println(Transaction.activeTx.size());
            while (Transaction.activeTx.size() >0){
                synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(QuiescentCheckPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
            for (Transaction trans: Transaction.activeTx){
                SimpleDB.bufferMgr().flushAll(trans.getTxNum());
            }
            
            int lsn = new CheckpointRecord().writeToLog();
            SimpleDB.logMgr().flush(lsn);
            Transaction.ongoingCP = false;
            Transaction.lock.notifyAll();
    }
}