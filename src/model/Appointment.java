package model;

import java.sql.Time;
import java.sql.Timestamp;

public class Appointment {
    private int id;
    private String title;
    private String desc;
    private String loc;
    private String cont;
    private String type;
    private String start;
    private String end;
    private int custId;
    private int userId;

    public Appointment(int id, String title, String desc, String loc, String cont,
                       String type, String start, String end, int custId, int userId) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.loc = loc;
        this.cont = cont;
        this.type = type;
        this.start = start;
        this.end = end;
        this.custId = custId;
        this.userId = userId;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDesc() { return desc; }
    public String getLoc() { return loc; }
    public String getCont() { return cont; }
    public String getType() { return type; }
    public String getStart() { return start; }
    public String getEnd() { return end; }
    public int getCustId() { return custId; }
    public int getUserId() { return userId; }

}
