package com.organization.taskManagement.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;

/**
 * Custom annotation to retrieve the currently authenticated user cleanly.
 * Replaces the verbose @AuthenticationPrincipal UserInfoDetails.
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
