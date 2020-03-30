package zendesk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
@JsonNaming(SnakeCaseStrategy.class)
public class Organization {

  @JsonProperty("_id")
  private String id;
  private String url;
  private String externalId;
  private String name;
  private List<String> domainNames;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss X")
  private Date createdAt;
  private String details;
  private boolean sharedTickets;
  private List<String> tags;
}
