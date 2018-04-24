package ch.fhnw.edu.wodss.tippspielapi.model;

import java.util.Locale;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Data
public class Nation {

  @Id
  @Size(min = 2, max = 3)
  @Column(length = 3)
  private String id;

  @Column
  private String code;

  public enum TournamentGroup {
    A, B, C, D, E, F, G, H
  }

  @Enumerated(EnumType.STRING)
  private TournamentGroup tournamentGroup;

}
