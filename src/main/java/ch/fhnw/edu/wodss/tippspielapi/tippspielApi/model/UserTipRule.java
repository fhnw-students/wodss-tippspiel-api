package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserTipRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tip_id", nullable = false)
    private Tip tip;

    @ManyToOne
    @JoinColumn(name = "tip_rule_id", nullable = false)
    private TipRule tipRule;

    private Integer points;

}
