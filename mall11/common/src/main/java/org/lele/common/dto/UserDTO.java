package org.lele.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

/**
 * org.lele.common.dto
 *  继承自spring security User的自定义实现
 * @author: lele
 * @date: 2020-05-07
 */
@Data
public class UserDTO extends User {

    private Long id;
    private String phone;
    @ApiModelProperty("创建时间")
    private LocalDateTime created;
    @ApiModelProperty("更新时间")
    private LocalDateTime updated;


    public UserDTO(String username, String password,
                   Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}