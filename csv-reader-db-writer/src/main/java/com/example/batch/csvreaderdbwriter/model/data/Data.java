package com.example.batch.csvreaderdbwriter.model.data;

import javax.persistence.*;

@Entity
@Table(name = "data")
@lombok.Data
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product;
    private String version;
    private String server;

    @Column(name = "fk_trk_id")
    private Long fkTrkId;

}
