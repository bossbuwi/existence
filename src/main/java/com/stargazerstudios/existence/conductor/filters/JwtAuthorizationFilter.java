package com.stargazerstudios.existence.conductor.filters;

import com.stargazerstudios.existence.conductor.constants.EnumWebSecurity;
import com.stargazerstudios.existence.conductor.constants.WebSecurityURI;
import com.stargazerstudios.existence.conductor.utils.AuthorityUtil;
import com.stargazerstudios.existence.conductor.utils.JwtUtil;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthorityUtil authorityUtil;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Resource(name = "UserServiceImpl")
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String authHeader = request.getHeader(EnumWebSecurity.HEADER_STRING.getValue());
        String username = null;
        String authToken = null;

        if (authHeader != null && authHeader.startsWith(EnumWebSecurity.TOKEN_PREFIX.getValue())) {
            authToken = authHeader.replace(EnumWebSecurity.TOKEN_PREFIX.getValue(),"");
            try {
                username = jwtUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                logger.warn("An error occurred while fetching username from token", e);
                SecurityContextHolder.clearContext();
            } catch (ExpiredJwtException e) {
                logger.warn("The token has expired.", e);
                SecurityContextHolder.clearContext();
            } catch(SignatureException e){
                logger.warn("Authentication Failed. Username or password not valid.");
                SecurityContextHolder.clearContext();
            }
        } else {
            SecurityContextHolder.clearContext();
            logger.warn("Couldn't find bearer string, header will be ignored.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
            boolean isBanned = authorityUtil.isBanned(authorities);

            if (!isBanned) {
                if (jwtUtil.isTokenValid(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = jwtUtil.getAuthenticationToken(
                            authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("Authenticated user: " + username + ". Setting security context.");
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        Collection<String> excludeUrlPatterns = new ArrayList<>();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        excludeUrlPatterns.add(WebSecurityURI.Unguarded.URI_LOGIN);
        excludeUrlPatterns.add(WebSecurityURI.Unguarded.URI_CONCERTO);
        excludeUrlPatterns.add(WebSecurityURI.Unguarded.URI_FRONTEND_ROOT);
        excludeUrlPatterns.add(WebSecurityURI.Unguarded.URI_USER_COUNT);
        excludeUrlPatterns.add(WebSecurityURI.Unguarded.URI_SYSTEM_COUNT);
        excludeUrlPatterns.add(WebSecurityURI.Unguarded.URI_EVENT_COUNT);
        excludeUrlPatterns.add(WebSecurityURI.Unguarded.URI_EVENT_LATEST);
        return excludeUrlPatterns.stream().anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }
}
