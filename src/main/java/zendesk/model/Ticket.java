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
public class Ticket {

  @JsonProperty("_id")
  private String id;
  private String url;
  private String externalId;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss X")
  private Date createdAt;
  private String type;
  private String subject;
  private String description;
  private String priority;
  private String status;
  private long submitterId;
  private long assigneeId;
  private long organizationId;
  private List<String> tags;
  private boolean hasIncidents;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss X")
  private Date dueAt;
  private String via;
}
