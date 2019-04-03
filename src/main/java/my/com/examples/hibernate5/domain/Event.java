package my.com.examples.hibernate5.domain;

import java.util.Date;

public class Event {
    
    private long id;
    private String title;
    private int price;
    private Date date;

    public Event() {
    }

    public Event(String title, int price, Date date) {
        this.title = title;
        this.price = price;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
