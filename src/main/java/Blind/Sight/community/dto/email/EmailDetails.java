package Blind.Sight.community.dto.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDetails {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
    // in case send mail to many users
    private String[] recipients;
}
