package zendesk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import java.util.Date;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(SnakeCaseStrategy.class)
public class User {

  @JsonProperty("_id")
  private long id;
  private String url;
  private String externalId;
  private String name;
  private String alias;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss X")
  private Date createdAt;
  private boolean active;
  private boolean verified;
  private boolean shared;
  private String locale;
  private String timezone;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss X")
  private Date lastLoginAt;
  private String email;
  private String phone;
  private String signature;
  private int organizationId;
  private List<String> tags;
  private boolean suspended;
  private String role;
}
