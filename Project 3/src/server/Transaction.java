package server;

import account.Account;

public class Transaction {
    
    int TID;
    int TNUM;
    String IP;
    int port;
    Account Account1;
    Account Account2;

    public Transaction(int TID, int TNUM, String IP, int port){
        this.TID = TID;
        this.TNUM = TNUM;
        this.IP = IP;
        this.port = port;
    }

    public Transaction(int TID, int TNUM, String IP, int port, Account Account1, Account Account2){
        this.TID = TID;
        this.TNUM = TNUM;
        this.IP = IP;
        this.port = port;
        this.Account1 = Account1;
        this.Account2 = Account2;
    }
}
