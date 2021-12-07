package com.mantarays.socialbackend.Forms;

import java.util.Map;

import com.mantarays.socialbackend.Models.User;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserLoginForm {
    @NonNull
    Map<String, String> tokens;

    @NonNull
    User user;
}
