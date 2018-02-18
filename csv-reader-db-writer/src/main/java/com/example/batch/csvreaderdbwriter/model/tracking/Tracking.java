package com.example.batch.csvreaderdbwriter.model.tracking;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tracking")
@Data
@EqualsAndHashCode(of = "id")
public class Tracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", columnDefinition = "datetime(6) not null default now(6)", insertable = false, updatable = false)
    private Date startTime;

    @Column(name = "end_time", columnDefinition = "datetime(6)")
    private Date endTime;

}
