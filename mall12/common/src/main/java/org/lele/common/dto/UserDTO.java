package org.lele.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("用户id:"+id)
                .append( "联系电话："+phone )
                .append(super.toString());
        return sb.toString();
    }
}