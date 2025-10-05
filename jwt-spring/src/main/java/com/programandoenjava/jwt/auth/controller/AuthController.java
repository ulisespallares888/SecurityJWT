package com.programandoenjava.jwt.auth.controller;

import com.programandoenjava.jwt.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    /*
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
        final TokenResponse response = service.register(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthRequest request) {
        final TokenResponse response = service.authenticate(request);
        return ResponseEntity.ok(response);
    }

        @PostMapping("/refresh-token")
    public TokenResponse refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication
    ) {
        return service.refreshToken(authentication);
    }

    */

    // En AuthController.java

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthRequest request) {
        final TokenResponse response = service.authenticate(request);

        // Crear la cookie con el access token
        ResponseCookie cookie = ResponseCookie.from("jwt", response.accessToken())
                .httpOnly(true)
                .path("/")
                .maxAge(24 * 60 * 60) // 1 día, ajusta según tu necesidad
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Eliminar la cookie estableciéndola vacía y expirando inmediatamente comentari o
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
        final TokenResponse response = service.register(request);

        ResponseCookie accessCookie = ResponseCookie.from("jwt", response.accessToken())
                .httpOnly(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();

        // Opcional: cookie para refresh token
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_jwt", response.refreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication
    ) {
        final TokenResponse response = service.refreshToken(authentication);

        ResponseCookie accessCookie = ResponseCookie.from("jwt", response.accessToken())
                .httpOnly(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_jwt", response.refreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(response);
    }



}
