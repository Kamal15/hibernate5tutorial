package in.co.nmsworks.hibernate5.domain;

import java.util.Set;

/**
 * Created by kamal (kamal@nmsworks.co.in) on 2/4/17.
 * <p>
 * Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
 * NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
 */
public class Person {
    
    private int id;
    private String name;
    private Set<Address> addresses;

    public Person() {
    }

    public Person(String name, Set<Address> addresses) {
        this.name = name;
        this.addresses = addresses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addresses=" + addresses +
                '}';
    }
}
