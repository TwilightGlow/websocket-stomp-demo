package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

/**
 * @author Javen
 * @date 2022/2/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketUser implements Principal {

    private String userId;
    private String nickName;

    @Override
    public String getName() {
        return userId;
    }
}
