package es.um.nosql.s13e.mongosongs_w_yaml;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;
import javax.validation.constraints.NotNull;


@Embedded
public class Rating
{
  @Property
  @NotNull(message = "score can't be null")
  private Double score;
  public Double getScore() {return this.score;}
  public void setScore(Double score) {this.score = score;}
  
  @Property
  @NotNull(message = "voters can't be null")
  private Integer voters;
  public Integer getVoters() {return this.voters;}
  public void setVoters(Integer voters) {this.voters = voters;}
}
