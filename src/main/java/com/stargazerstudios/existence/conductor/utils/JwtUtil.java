package com.stargazerstudios.existence.conductor.utils;

import com.stargazerstudios.existence.conductor.constants.EnumWebSecurity;
import com.stargazerstudios.existence.conductor.constants.WebConstant;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtUtil implements Serializable {
    // TODO: Put useful comments for easier code reading

    @Serial
    private static final long serialVersionUID = 1L;

    @Value("${jwt.secret}")
    private String secret;

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final Date expiration = getExpirationDateFromToken(token);
        final String username = getUsernameFromToken(token);
        boolean isExpired = expiration.before(new Date());
        return (username.equals(userDetails.getUsername()) && !isExpired);
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(final String token,
                                                                      final Authentication existingAuth,
                                                                      final UserDetails userDetails) {
        final JwtParser jwtParser = Jwts.parser().setSigningKey(secret.getBytes());
        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        final Claims claims = claimsJws.getBody();
        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(EnumWebSecurity.AUTHORITY_CLAIMS.getValue()).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(EnumWebSecurity.AUTHORITY_CLAIMS.getValue(), authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + WebConstant.JWT_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}
